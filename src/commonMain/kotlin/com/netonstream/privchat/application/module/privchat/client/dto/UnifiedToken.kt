package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Unified token API DTOs（spec TOKEN_UNIFICATION_SPEC v1.3 §6.1）。
 *
 * **重要边界**：本文件 DTOs 是 application → server 的内部传输形态，
 * 与 application 对外的 [com.netonstream...controller.app.auth.dto.MemberLoginResponse]
 * 是两个独立的 contract。Phase A 期间：
 *
 * - 登录入口仍返 `MemberLoginResponse`（不变）
 * - `UnifiedLoginResponse` 仅作为 `PrivchatServiceClient.issueAuthToken` 返回值
 * - Phase B 才考虑映射 / 合并；Phase A 不能动 `MemberLoginResponse`
 *
 * server 端字段命名为 `snake_case`（见 `privchat-server/src/http/routes/auth.rs`），
 * 因此本 DTO 全部用 `@SerialName` 显式映射。
 */

// ──────────── /api/service/auth/issue ────────────

/**
 * 颁发 unified token 的请求体。**service-only** 入口（spec §6.1.1）。
 *
 * - `userId`: 必须是 `privchat_users` 中已存在的 uid（server 会用它在
 *   `privchat_devices` 上做 FK upsert，缺 user 会失败）
 * - `deviceId`: 客户端持有则填；为 null 时 server 自动生成 UUID
 * - `scope`: application 决定授予范围；server 不二次审 RBAC（spec §6.1.1）
 * - `audience`: 缺省走 server `[auth.rsa_jwt].default_audience`
 */
@Serializable
data class IssueAuthTokenRequest(
    @SerialName("user_id") val userId: Long,
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("device_info") val deviceInfo: DeviceInfoInput,
    val scope: List<String>? = null,
    val audience: List<String>? = null,
    @SerialName("business_system_id") val businessSystemId: String? = null,
)

/**
 * Unified token 颁发结果（spec §8 `LoginResponse`，server 直出）。
 *
 * 与 `MemberLoginResponse` **不同**：这是 server-application 内部 DTO，
 * 不直接返客户端。Phase A 仅供 application middleware / verifier 用。
 */
@Serializable
data class UnifiedLoginResponse(
    @SerialName("user_id") val userId: Long,
    @SerialName("access_token") val accessToken: String,
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("token_type") val tokenType: String = "Bearer",
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("refresh_expires_in") val refreshExpiresIn: Long,
    @SerialName("device_id") val deviceId: String,
    @SerialName("session_version") val sessionVersion: Long,
    @SerialName("device_created") val deviceCreated: Boolean,
    val scope: List<String>,
    val audience: List<String>,
    val issuer: String,
    /** server 当前签名 key 的 kid，便于客户端调试 / 比对 JWKS。 */
    val kid: String,
)

// ──────────── /api/service/auth/refresh ────────────

/**
 * 刷新 access token。Phase A 不 rotate refresh：server 返新 access、原样返同一 refresh，
 * 仅 touch `last_used_at`（spec §6.1）。
 */
@Serializable
data class RefreshAuthTokenRequest(
    @SerialName("refresh_token") val refreshToken: String,
    @SerialName("device_id") val deviceId: String,
)

// ──────────── /api/service/auth/introspect ────────────

@Serializable
data class IntrospectAuthTokenRequest(
    val token: String,
)

/**
 * Token 内省结果（spec §6.1）。**业务上永远成功**：token 失效仅通过
 * `active=false` + [reason] 表达，envelope code 仍 0。
 *
 * `reason` 字符串集合（server 端 `unified_token_service.rs::IntrospectResult::inactive`）：
 *
 * - `expired` —— `exp` 早于 server now
 * - `revoked` —— refresh record 被 revoke 或 device session_state=Revoked
 * - `version_mismatch` —— claim 中 session_version < DB 当前值（被 bump 了）
 * - `signature_invalid` —— 签名 / typ / 格式错误
 * - `unknown_kid` —— JWT header `kid` 不在当前 JWKS（rotation 期 stale 缓存）
 * - `not_found` —— device / refresh record 不存在（已被清理 / 从未颁发）
 *
 * application middleware 见到 `active=false` 必须 **fail closed**（401），
 * 不能依据 reason 做"软"放行。
 */
@Serializable
data class IntrospectAuthTokenResponse(
    val active: Boolean,
    @SerialName("user_id") val userId: Long? = null,
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("session_version") val sessionVersion: Long? = null,
    val scope: List<String>? = null,
    val audience: List<String>? = null,
    @SerialName("expires_at") val expiresAt: Long? = null,
    val jti: String? = null,
    val reason: String? = null,
)

// ──────────── /api/service/auth/revoke ────────────

/**
 * 撤销 refresh token。**互斥**参数（spec §6.1）：必须传 `jti` 或 `refreshToken` 之一，
 * 同传 / 都不传 → server 返 envelope error。
 */
@Serializable
data class RevokeAuthTokenRequest(
    val jti: String? = null,
    @SerialName("refresh_token") val refreshToken: String? = null,
)

@Serializable
data class RevokeAuthTokenResponse(
    /** 受影响的 refresh 行数；0 = miss / 已是 revoked 状态（幂等）。 */
    @SerialName("revoked_count") val revokedCount: Long,
)

// ──────────── JWKS ────────────

/**
 * 单条 JWKS RSA key（spec §6.1 `GET /api/service/auth/jwks`）。
 *
 * `n` / `e` 是 base64url-no-pad 大端字节。组合成 `RSAPublicKey`：
 * - `modulus = BigInteger(1, base64url_decode(n))`
 * - `exponent = BigInteger(1, base64url_decode(e))`
 */
@Serializable
data class JwkRsa(
    val kid: String,
    val kty: String,
    val alg: String,
    @SerialName("use") val use_: String,
    val n: String,
    val e: String,
)

@Serializable
data class JwkSet(
    val keys: List<JwkRsa>,
)

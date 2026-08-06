package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 设备元信息（与 server `auth::models::DeviceInfo` 对齐）。
 *
 * server 端这些字段是非 nullable `String`（schema 强制非空），所以本 DTO 必填字段
 * 用 `String` + 默认 `""`；`userAgent` 在 server 是 `Option<String>`，这里用 `String?`
 * 配合 `explicitNulls = false` 省略 null。
 */
@Serializable
data class DeviceInfoInput(
    @SerialName("app_id") val appId: String,
    @SerialName("device_name") val deviceName: String = "",
    @SerialName("device_model") val deviceModel: String = "",
    @SerialName("os_version") val osVersion: String = "",
    @SerialName("app_version") val appVersion: String = "",
    @SerialName("ip_address") val ipAddress: String = "",
    @SerialName("user_agent") val userAgent: String? = null,
)

/**
 * IM Token 签发请求（`POST /api/service/users/{uid}/tokens`）。
 * uid 在 URL path，body 不含。
 */
@Serializable
data class IssueImTokenRequest(
    @SerialName("device_id") val deviceId: String? = null,
    @SerialName("device_info") val deviceInfo: DeviceInfoInput,
    val ttl: Long? = null,
    @SerialName("business_system_id") val businessSystemId: String? = null,
)

@Serializable
data class IssueImTokenResponse(
    @SerialName("im_token") val imToken: String,
    @SerialName("device_id") val deviceId: String,
    @SerialName("expires_in") val expiresIn: Long,
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("session_version") val sessionVersion: Long,
    @SerialName("device_created") val deviceCreated: Boolean,
    /** v1.3 新增（spec TOKEN_API §3.3）：客户端走 server 内置 refresh RPC 续签 im_token。 */
    @SerialName("im_refresh_token") val imRefreshToken: String = "",
    /** v1.3 新增：refresh token 过期时间（秒）。 */
    @SerialName("im_refresh_expires_in") val imRefreshExpiresIn: Long = 0,
)

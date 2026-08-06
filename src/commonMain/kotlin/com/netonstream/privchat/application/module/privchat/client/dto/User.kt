package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 创建用户请求（与 server `USER_API §3` 对齐）。
 * `phone` / `email` / `username` 至少一个非空；全空 → 400 INVALID_USER_IDENTITY。
 *
 * 不含 `metadata` 字段——server P0 #2 contract-fix 拒绝 metadata（USER_API §3.2）。
 */
@Serializable
data class CreateUserRequest(
    val phone: String? = null,
    val username: String? = null,
    val email: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    /** 0=NORMAL（默认）, 1=SYSTEM(保留), 2=BOT */
    @SerialName("user_type") val userType: Int? = null,
    @SerialName("business_system_id") val businessSystemId: String? = null,
)

@Serializable
data class CreateUserResponse(
    @SerialName("user_id") val userId: Long,
    val created: Boolean,
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("user_type") val userType: Int = 0,
    @SerialName("business_system_id") val businessSystemId: String? = null,
    @SerialName("created_at") val createdAt: Long? = null,
)

@Serializable
data class UserInfo(
    @SerialName("user_id") val userId: Long,
    val username: String? = null,
    val phone: String? = null,
    val email: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("user_type") val userType: Int = 0,
    /** server 不一致：list 端点返 Int，detail 端点返 String。统一吃成 String。 */
    @kotlinx.serialization.Serializable(with = FlexibleStatusSerializer::class)
    val status: String? = null,
    @SerialName("business_system_id") val businessSystemId: String? = null,
    @SerialName("created_at") val createdAt: Long = 0,
    @SerialName("updated_at") val updatedAt: Long = 0,
)

/**
 * `PUT /api/service/users/{uid}` 请求体。所有字段可选；为 null 表示不修改。
 *
 * `username`：application member 改名的镜像通道（spec MODULE_MEMBER_PROFILE_SPEC §7）。
 * server 只做 DB UNIQUE 兜底，冲突回 409；不做格式校验、保留词、频控——application 是守门人。
 *
 * **第一版只表达"present, non-null"语义**：null 字段一律视作"不修改"。
 * 显式清空 avatar / username（DB 写 NULL）目前不支持；如果未来需要 absent vs present(null)
 * 区分，应改成 sealed PatchField，避免在 wire 层歧义。
 */
@Serializable
data class UpdateUserRequest(
    val username: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    val email: String? = null,
    val phone: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    val status: Int? = null,
    @SerialName("user_type") val userType: Int? = null,
)

/**
 * `POST /api/service/users/{uid}/suspend` 请求体。
 *
 * `durationSecs` 为 null 表示永久封禁；非 null 表示自动解封倒计时。
 */
@Serializable
data class SuspendUserRequest(
    val reason: String,
    @SerialName("duration_secs") val durationSecs: Long? = null,
)

/**
 * `GET /api/service/users` 分页 + 过滤参数。
 *
 * 不直接序列化（query string 由 client 自行拼接），但本类作为 client 接口契约的一部分。
 */
data class ListUsersQuery(
    val page: Int = 1,
    val pageSize: Int = 20,
    val search: String? = null,
    val status: Int? = null,
    val userType: Int? = null,
    val businessSystemId: String? = null,
)

/**
 * `GET /api/service/users` 响应。spec SERVICE_CLIENT_CONTRACT §3.1 描述为
 * `PageResult<UserInfo>`，server 实际字段名为 `users` —— 此处与 server 同形以减少映射层。
 */
@Serializable
data class ListUsersResponse(
    val users: List<UserInfo>,
    val total: Long,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
)

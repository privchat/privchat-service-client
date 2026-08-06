package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * IM 登录日志条目（`GET /api/service/login-logs` items）。
 *
 * 与 server side `privchat_login_logs` 表的 admin 投影对齐。详情字段（token_jti、
 * device_model、user_agent、风控因子、metadata）走 `GET /login-logs/{id}` 单独取，
 * 列表只回常用展示字段。
 */
@Serializable
data class LoginLogItem(
    @SerialName("log_id") val logId: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("device_id") val deviceId: String,
    /** ios / android / macos / windows / linux / web / unknown */
    @SerialName("device_type") val deviceType: String? = null,
    @SerialName("device_name") val deviceName: String? = null,
    @SerialName("ip_address") val ipAddress: String? = null,
    /** 0=success, 1=suspicious, 2=blocked */
    val status: Short = 0,
    @SerialName("risk_score") val riskScore: Int? = null,
    @SerialName("is_new_device") val isNewDevice: Boolean = false,
    @SerialName("is_new_location") val isNewLocation: Boolean = false,
    @SerialName("created_at") val createdAt: Long = 0,
)

@Serializable
data class ListLoginLogsResponse(
    val logs: List<LoginLogItem>,
    val total: Int,
    val page: Long,
    @SerialName("page_size") val pageSize: Long,
)

/** 登录日志查询参数（client 端按 query string 拼接）。 */
data class ListLoginLogsQuery(
    val userId: Long? = null,
    val ipAddress: String? = null,
    val status: Short? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val page: Long = 1,
    val pageSize: Long = 20,
)

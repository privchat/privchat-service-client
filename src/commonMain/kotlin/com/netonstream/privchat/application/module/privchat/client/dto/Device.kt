package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 单个设备信息（与 server `GET /api/service/users/{uid}/devices` 返回 item 对齐）。
 */
@Serializable
data class DeviceInfo(
    @SerialName("device_id") val deviceId: String,
    @SerialName("device_name") val deviceName: String? = null,
    @SerialName("device_model") val deviceModel: String? = null,
    @SerialName("app_id") val appId: String,
    /** ios / android / macos / windows / linux / mobile / desktop / web / unknown */
    @SerialName("device_type") val deviceType: String,
    @SerialName("last_active_at") val lastActiveAt: Long,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("ip_address") val ipAddress: String? = null,
)

@Serializable
data class ListDevicesResponse(
    @SerialName("user_id") val userId: Long,
    val devices: List<DeviceInfo>,
    val total: Int,
)

/**
 * `POST /api/service/devices/{deviceId}/revoke` 请求体。
 *
 * `userId` 必须传，server 用来校验设备归属（防止误踢）。
 */
@Serializable
data class RevokeDeviceRequest(
    @SerialName("user_id") val userId: Long,
    val reason: String? = null,
)

@Serializable
data class RevokeDeviceResponse(
    val success: Boolean,
    @SerialName("device_id") val deviceId: String,
    @SerialName("user_id") val userId: Long,
    val message: String,
)

/**
 * `POST /api/service/users/{uid}/revoke-all-devices` 请求体。
 */
@Serializable
data class RevokeAllDevicesRequest(
    val reason: String? = null,
)

@Serializable
data class RevokeAllDevicesResponse(
    val success: Boolean,
    @SerialName("user_id") val userId: Long,
    @SerialName("revoked_count") val revokedCount: Int,
    val message: String,
)

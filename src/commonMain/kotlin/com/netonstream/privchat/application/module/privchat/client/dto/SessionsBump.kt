package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * `POST /api/service/users/{uid}/sessions/bump` 请求体。
 *
 * 仅推进 `session_version`，不改 `session_state`（不踢设备）。改密 / 改手机号场景使用。
 */
@Serializable
data class BumpSessionsRequest(
    val reason: String? = null,
)

@Serializable
data class BumpSessionsResponse(
    @SerialName("user_id") val userId: Long,
    @SerialName("devices_affected") val devicesAffected: Int,
    val reason: String? = null,
)

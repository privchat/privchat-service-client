package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * `GET /api/service/channels/{channel_id}/members/{user_id}` 响应（资金授权 #84）。
 * channel 不存在或非成员 → is_member=false。
 */
@Serializable
data class ChannelMemberCheck(
    @SerialName("is_member") val isMember: Boolean = false,
)

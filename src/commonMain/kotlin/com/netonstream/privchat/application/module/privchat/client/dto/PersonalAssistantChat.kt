package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EnsurePersonalAssistantDirectRequest(
    @SerialName("owner_user_id") val ownerUserId: Long,
    @SerialName("assistant_uid") val assistantUid: Long,
)

@Serializable
data class EnsurePersonalAssistantDirectResponse(
    @SerialName("channel_id") val channelId: Long,
    val created: Boolean,
)

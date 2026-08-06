package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * `POST /api/service/messages/send-system` 请求体。
 *
 * 仅特殊用途（Bot 启动欢迎语、运营公告等），普通业务不应直接调用 —— 走 IM 客户端发送。
 */
@Serializable
data class SendSystemMessageRequest(
    @SerialName("channel_id") val channelId: Long,
    val content: String,
    @SerialName("message_type") val messageType: String? = null,
    val metadata: JsonElement? = null,
    /**
     * 发送者 user_id（可选）。null 时 server 用 SYSTEM_USER_ID。
     * **必须** user_type ∈ {1=System, 2=Bot}，server 强校验。
     */
    @SerialName("sender_id") val senderId: Long? = null,
)

@Serializable
data class SendSystemMessageResponse(
    val success: Boolean,
    @SerialName("message_id") val messageId: Long,
    @SerialName("channel_id") val channelId: Long,
    @SerialName("created_at") val createdAt: Long,
    val message: String,
)

/**
 * `POST /api/service/messages/send` 请求体（允许任意 sender_id）。
 *
 * RP-12：资金消息卡片服务端注入用——sender=发送方真实 uid，messageType=red_packet/money_transfer，
 * dedupKey=red_packet:{id}/money_transfer:{id} 保证同订单至多一张卡片（server ON CONFLICT）。
 */
@Serializable
data class SendMessageRequest(
    @SerialName("channel_id") val channelId: Long,
    @SerialName("sender_id") val senderId: Long,
    val content: String,
    @SerialName("message_type") val messageType: String? = null,
    val metadata: JsonElement? = null,
    @SerialName("dedup_key") val dedupKey: String? = null,
)

@Serializable
data class SendMessageResponse(
    val success: Boolean,
    @SerialName("message_id") val messageId: Long,
    @SerialName("channel_id") val channelId: Long,
    @SerialName("sender_id") val senderId: Long,
    @SerialName("created_at") val createdAt: Long,
    val message: String,
)

/**
 * `POST /api/service/system-messages/send-to-user` 请求体。
 *
 * 服务端按 SYSTEM_USER_ID ⇄ targetUid ensure direct channel + 写消息，
 * 调用方不需要事先解析 channel_id。
 */
@Serializable
data class SendSystemMessageToUserRequest(
    @SerialName("user_id") val userId: Long,
    val content: String,
    @SerialName("message_type") val messageType: String? = null,
    val metadata: JsonElement? = null,
    /**
     * 发送者 user_id（可选）。null 时 server 用 SYSTEM_USER_ID。
     * **必须** user_type ∈ {1=System, 2=Bot}，server 强校验。
     */
    @SerialName("sender_id") val senderId: Long? = null,
)

@Serializable
data class SendSystemMessageToUserResponse(
    val success: Boolean,
    @SerialName("user_id") val userId: Long,
    @SerialName("channel_id") val channelId: Long,
    @SerialName("message_id") val messageId: Long,
    @SerialName("created_at") val createdAt: Long,
)

/**
 * `GET /api/service/system-messages/senders` 返回项。user_type ∈ {1=System, 2=Bot}。
 */
@Serializable
data class SystemSenderInfo(
    @SerialName("user_id") val userId: Long,
    val username: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
    @SerialName("user_type") val userType: Int,
)

@Serializable
data class ListSystemSendersResponse(
    val items: List<SystemSenderInfo>,
)

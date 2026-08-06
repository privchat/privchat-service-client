package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * 消息条目（`GET /api/service/messages` items）。
 *
 * `metadata` 是任意 JSON（图片 / 文件 url、引用消息、@提及等），保留为
 * [JsonElement]，由调用方按需自行解码。
 *
 * `messageType` server 侧映射为字符串（"text" / "image" / ...），原始数值字段不暴露。
 *
 * 默认值字段加 [@EncodeDefault.ALWAYS] —— neton 默认序列化会丢"等于默认值"的字段，
 * application 转发到前端时会变成空白；这里强制保留。
 */
@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class MessageItem(
    @SerialName("message_id") val messageId: Long,
    @SerialName("channel_id") val channelId: Long,
    @SerialName("sender_id") val senderId: Long,
    val pts: Long,
    @SerialName("local_message_id") val localMessageId: Long? = null,
    val content: String,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS)
    @SerialName("message_type") val messageType: String = "text",
    val metadata: JsonElement? = null,
    @SerialName("reply_to_message_id") val replyToMessageId: Long? = null,
    @SerialName("created_at") val createdAt: Long,
    @SerialName("updated_at") val updatedAt: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) val deleted: Boolean = false,
    @SerialName("deleted_at") val deletedAt: Long? = null,
    @EncodeDefault(EncodeDefault.Mode.ALWAYS) val revoked: Boolean = false,
    @SerialName("revoked_at") val revokedAt: Long? = null,
    @SerialName("revoked_by") val revokedBy: Long? = null,
)

@Serializable
data class ListMessagesResponse(
    val messages: List<MessageItem>,
    val total: Int,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
)

/**
 * 消息查询参数。
 *
 * `channelId` / `userId` 至少传一个才有意义（否则是全服扫描），但 server 不强制：
 * 控制层应在 controller 处把 v1 不允许的"双 null"组合直接拒掉。
 */
data class ListMessagesQuery(
    val channelId: Long? = null,
    val userId: Long? = null,
    val startTime: Long? = null,
    val endTime: Long? = null,
    val page: Int = 1,
    val pageSize: Int = 20,
)

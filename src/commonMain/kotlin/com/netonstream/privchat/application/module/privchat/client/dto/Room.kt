package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * `POST /api/service/room` 请求体（spec ROOM_CHANNEL_SPEC §15.3）。
 *
 * Room create 是 server 端**纯内存** channel 的注册（不持久化）；server 内部分配
 * channel_id 返回，业务方负责持久化映射（如 module-game 的 `game_channel_binding`）。
 *
 * @property name 可选名称，仅 server 端 ops/debug 列表展示用；空时 server 自动填 `Room-{channel_id}`。
 */
@Serializable
data class CreateRoomRequest(
    val name: String? = null,
)

/**
 * `POST /api/service/room` 响应 data。
 *
 * **不**接受 `channel_type`——Room 与 channel_type 解耦，调用方关心的是返回的 channel_id。
 * 业务方拿到 channel_id 后写自己的 binding 表（如 `game_channel_binding(scope_type, scope_id, channel_id)`）。
 */
@Serializable
data class CreateRoomResponse(
    val success: Boolean = true,
    @SerialName("channel_id") val channelId: Long,
    val name: String? = null,
    val message: String? = null,
)

/**
 * `POST /api/service/room/{channel_id}/broadcast` 请求体。
 *
 * Room（业务/游戏频道）广播：server 用发布订阅协议（`PublishRequest`）把 `content`
 * 推给该 channel 所有在线订阅者，并写入 room history（迟到订阅者可拉历史）。
 * 与 `sendSystemMessage` 不同 —— 后者写 `privchat_messages`（FK→`privchat_channels`），
 * 而 Room channel 是 server 纯内存频道，必须走本广播入口。
 *
 * @property content 推送负载（游戏事件 JSON 串），到客户端即 `PublishRequest.payload` 字节。
 * @property senderId 可选发送者；null 时 server 不带 publisher。
 */
@Serializable
data class RoomBroadcastRequest(
    val content: String,
    @SerialName("sender_id") val senderId: Long? = null,
)

@Serializable
data class RoomBroadcastResponse(
    val success: Boolean = true,
    @SerialName("channel_id") val channelId: Long = 0,
    @SerialName("online_count") val onlineCount: Int = 0,
    val delivered: Int = 0,
    @SerialName("server_message_id") val serverMessageId: Long? = null,
    val message: String? = null,
)

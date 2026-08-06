package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * `POST /api/service/room-tickets/issue` 请求体（spec ROOM_CHANNEL_SPEC §4.5）。
 *
 * 业务 API（如 module-game）在做完业务权限判定后调本端点拿一张 HS256 JWT，
 * 再回给客户端用作 `SubscribeRequest.param`。
 *
 * - [scope] 缺省按 server 端 `"subscribe"`（spec §4.3 当前唯一 scope）
 * - [ttlSecs] 缺省 300；server 强制 [30, 3600] 区间，越界返 400
 * - [kid] 多 key 轮换时显式指定签名 key；缺省走 server `config.default_kid`
 */
@Serializable
data class IssueRoomTicketRequest(
    @SerialName("channel_id") val channelId: Long,
    @SerialName("user_id") val userId: Long,
    @SerialName("device_id") val deviceId: String,
    val scope: String? = null,
    @SerialName("ttl_secs") val ttlSecs: Long? = null,
    val kid: String? = null,
)

@Serializable
data class IssueRoomTicketResponse(
    /** HS256 JWT，client 作 SubscribeRequest.param 发出 */
    val ticket: String,
    @SerialName("channel_id") val channelId: Long,
    @SerialName("user_id") val userId: Long,
    /** 绝对过期时间（Unix 秒）；客户端 / 业务侧用来调度 refresh */
    val exp: Long,
)

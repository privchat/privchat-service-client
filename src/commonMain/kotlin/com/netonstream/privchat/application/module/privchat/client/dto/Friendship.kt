package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 用户好友项（`GET /api/service/users/{uid}/friends` items）。
 *
 * 仅返展示字段；备注 / 分组等扩展字段 v1 不暴露。
 */
@Serializable
data class FriendItem(
    @SerialName("user_id") val userId: Long,
    val username: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

@Serializable
data class ListUserFriendsResponse(
    @SerialName("user_id") val userId: Long,
    val friends: List<FriendItem>,
    val total: Int,
)

/** `POST /api/service/friendships` — 建双向好友 + 自动开私聊会话(MEMBER_INVITE_CODE §6)。 */
@Serializable
data class CreateFriendshipRequest(
    @SerialName("user1_id") val user1Id: Long,
    @SerialName("user2_id") val user2Id: Long,
)

@Serializable
data class CreateFriendshipResponse(
    val success: Boolean = false,
    @SerialName("user1_id") val user1Id: Long = 0,
    @SerialName("user2_id") val user2Id: Long = 0,
    @SerialName("channel_id") val channelId: Long = 0,
    val message: String? = null,
)

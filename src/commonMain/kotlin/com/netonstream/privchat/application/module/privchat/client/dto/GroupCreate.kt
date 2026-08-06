package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 服务端授权建群：调用方指定群主与初始成员，被加入者无需同意、也无需互为好友。
 * server 只被告知「哪些用户组成这个群」，不理解业务原因。
 */
@Serializable
data class CreateGroupRequest(
    @SerialName("owner_id") val ownerId: Long,
    val name: String,
    val description: String? = null,
    /** 不含群主；重复项与群主自身会被服务端忽略 */
    @SerialName("member_ids") val memberIds: List<Long> = emptyList(),
)

@Serializable
data class CreateGroupResponse(
    val success: Boolean = false,
    @SerialName("group_id") val groupId: Long = 0,
    @SerialName("owner_id") val ownerId: Long = 0,
    val name: String = "",
    @SerialName("member_ids") val memberIds: List<Long> = emptyList(),
)

@Serializable
data class AddGroupMemberRequest(
    @SerialName("user_id") val userId: Long,
)

@Serializable
data class AddGroupMemberResponse(
    val success: Boolean = false,
    @SerialName("group_id") val groupId: Long = 0,
    @SerialName("user_id") val userId: Long = 0,
    @SerialName("announcement_message_id") val announcementMessageId: Long? = null,
    val message: String = "",
)

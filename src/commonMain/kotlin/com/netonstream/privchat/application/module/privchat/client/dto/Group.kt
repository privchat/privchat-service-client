package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * 群组列表项（与 server `GET /api/service/groups` items 对齐）。
 *
 * `ownerId` server 侧来自 `privchat_groups.owner_id`，理论上 NOT NULL，但 server
 * 防御性返成 nullable u64，这里也跟着放宽。
 */
@Serializable
data class GroupAdminInfo(
    @SerialName("group_id") val groupId: Long,
    @SerialName("channel_id") val channelId: Long,
    val name: String? = null,
    val description: String? = null,
    @SerialName("owner_id") val ownerId: Long? = null,
    @SerialName("member_count") val memberCount: Int = 0,
    @SerialName("last_message_id") val lastMessageId: Long? = null,
    @SerialName("last_message_at") val lastMessageAt: Long? = null,
    @SerialName("message_count") val messageCount: Long = 0,
    @SerialName("created_at") val createdAt: Long = 0,
    @SerialName("updated_at") val updatedAt: Long = 0,
)

/** 群成员（`GET /api/service/groups/{id}/members` items）。 */
@Serializable
data class GroupMemberItem(
    @SerialName("user_id") val userId: Long,
    /** server 返 i16 数值；本端通过 [FlexibleMemberRoleSerializer] 映射为 owner/admin/member。 */
    @kotlinx.serialization.Serializable(with = FlexibleMemberRoleSerializer::class)
    val role: String? = null,
    @SerialName("joined_at") val joinedAt: Long? = null,
    /** 群内昵称（成员自定义，可能为空）。 */
    val nickname: String? = null,
    /** server JOIN privchat_users 带回的全局字段。 */
    val username: String? = null,
    @SerialName("display_name") val displayName: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null,
)

/**
 * 群详情：列表所有字段 + 群主全局展示字段。
 *
 * 不含成员数组 —— 万人群一次性全返会把这个接口拖死。成员走
 * [PrivchatServiceClient.listGroupMembers] 分页取，总数看 [memberCount]。
 */
@Serializable
data class GroupAdminDetail(
    @SerialName("group_id") val groupId: Long,
    @SerialName("channel_id") val channelId: Long,
    val name: String? = null,
    val description: String? = null,
    @SerialName("owner_id") val ownerId: Long? = null,
    @SerialName("owner_username") val ownerUsername: String? = null,
    @SerialName("owner_display_name") val ownerDisplayName: String? = null,
    @SerialName("owner_avatar_url") val ownerAvatarUrl: String? = null,
    @SerialName("member_count") val memberCount: Int = 0,
    @SerialName("last_message_id") val lastMessageId: Long? = null,
    @SerialName("last_message_at") val lastMessageAt: Long? = null,
    @SerialName("message_count") val messageCount: Long = 0,
    @SerialName("created_at") val createdAt: Long = 0,
    @SerialName("updated_at") val updatedAt: Long = 0,
)

@Serializable
data class ListGroupsResponse(
    val groups: List<GroupAdminInfo>,
    val total: Int,
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
)

@Serializable
data class DissolveGroupResponse(
    val success: Boolean,
    @SerialName("group_id") val groupId: Long,
    val message: String,
)

/** 用户加入的群（`GET /api/service/users/{uid}/groups`）。 */
@Serializable
data class UserGroupItem(
    @SerialName("group_id") val groupId: Long,
    @SerialName("channel_id") val channelId: Long,
    val name: String? = null,
    val description: String? = null,
    @SerialName("owner_id") val ownerId: Long? = null,
    @SerialName("member_count") val memberCount: Int = 0,
    /** 当前用户在该群里的角色（server 返 i16，本端映射为 owner/admin/member）。 */
    @kotlinx.serialization.Serializable(with = FlexibleMemberRoleSerializer::class)
    val role: String? = null,
    /** 当前用户在该群里的群昵称 */
    val nickname: String? = null,
    @SerialName("joined_at") val joinedAt: Long? = null,
)

/** 群成员分页（`GET /api/service/groups/{id}/members`）。 */
@Serializable
data class ListGroupMembersResponse(
    @SerialName("group_id") val groupId: Long,
    val members: List<GroupMemberItem> = emptyList(),
    /** 库里的活跃成员总数（server 独立 COUNT），不是本页条数。 */
    val total: Int = 0,
    val page: Int = 1,
    @SerialName("page_size") val pageSize: Int = 20,
)

@Serializable
data class ListUserGroupsResponse(
    @SerialName("user_id") val userId: Long,
    val groups: List<UserGroupItem>,
)

/** 群列表查询参数（仅 page / page_size，server 暂不支持 search）。 */
data class ListGroupsQuery(
    val page: Int = 1,
    val pageSize: Int = 20,
)

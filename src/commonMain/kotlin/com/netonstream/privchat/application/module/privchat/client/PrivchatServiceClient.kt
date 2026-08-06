package com.netonstream.privchat.application.module.privchat.client

import com.netonstream.privchat.application.module.privchat.client.dto.AddGroupMemberRequest
import com.netonstream.privchat.application.module.privchat.client.dto.AddGroupMemberResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateGroupRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateGroupResponse
import com.netonstream.privchat.application.module.privchat.client.dto.BumpSessionsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ConfirmQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateFriendshipRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateFriendshipResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateQrSceneRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateRoomRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateRoomResponse
import com.netonstream.privchat.application.module.privchat.client.dto.IssueRoomTicketRequest
import com.netonstream.privchat.application.module.privchat.client.dto.IssueRoomTicketResponse
import com.netonstream.privchat.application.module.privchat.client.dto.PushQrAuthorizedResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateUserRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateUserResponse
import com.netonstream.privchat.application.module.privchat.client.dto.DissolveGroupResponse
import com.netonstream.privchat.application.module.privchat.client.dto.GroupAdminDetail
import com.netonstream.privchat.application.module.privchat.client.dto.IntrospectAuthTokenResponse
import com.netonstream.privchat.application.module.privchat.client.dto.IssueAuthTokenRequest
import com.netonstream.privchat.application.module.privchat.client.dto.IssueImTokenRequest
import com.netonstream.privchat.application.module.privchat.client.dto.IssueImTokenResponse
import com.netonstream.privchat.application.module.privchat.client.dto.JwkSet
import com.netonstream.privchat.application.module.privchat.client.dto.ListGroupsQuery
import com.netonstream.privchat.application.module.privchat.client.dto.ListGroupsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListLoginLogsQuery
import com.netonstream.privchat.application.module.privchat.client.dto.ListLoginLogsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListMessagesQuery
import com.netonstream.privchat.application.module.privchat.client.dto.ListMessagesResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListUserFriendsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListUserGroupsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAuthTokenRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAuthTokenResponse
import com.netonstream.privchat.application.module.privchat.client.dto.UnifiedLoginResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListDevicesResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListUsersQuery
import com.netonstream.privchat.application.module.privchat.client.dto.ListUsersResponse
import com.netonstream.privchat.application.module.privchat.client.dto.QrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.QrSceneStatus
import com.netonstream.privchat.application.module.privchat.client.dto.RejectQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAllDevicesResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeDeviceResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ScanQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageRequest
import com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageResponse
import com.netonstream.privchat.application.module.privchat.client.dto.UpdateUserRequest
import com.netonstream.privchat.application.module.privchat.client.dto.UserInfo

/**
 * Neton Application 与 `privchat-server` Service API 之间的强类型客户端。
 *
 * 是 application 侧调用 server 的**唯一**入口（spec MODULE_PRIVCHAT_SPEC §3.1）；其他业务模块
 * （member / platform / payment）只能通过此接口操作 server，不允许自行构造 HTTP / 持有 service key。
 *
 * 完整方法集对应 spec SERVICE_CLIENT_CONTRACT §2 + QR_API §4。
 */
interface PrivchatServiceClient {

    // ──────────── 用户 ────────────

    /**
     * 创建用户。多键幂等（phone > email > username），命中已有用户返回 `created=false`。
     *
     * - 三者全空 → `BadRequest`（envelope.code=10101 INVALID_USER_IDENTITY）
     * - phone 不合法 → `BadRequest`（envelope.code=10104 INVALID_PHONE_FORMAT）
     * - 多键命中不同 uid → `Conflict`（envelope.code=10205 IDENTITY_CONFLICT）
     */
    suspend fun createUser(request: CreateUserRequest): CreateUserResponse

    /** 按 uid 查用户；不存在 → `NotFound`（20200）。 */
    suspend fun getUser(uid: Long): UserInfo

    /** 资金授权(#84):uid 是否为 channelId 的成员(DM 双方 / 群成员)。 */
    suspend fun isChannelMember(channelId: Long, uid: Long): Boolean

    /**
     * 按手机号查询用户。
     *
     * - 命中 → 返 [UserInfo]
     * - 未命中 → 返 `null`（404 不抛异常）
     * - phone 格式不合法 → `BadRequest`（envelope.code=10104）
     */
    suspend fun getUserByMobile(mobile: String): UserInfo?

    /** 部分更新用户字段；patch 中 null 字段保持不变。 */
    suspend fun updateUser(uid: Long, patch: UpdateUserRequest): UserInfo

    /** 软删除（status → Deleted）。 */
    suspend fun deleteUser(uid: Long)

    /** 封禁用户。`durationSecs=null` 表示永久。 */
    suspend fun suspendUser(uid: Long, reason: String, durationSecs: Long? = null)

    /** 解封。 */
    suspend fun unsuspendUser(uid: Long)

    /** 分页 + 搜索 + 过滤查询用户。 */
    suspend fun listUsers(query: ListUsersQuery = ListUsersQuery()): ListUsersResponse

    // ──────────── Token & 设备 ────────────

    /**
     * 按 uid 签发 IM Token + 登记设备。
     *
     * - uid 不存在 → `NotFound`（envelope.code=20200 USER_NOT_FOUND）
     * - 设备复用：相同 `deviceId` 返回 `deviceCreated=false` + `sessionVersion` 不变
     * - 签发**不**自动 bump session_version；如需让旧 token 失效请单独调 [bumpSessions]
     */
    suspend fun issueImToken(uid: Long, request: IssueImTokenRequest): IssueImTokenResponse

    /**
     * 推进用户全部活跃设备的 `session_version` += 1，**不**改 `session_state`。
     *
     * 改密 / 改手机号场景使用。区别于 [revokeAllDevices]（后者把 `session_state` 设为 Revoked）。
     */
    suspend fun bumpSessions(uid: Long, reason: String? = null): BumpSessionsResponse

    /** 强制踢出指定设备（`session_state=Revoked`）。需提供 uid 防误踢。 */
    suspend fun revokeDevice(deviceId: String, uid: Long, reason: String? = null): RevokeDeviceResponse

    /** 强制踢出该用户所有活跃设备。 */
    suspend fun revokeAllDevices(uid: Long, reason: String? = null): RevokeAllDevicesResponse

    /** 列出用户当前的所有设备记录。 */
    suspend fun listDevices(uid: Long): ListDevicesResponse

    // ──────────── Room channel ────────────

    /**
     * 创建一个 Room channel（server-side 纯内存通道，spec ROOM_CHANNEL §15.3）。
     *
     * 业务方持久化返回的 [CreateRoomResponse.channelId]；server 重启后 channel_id
     * 仍可用，下次有 session 订阅时隐式再激活。Room 本身**不**接受 `channel_type`，
     * 调用方只能传可选 [CreateRoomRequest.name]（仅 server 端 ops/debug 显示）。
     *
     * 典型场景：直播间 / game lobby / game table channel 都走这条入口。
     */
    /** 建双向好友(自动开私聊会话)。邀请码自动加好友(MEMBER_INVITE_CODE §6)。 */
    suspend fun createFriendship(request: CreateFriendshipRequest): CreateFriendshipResponse

    suspend fun createRoom(request: CreateRoomRequest = CreateRoomRequest()): CreateRoomResponse

    /**
     * 为一个 Room channel 签发 subscribe ticket（spec ROOM_CHANNEL §4.5）。
     *
     * 业务侧（如 module-game）在做完业务权限判定（房间存在 + 用户合法 + 座位分配等）
     * 之后调用本端点拿到一张 HS256 JWT ticket，再回给 client 用作
     * `SubscribeRequest.param`。
     *
     * application 进程**不**直接持有 `room_ticket_secret`——签发权聚集在 server。
     */
    suspend fun issueRoomTicket(request: IssueRoomTicketRequest): IssueRoomTicketResponse

    /**
     * 向一个 Room channel 广播消息（发布订阅；推给所有在线订阅者 + 写 room history）。
     * 用于 game table 等业务频道的实时事件下发（入座/开局/发牌/行动/结算）。
     * Room channel 是 server 纯内存频道，**不能**用 sendSystemMessage（那会写 privchat_messages）。
     */
    suspend fun broadcastRoom(
        channelId: Long,
        content: String,
        senderId: Long? = null,
    ): com.netonstream.privchat.application.module.privchat.client.dto.RoomBroadcastResponse

    // ──────────── 扫码登录 ────────────

    /** Web 申请创建场景（state=created）。返回 `qr_token` 由 App 扫码回传。 */
    suspend fun createQrScene(request: CreateQrSceneRequest): QrSceneResponse

    /** 兜底查询场景状态；正常路径走 server unauth RPC 推送。 */
    suspend fun getQrScene(sceneId: String): QrSceneStatus

    /** App 扫码 → state=scanned；颁发 `confirm_token`。 */
    suspend fun scanQrScene(
        sceneId: String,
        scannerUid: Long,
        scannerDeviceId: String,
        qrToken: String,
        scannerAvatar: String? = null,
        scannerDisplayName: String? = null,
    ): ScanQrSceneResponse

    /** App 确认 → state=authorized；消费 `confirm_token`。 */
    suspend fun confirmQrScene(
        sceneId: String,
        scannerUid: Long,
        scannerDeviceId: String,
        confirmToken: String,
    ): ConfirmQrSceneResponse

    /** App 拒绝 → state=rejected；消费 `confirm_token`。 */
    suspend fun rejectQrScene(
        sceneId: String,
        scannerUid: Long,
        confirmToken: String,
    ): RejectQrSceneResponse

    /**
     * 把 application 端组装好的登录返回对象推回给 Web 端 unauth 连接（spec QR_API §5）。
     *
     * `loginResponseJson` 应为 `MemberLoginResponse` 的 JSON 形态；server 透明转发，
     * 不解析 schema。返回 `delivered=false` 仅表示 Web 已断开（NoSubscriber），
     * 调用方应 **不** 回滚 confirm 业务状态，只记录日志即可。
     */
    suspend fun pushQrLoginAuthorized(
        sceneId: String,
        loginResponseJson: kotlinx.serialization.json.JsonElement,
    ): PushQrAuthorizedResponse

    // ──────────── Unified token (TOKEN_UNIFICATION_SPEC v1.3 Phase A) ────────────
    //
    // 这 5 个方法对应 server 端 `/api/service/auth/*` 端点（含 JWKS）。Phase A 在 application
    // 端**默认 dormant**：`security.unified_token.enabled=false` 时 dispatcher 不调它们；
    // 仅 unit test 与 staging 演练时启用。spec §10 phase A 是纯 additive。

    /**
     * 颁发 unified access + refresh token（spec §6.1）。
     *
     * - service-only：调用方必须是 trusted application（`X-Service-Key` 由 client 自带）
     * - server 在 `privchat_devices` 上 upsert，故 `request.userId` 必须是已存在用户
     * - server 把 refresh JWT 的 SHA-256 hex 写进 `privchat_refresh_tokens`，明文不落库
     * - phase A 不 rotate refresh
     */
    suspend fun issueAuthToken(request: IssueAuthTokenRequest): UnifiedLoginResponse

    /**
     * 用 refresh token 换新 access。Phase A 不 rotate refresh：`refresh_token` 字段原样返回。
     *
     * 失败场景（envelope error）：
     * - 签名失败 / typ 错 / device_id 与 claim 不一致 → `Unauthorized`
     * - jti 不存在 / 已 revoke / 已过期 → `Unauthorized`
     * - session_version bump 后 → `Unauthorized`
     */
    suspend fun refreshAuthToken(refreshToken: String, deviceId: String): UnifiedLoginResponse

    /**
     * 内省 unified token，返回**权威**状态（server 端不缓存，spec §6.1）。
     *
     * 业务结果通过 [IntrospectAuthTokenResponse.active] 表达，envelope code 永远 0；
     * 只有 service-key 错 / token 字段缺失才走 envelope error。
     */
    suspend fun introspectAuthToken(token: String): IntrospectAuthTokenResponse

    /**
     * 撤销 refresh token（spec §6.1）。`request` 中 `jti` 与 `refreshToken` **互斥必传一**：
     * 同传 / 都不传 → server 返 envelope error。
     */
    suspend fun revokeAuthToken(request: RevokeAuthTokenRequest): RevokeAuthTokenResponse

    /**
     * 拉取 server 当前签名公钥集（spec §6.1）。
     *
     * 公开端点：server **不**校验 service-key（公钥发布）。本 client 在底层 HTTP 通道
     * 上仍可能透传 service-key（兼容现状），server 不会拒绝。
     *
     * 失败场景：
     * - server 未配置 `[auth.rsa_jwt]` → server 返 503，本方法抛
     *   [com.netonstream.privchat.application.module.privchat.client.error.PrivchatServiceException]
     */
    suspend fun fetchJwks(): JwkSet

    // ──────────── 消息（仅特殊用途） ────────────

    /**
     * 系统消息发送（Bot 欢迎语、运营公告等）。普通用户消息走 IM 客户端，**不应**走此接口。
     */
    suspend fun sendSystemMessage(request: SendSystemMessageRequest): SendSystemMessageResponse

    /**
     * 通用消息注入（允许任意 sender_id + dedupKey）。RP-12：资金卡片服务端注入用——
     * sender=发送方真实 uid，messageType=red_packet/money_transfer，dedupKey 保证同订单幂等。
     */
    suspend fun sendMessage(
        request: com.netonstream.privchat.application.module.privchat.client.dto.SendMessageRequest,
    ): com.netonstream.privchat.application.module.privchat.client.dto.SendMessageResponse

    /**
     * 给指定用户发送系统消息（私聊形式）。server 自动 ensure 系统账号 ⇄ targetUid 的 direct channel
     * 并写消息。调用方无需提前解析或创建 channel。
     */
    suspend fun sendSystemMessageToUser(
        request: com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageToUserRequest,
    ): com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageToUserResponse

    /**
     * 列出所有 `user_type ∈ {1=System, 2=Bot}` 的用户作为系统消息可用 sender。
     * v1 直接返完整列表（不分页）；v2 数量大时再加 page。
     */
    suspend fun listSystemSenders(): com.netonstream.privchat.application.module.privchat.client.dto.ListSystemSendersResponse

    // ──────────── Admin: 群组 ────────────

    /** 群组列表（仅 page / page_size，server 暂不支持 search）。 */
    /** 读取 IM 平台级隐私配置(PROFILE_VISIBILITY P2)。 */
    suspend fun getPrivacyConfig(): com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig

    /** 更新 IM 平台级隐私配置。 */
    suspend fun updatePrivacyConfig(
        config: com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig,
    ): com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig

    suspend fun listGroups(query: ListGroupsQuery = ListGroupsQuery()): ListGroupsResponse

    /** 群详情（含群成员）；不存在 → `NotFound`。 */
    suspend fun getGroup(groupId: Long): GroupAdminDetail

    /** 解散群（删除成员关系 + 频道软删）；不存在 → `NotFound`。 */
    suspend fun dissolveGroup(groupId: Long): DissolveGroupResponse

    /**
     * 服务端授权建群。被加入者无需同意、无需互为好友 —— 授权来自 service key
     * 而非发起人的 IM 会话。
     */
    suspend fun createGroup(request: CreateGroupRequest): CreateGroupResponse

    /** 加群成员（服务端会附带一条入群公告）。已是活跃成员会被拒绝。 */
    suspend fun addGroupMember(groupId: Long, userId: Long): AddGroupMemberResponse

    /** 移除群成员（soft delete：left_at = now）。Owner 不可移除。 */
    suspend fun removeGroupMember(groupId: Long, userId: Long)

    /**
     * 设置群成员角色。`role` 仅支持 `"admin"` / `"member"`；Owner 不可通过本接口设置。
     */
    suspend fun setGroupMemberRole(groupId: Long, userId: Long, role: String)

    // ──────────── Admin: 用户聚合查询 ────────────

    /** 用户的好友列表（用户详情页 tab）。 */
    suspend fun getUserFriends(uid: Long): ListUserFriendsResponse

    /** 用户加入的群列表（用户详情页 tab）。 */
    suspend fun getUserGroups(uid: Long): ListUserGroupsResponse

    // ──────────── Admin: 登录日志 ────────────

    /**
     * 登录日志列表查询。`query.userId` 不传 = 全服扫描；admin 控制器层应在 v1 强制要求传
     * userId（避免大表全扫）。
     */
    suspend fun listLoginLogs(query: ListLoginLogsQuery): ListLoginLogsResponse

    // ──────────── Admin: 消息查询 ────────────

    /**
     * 消息列表查询。`channelId` / `userId` 至少需要一个有效过滤；controller 应在两个都
     * null 时直接拒绝。
     */
    suspend fun listMessages(query: ListMessagesQuery): ListMessagesResponse

    // ──────────── Admin: 私聊频道反查 ────────────

    /**
     * 按两个用户 ID 查找他们之间的私聊频道 ID。
     *
     * - 命中 → 返 channel_id
     * - 不存在 → 返 null（server 404 不抛异常）
     * - uidA == uidB → server 返 BadRequest，本方法抛 [PrivchatServiceException.BadRequest]
     */
    suspend fun lookupDirectChannel(uidA: Long, uidB: Long): Long?
}

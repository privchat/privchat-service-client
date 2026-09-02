package com.netonstream.privchat.application.module.privchat.client

import com.netonstream.privchat.application.module.privchat.client.dto.AddGroupMemberRequest
import com.netonstream.privchat.application.module.privchat.client.dto.AddGroupMemberResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateGroupRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateGroupResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ChannelMemberCheck
import com.netonstream.privchat.application.module.privchat.client.dto.BumpSessionsRequest
import com.netonstream.privchat.application.module.privchat.client.dto.BumpSessionsResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ConfirmQrSceneRequest
import com.netonstream.privchat.application.module.privchat.client.dto.ConfirmQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateFriendshipRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateFriendshipResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateQrSceneRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateRoomRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateRoomResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RoomBroadcastRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RoomBroadcastResponse
import com.netonstream.privchat.application.module.privchat.client.dto.IssueRoomTicketRequest
import com.netonstream.privchat.application.module.privchat.client.dto.IssueRoomTicketResponse
import com.netonstream.privchat.application.module.privchat.client.dto.CreateUserRequest
import com.netonstream.privchat.application.module.privchat.client.dto.CreateUserResponse
import com.netonstream.privchat.application.module.privchat.client.dto.DissolveGroupResponse
import com.netonstream.privchat.application.module.privchat.client.dto.GroupAdminDetail
import com.netonstream.privchat.application.module.privchat.client.dto.IntrospectAuthTokenRequest
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
import com.netonstream.privchat.application.module.privchat.client.dto.RefreshAuthTokenRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAuthTokenRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAuthTokenResponse
import com.netonstream.privchat.application.module.privchat.client.dto.UnifiedLoginResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListDevicesResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ListUsersQuery
import com.netonstream.privchat.application.module.privchat.client.dto.ListUsersResponse
import com.netonstream.privchat.application.module.privchat.client.dto.PushQrAuthorizedRequest
import com.netonstream.privchat.application.module.privchat.client.dto.PushQrAuthorizedResponse
import com.netonstream.privchat.application.module.privchat.client.dto.QrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.QrSceneStatus
import com.netonstream.privchat.application.module.privchat.client.dto.RejectQrSceneRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RejectQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAllDevicesRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeAllDevicesResponse
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeDeviceRequest
import com.netonstream.privchat.application.module.privchat.client.dto.RevokeDeviceResponse
import com.netonstream.privchat.application.module.privchat.client.dto.ScanQrSceneRequest
import com.netonstream.privchat.application.module.privchat.client.dto.ScanQrSceneResponse
import com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageRequest
import com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageResponse
import com.netonstream.privchat.application.module.privchat.client.dto.SuspendUserRequest
import com.netonstream.privchat.application.module.privchat.client.dto.UpdateUserRequest
import com.netonstream.privchat.application.module.privchat.client.dto.UserInfo
import com.netonstream.privchat.application.module.privchat.client.error.PrivchatServiceException
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import neton.core.http.HttpHeaders
import neton.http.client.HttpClient
import neton.http.client.HttpClientBody
import neton.http.client.HttpClientError
import neton.http.client.HttpClientException
import neton.http.client.HttpClientMethod
import neton.http.client.HttpClientRequest
import neton.http.client.HttpClientTimeouts
import kotlinx.serialization.json.JsonElement
import kotlin.time.TimeSource
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * 经 Neton `HttpClient` 的实现。线程安全，可作单例长期持有。
 *
 * - `baseUrl`：server admin/service API 根（如 `http://localhost:9090`），不含尾斜杠
 * - `serviceMasterKey`：`X-Service-Key` 值
 * - `businessSystemId`：可选，注入 `X-Business-System-Id` 头
 * - `httpClient`：**借用**应用创建并绑定的出站客户端。本类不创建引擎、不关闭它——
 *   同一个客户端还服务着 storage / ai / transfer 回调，谁创建谁关闭。
 *
 * `X-Request-Id` 由 client 自动生成（UUID v4），便于链路追踪。
 * 超时按请求显式给出（connect 3s / request 5s / socket 5s），不依赖借来的客户端
 * 恰好怎么配置。
 */
@OptIn(ExperimentalUuidApi::class)
class PrivchatServiceClientImpl(
    private val baseUrl: String,
    private val serviceMasterKey: String,
    private val businessSystemId: String? = null,
    private val httpClient: HttpClient,
) : PrivchatServiceClient, AutoCloseable {

    // ──────────── 用户 ────────────

    override suspend fun createFriendship(request: CreateFriendshipRequest): CreateFriendshipResponse =
        post("/api/service/friendships", request, CreateFriendshipResponse.serializer())

    override suspend fun createUser(request: CreateUserRequest): CreateUserResponse =
        post("/api/service/users", request, CreateUserResponse.serializer())

    override suspend fun getUser(uid: Long): UserInfo =
        getDecoded("/api/service/users/$uid", UserInfo.serializer())

    override suspend fun isChannelMember(channelId: Long, uid: Long): Boolean =
        getDecoded(
            "/api/service/channels/$channelId/members/$uid",
            ChannelMemberCheck.serializer(),
        ).isMember

    override suspend fun getUserByMobile(mobile: String): UserInfo? {
        val path = "/api/service/users/by-mobile/$mobile"
        val response = exchange(HttpClientMethod.Get, path, null)
        if (response.status == 404) return null
        return decodeEnvelope(response, UserInfo.serializer())
    }

    override suspend fun updateUser(uid: Long, patch: UpdateUserRequest): UserInfo =
        put("/api/service/users/$uid", patch, UserInfo.serializer())

    override suspend fun deleteUser(uid: Long) {
        deleteUnit("/api/service/users/$uid")
    }

    override suspend fun suspendUser(uid: Long, reason: String, durationSecs: Long?) {
        postUnit("/api/service/users/$uid/suspend", SuspendUserRequest(reason, durationSecs))
    }

    override suspend fun unsuspendUser(uid: Long) {
        postUnit("/api/service/users/$uid/unsuspend", emptyJsonObject())
    }

    override suspend fun listUsers(query: ListUsersQuery): ListUsersResponse {
        val qs = buildString {
            append("?page=").append(query.page)
            append("&page_size=").append(query.pageSize)
            query.search?.let { append("&search=").append(it) }
            query.status?.let { append("&status=").append(it) }
            query.userType?.let { append("&user_type=").append(it) }
            query.businessSystemId?.let { append("&business_system_id=").append(it) }
        }
        return getDecoded("/api/service/users$qs", ListUsersResponse.serializer())
    }

    // ──────────── Token & 设备 ────────────

    override suspend fun issueImToken(
        uid: Long,
        request: IssueImTokenRequest,
    ): IssueImTokenResponse =
        post("/api/service/users/$uid/tokens", request, IssueImTokenResponse.serializer())

    override suspend fun bumpSessions(uid: Long, reason: String?): BumpSessionsResponse =
        post(
            "/api/service/users/$uid/sessions/bump",
            BumpSessionsRequest(reason = reason),
            BumpSessionsResponse.serializer(),
        )

    override suspend fun revokeDevice(
        deviceId: String,
        uid: Long,
        reason: String?,
    ): RevokeDeviceResponse =
        post(
            "/api/service/devices/$deviceId/revoke",
            RevokeDeviceRequest(uid, reason),
            RevokeDeviceResponse.serializer(),
        )

    override suspend fun revokeAllDevices(uid: Long, reason: String?): RevokeAllDevicesResponse =
        post(
            "/api/service/users/$uid/revoke-all-devices",
            RevokeAllDevicesRequest(reason),
            RevokeAllDevicesResponse.serializer(),
        )

    override suspend fun listDevices(uid: Long): ListDevicesResponse =
        getDecoded("/api/service/users/$uid/devices", ListDevicesResponse.serializer())

    // ──────────── Room channel ────────────

    override suspend fun createRoom(request: CreateRoomRequest): CreateRoomResponse =
        post("/api/service/room", request, CreateRoomResponse.serializer())

    override suspend fun issueRoomTicket(request: IssueRoomTicketRequest): IssueRoomTicketResponse =
        post("/api/service/room-tickets/issue", request, IssueRoomTicketResponse.serializer())

    override suspend fun broadcastRoom(
        channelId: Long,
        content: String,
        senderId: Long?,
    ): RoomBroadcastResponse =
        post(
            "/api/service/room/$channelId/broadcast",
            RoomBroadcastRequest(content = content, senderId = senderId),
            RoomBroadcastResponse.serializer(),
        )

    // ──────────── 扫码登录 ────────────

    override suspend fun createQrScene(request: CreateQrSceneRequest): QrSceneResponse =
        post("/api/service/qr-login/scenes", request, QrSceneResponse.serializer())

    override suspend fun getQrScene(sceneId: String): QrSceneStatus =
        getDecoded("/api/service/qr-login/scenes/$sceneId", QrSceneStatus.serializer())

    override suspend fun scanQrScene(
        sceneId: String,
        scannerUid: Long,
        scannerDeviceId: String,
        qrToken: String,
        scannerAvatar: String?,
        scannerDisplayName: String?,
    ): ScanQrSceneResponse =
        post(
            "/api/service/qr-login/scenes/$sceneId/scan",
            ScanQrSceneRequest(
                scannerUid = scannerUid,
                scannerDeviceId = scannerDeviceId,
                qrToken = qrToken,
                scannerAvatar = scannerAvatar,
                scannerDisplayName = scannerDisplayName,
            ),
            ScanQrSceneResponse.serializer(),
        )

    override suspend fun confirmQrScene(
        sceneId: String,
        scannerUid: Long,
        scannerDeviceId: String,
        confirmToken: String,
    ): ConfirmQrSceneResponse =
        post(
            "/api/service/qr-login/scenes/$sceneId/confirm",
            ConfirmQrSceneRequest(scannerUid, scannerDeviceId, confirmToken),
            ConfirmQrSceneResponse.serializer(),
        )

    override suspend fun rejectQrScene(
        sceneId: String,
        scannerUid: Long,
        confirmToken: String,
    ): RejectQrSceneResponse =
        post(
            "/api/service/qr-login/scenes/$sceneId/reject",
            RejectQrSceneRequest(scannerUid, confirmToken),
            RejectQrSceneResponse.serializer(),
        )

    override suspend fun pushQrLoginAuthorized(
        sceneId: String,
        loginResponseJson: JsonElement,
    ): PushQrAuthorizedResponse =
        post(
            "/api/service/qr-login/scenes/$sceneId/push-authorized",
            PushQrAuthorizedRequest(loginResponseJson),
            PushQrAuthorizedResponse.serializer(),
        )

    // ──────────── Unified token (TOKEN_UNIFICATION_SPEC v1.3 Phase A) ────────────

    override suspend fun issueAuthToken(request: IssueAuthTokenRequest): UnifiedLoginResponse =
        post(
            "/api/service/auth/issue",
            request,
            UnifiedLoginResponse.serializer(),
        )

    override suspend fun refreshAuthToken(
        refreshToken: String,
        deviceId: String,
    ): UnifiedLoginResponse =
        post(
            "/api/service/auth/refresh",
            RefreshAuthTokenRequest(refreshToken = refreshToken, deviceId = deviceId),
            UnifiedLoginResponse.serializer(),
        )

    override suspend fun introspectAuthToken(token: String): IntrospectAuthTokenResponse =
        post(
            "/api/service/auth/introspect",
            IntrospectAuthTokenRequest(token = token),
            IntrospectAuthTokenResponse.serializer(),
        )

    override suspend fun revokeAuthToken(
        request: RevokeAuthTokenRequest,
    ): RevokeAuthTokenResponse =
        post(
            "/api/service/auth/revoke",
            request,
            RevokeAuthTokenResponse.serializer(),
        )

    override suspend fun fetchJwks(): JwkSet {
        // JWKS 是公开端点（spec §6.1）：server 不要求 X-Service-Key，但 client 沿用通用
        // HTTP 通道顺手带也无害（server 忽略）。响应 **没有** envelope 包装，
        // 直接是 `{"keys":[...]}`，因此走单独的 `getRaw` 路径，不调 `decodeEnvelope`。
        val path = "/api/service/auth/jwks"
        val response = exchange(HttpClientMethod.Get, path, null)
        val raw = response.body
        if (response.status !in 200..299) {
            // server 未配置 [auth.rsa_jwt] 时返 503 + 普通 JSON {"error","message"}
            throw PrivchatServiceException.ServerError(
                code = response.status,
                message = "fetchJwks failed status=${response.status} body=$raw",
            )
        }
        return try {
            JSON.decodeFromString(JwkSet.serializer(), raw)
        } catch (e: Exception) {
            throw PrivchatServiceException.Decode(
                message = "fetchJwks decode failed: $raw",
                cause = e,
            )
        }
    }

    // ──────────── 消息 ────────────

    override suspend fun sendSystemMessage(
        request: SendSystemMessageRequest,
    ): SendSystemMessageResponse =
        post(
            "/api/service/messages/send-system",
            request,
            SendSystemMessageResponse.serializer(),
        )

    override suspend fun sendMessage(
        request: com.netonstream.privchat.application.module.privchat.client.dto.SendMessageRequest,
    ): com.netonstream.privchat.application.module.privchat.client.dto.SendMessageResponse =
        post(
            "/api/service/messages/send",
            request,
            com.netonstream.privchat.application.module.privchat.client.dto.SendMessageResponse.serializer(),
        )

    override suspend fun sendSystemMessageToUser(
        request: com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageToUserRequest,
    ): com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageToUserResponse =
        post(
            "/api/service/system-messages/send-to-user",
            request,
            com.netonstream.privchat.application.module.privchat.client.dto.SendSystemMessageToUserResponse.serializer(),
        )

    override suspend fun listSystemSenders(): com.netonstream.privchat.application.module.privchat.client.dto.ListSystemSendersResponse =
        getDecoded(
            "/api/service/system-messages/senders",
            com.netonstream.privchat.application.module.privchat.client.dto.ListSystemSendersResponse.serializer(),
        )

    // ──────────── Admin: 群组 ────────────

    override suspend fun getPrivacyConfig():
        com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig =
        getDecoded(
            "/api/service/privacy-config",
            com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig.serializer(),
        )

    override suspend fun updatePrivacyConfig(
        config: com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig,
    ): com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig {
        val path = "/api/service/privacy-config"
        val response = exchange(
            HttpClientMethod.Put,
            path,
            encode(
                kotlinx.serialization.json.JsonObject(
                    mapOf(
                        "username_searchable" to
                            kotlinx.serialization.json.JsonPrimitive(config.usernameSearchable),
                    ),
                ),
            ),
        )
        return decodeEnvelope(
            response,
            com.netonstream.privchat.application.module.privchat.client.dto.PrivacyConfig.serializer(),
        )
    }

    override suspend fun listGroups(query: ListGroupsQuery): ListGroupsResponse {
        val qs = "?page=${query.page}&page_size=${query.pageSize}"
        return getDecoded("/api/service/groups$qs", ListGroupsResponse.serializer())
    }

    override suspend fun getGroup(groupId: Long): GroupAdminDetail =
        getDecoded("/api/service/groups/$groupId", GroupAdminDetail.serializer())

    override suspend fun dissolveGroup(groupId: Long): DissolveGroupResponse {
        val response = exchange(HttpClientMethod.Delete, "/api/service/groups/$groupId", null)
        return decodeEnvelope(response, DissolveGroupResponse.serializer())
    }

    override suspend fun createGroup(request: CreateGroupRequest): CreateGroupResponse =
        post("/api/service/groups", request, CreateGroupResponse.serializer())

    override suspend fun addGroupMember(groupId: Long, userId: Long): AddGroupMemberResponse =
        post(
            "/api/service/groups/$groupId/members",
            AddGroupMemberRequest(userId),
            AddGroupMemberResponse.serializer(),
        )

    override suspend fun removeGroupMember(groupId: Long, userId: Long) {
        deleteUnit("/api/service/groups/$groupId/members/$userId")
    }

    override suspend fun setGroupMemberRole(groupId: Long, userId: Long, role: String) {
        val path = "/api/service/groups/$groupId/members/$userId/role"
        val response = exchange(
            HttpClientMethod.Put,
            path,
            encode(kotlinx.serialization.json.JsonObject(mapOf("role" to kotlinx.serialization.json.JsonPrimitive(role)))),
        )
        verifyOk(response)
    }

    // ──────────── Admin: 用户聚合查询 ────────────

    override suspend fun getUserFriends(uid: Long): ListUserFriendsResponse =
        getDecoded("/api/service/users/$uid/friends", ListUserFriendsResponse.serializer())

    override suspend fun getUserGroups(uid: Long): ListUserGroupsResponse =
        getDecoded("/api/service/users/$uid/groups", ListUserGroupsResponse.serializer())

    // ──────────── Admin: 登录日志 ────────────

    override suspend fun listLoginLogs(query: ListLoginLogsQuery): ListLoginLogsResponse {
        val qs = buildString {
            append("?page=").append(query.page)
            append("&page_size=").append(query.pageSize)
            query.userId?.let { append("&user_id=").append(it) }
            query.ipAddress?.let { append("&ip_address=").append(it) }
            query.status?.let { append("&status=").append(it) }
            query.startTime?.let { append("&start_time=").append(it) }
            query.endTime?.let { append("&end_time=").append(it) }
        }
        return getDecoded("/api/service/login-logs$qs", ListLoginLogsResponse.serializer())
    }

    // ──────────── Admin: 消息查询 ────────────

    override suspend fun listMessages(query: ListMessagesQuery): ListMessagesResponse {
        val qs = buildString {
            append("?page=").append(query.page)
            append("&page_size=").append(query.pageSize)
            query.channelId?.let { append("&channel_id=").append(it) }
            query.userId?.let { append("&user_id=").append(it) }
            query.startTime?.let { append("&start_time=").append(it) }
            query.endTime?.let { append("&end_time=").append(it) }
        }
        return getDecoded("/api/service/messages$qs", ListMessagesResponse.serializer())
    }

    override suspend fun lookupDirectChannel(uidA: Long, uidB: Long): Long? {
        val path = "/api/service/direct-channels/lookup?user_a=$uidA&user_b=$uidB"
        val response = exchange(HttpClientMethod.Get, path, null)
        if (response.status == 404) return null
        val data = decodeEnvelope(
            response,
            kotlinx.serialization.json.JsonObject.serializer(),
        )
        val raw = data["channel_id"] ?: return null
        return (raw as? kotlinx.serialization.json.JsonPrimitive)?.content?.toLongOrNull()
    }

    override fun close() {
        // 借来的客户端不在这里关：应用创建、应用关闭。保留 AutoCloseable 只为兼容调用方。
    }

    // ── 内部工具 ──

    private suspend inline fun <reified Req, T> post(
        path: String,
        body: Req,
        responseSerializer: kotlinx.serialization.KSerializer<T>,
    ): T {
        val response = exchange(HttpClientMethod.Post, path, encode(body))
        return decodeEnvelope(response, responseSerializer)
    }

    private suspend inline fun <reified Req, T> put(
        path: String,
        body: Req,
        responseSerializer: kotlinx.serialization.KSerializer<T>,
    ): T {
        val response = exchange(HttpClientMethod.Put, path, encode(body))
        return decodeEnvelope(response, responseSerializer)
    }

    private suspend fun <T> getDecoded(
        path: String,
        responseSerializer: kotlinx.serialization.KSerializer<T>,
    ): T {
        val response = exchange(HttpClientMethod.Get, path, null)
        return decodeEnvelope(response, responseSerializer)
    }

    private suspend inline fun <reified Req> postUnit(path: String, body: Req) {
        val response = exchange(HttpClientMethod.Post, path, encode(body))
        verifyOk(response)
    }

    private suspend fun deleteUnit(path: String) {
        val response = exchange(HttpClientMethod.Delete, path, null)
        verifyOk(response)
    }

    /** 一次往返的结果。`path` 只用于诊断信息。 */
    private class Exchange(val status: Int, val body: String, val path: String)

    private inline fun <reified B> encode(body: B): String = JSON.encodeToString(serializer<B>(), body)

    private suspend fun exchange(method: HttpClientMethod, path: String, jsonBody: String?): Exchange {
        val name = method.name.uppercase()
        val started = TimeSource.Monotonic.markNow()
        val request = HttpClientRequest(
            method = method,
            url = baseUrl + path,
            headers = commonHeaders(),
            body = jsonBody?.let { HttpClientBody.Json(it) },
            timeout = REQUEST_TIMEOUTS,
        )
        val resp = try {
            httpClient.request(request)
        } catch (e: HttpClientException) {
            when (e.error) {
                is HttpClientError.Timeout -> {
                    println("[privchat-svc] $name $path TIMEOUT cause=${e.error.message}")
                    throw PrivchatServiceException.Timeout("request timed out: $name $path", e)
                }
                else -> {
                    println("[privchat-svc] $name $path NETWORK cause=${e.error.message}")
                    throw PrivchatServiceException.Network("network error: $name $path: ${e.error.message}", e)
                }
            }
        }
        val elapsed = started.elapsedNow().inWholeMilliseconds
        println("[privchat-svc] $name $path → ${resp.statusCode} (${elapsed}ms)")
        return Exchange(resp.statusCode, resp.body, path)
    }

    private suspend fun <T> decodeEnvelope(
        response: Exchange,
        responseSerializer: kotlinx.serialization.KSerializer<T>,
    ): T {
        val raw = response.body
        val envelope = try {
            JSON.decodeFromString(ApiEnvelope.serializer(JsonElement.serializer()), raw)
        } catch (e: Exception) {
            throw PrivchatServiceException.Decode(
                "unable to decode envelope from ${response.status}: ${raw.take(256)}",
                e,
            )
        }

        if (envelope.isOk) {
            val data = envelope.data
                ?: throw PrivchatServiceException.Decode(
                    "envelope.code=0 but data is null (path=${response.path})",
                )
            return try {
                JSON.decodeFromJsonElement(responseSerializer, data)
            } catch (e: Exception) {
                throw PrivchatServiceException.Decode(
                    "unable to decode envelope.data into expected type: $data",
                    e,
                )
            }
        }

        throw mapErrorEnvelope(response.status, envelope.code, envelope.message)
    }

    /** 仅校验 envelope.code==0；data 部分丢弃。用于 Unit 返回的接口。 */
    private suspend fun verifyOk(response: Exchange) {
        val raw = response.body
        val envelope = try {
            JSON.decodeFromString(ApiEnvelope.serializer(JsonElement.serializer()), raw)
        } catch (e: Exception) {
            throw PrivchatServiceException.Decode(
                "unable to decode envelope from ${response.status}: ${raw.take(256)}",
                e,
            )
        }
        if (!envelope.isOk) throw mapErrorEnvelope(response.status, envelope.code, envelope.message)
    }

    private fun mapErrorEnvelope(
        status: Int,
        code: Int,
        message: String,
    ): PrivchatServiceException = when (status) {
        400 -> PrivchatServiceException.BadRequest(code, message)
        401 -> PrivchatServiceException.Unauthorized(code, message)
        403 -> PrivchatServiceException.Forbidden(code, message)
        404 -> PrivchatServiceException.NotFound(code, message)
        409 -> PrivchatServiceException.Conflict(code, message)
        429 -> PrivchatServiceException.TooManyRequests(code, message)
        504 -> PrivchatServiceException.Timeout("server reported timeout: $message")
        in 500..599 -> PrivchatServiceException.ServerError(code, message)
        else -> PrivchatServiceException.ServerError(
            code,
            "unexpected HTTP $status: $message",
        )
    }

    private fun commonHeaders(): HttpHeaders {
        var h = HttpHeaders.of(
            "X-Service-Key" to serviceMasterKey,
            "X-Request-Id" to Uuid.random().toString(),
        )
        businessSystemId?.let { h = h.add("X-Business-System-Id", it) }
        return h
    }

    /** 用作 `postUnit` 的空 body 占位。 */
    private fun emptyJsonObject(): kotlinx.serialization.json.JsonObject =
        kotlinx.serialization.json.JsonObject(emptyMap())

    companion object {
        /** 与旧 Ktor 配置一致：connect 3s / request 5s / socket 5s。 */
        private val REQUEST_TIMEOUTS = HttpClientTimeouts(connectMillis = 3_000, requestMillis = 5_000, socketMillis = 5_000)

        private val JSON = Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
            explicitNulls = false
            coerceInputValues = true
        }
    }
}

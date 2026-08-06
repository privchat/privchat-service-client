package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Web 扫码登录场景的 client DTO（spec QR_API §4 + SERVICE_CLIENT_CONTRACT §3.3）。
 *
 * application 内部 5 个方法对应 5 个 server 路径：`/api/service/qr-login/scenes[/{id}[/scan|/confirm|/reject]]`。
 */

@Serializable
data class CreateQrSceneRequest(
    val purpose: String = "login",
    @SerialName("device_id") val deviceId: String,
    @SerialName("device_info") val deviceInfo: WebDeviceInfoInput,
    /** created → scanned 阶段过期秒数；缺省 90s。 */
    val ttl: Long? = null,
)

@Serializable
data class WebDeviceInfoInput(
    @SerialName("app_id") val appId: String = "web",
    @SerialName("device_name") val deviceName: String? = null,
    @SerialName("user_agent") val userAgent: String? = null,
    @SerialName("ip_address") val ipAddress: String? = null,
)

@Serializable
data class QrSceneResponse(
    @SerialName("scene_id") val sceneId: String,
    @SerialName("qr_token") val qrToken: String,
    @SerialName("expires_at") val expiresAt: Long,
    @SerialName("rpc_topic") val rpcTopic: String,
)

@Serializable
data class QrSceneStatus(
    @SerialName("scene_id") val sceneId: String,
    /** created / scanned / authorized / rejected / expired */
    val state: String,
    @SerialName("expires_at") val expiresAt: Long,
    @SerialName("scanned_at") val scannedAt: Long? = null,
    @SerialName("scanner_uid") val scannerUid: Long? = null,
    @SerialName("scanner_avatar") val scannerAvatar: String? = null,
    @SerialName("scanner_display_name") val scannerDisplayName: String? = null,
)

@Serializable
data class ScanQrSceneRequest(
    @SerialName("scanner_uid") val scannerUid: Long,
    @SerialName("scanner_device_id") val scannerDeviceId: String,
    @SerialName("qr_token") val qrToken: String,
    @SerialName("scanner_avatar") val scannerAvatar: String? = null,
    @SerialName("scanner_display_name") val scannerDisplayName: String? = null,
)

@Serializable
data class ScanQrSceneResponse(
    @SerialName("scene_id") val sceneId: String,
    val state: String,
    @SerialName("confirm_token") val confirmToken: String,
    val purpose: String,
    @SerialName("web_device_info") val webDeviceInfo: WebDeviceSnapshot,
)

@Serializable
data class WebDeviceSnapshot(
    @SerialName("device_name") val deviceName: String? = null,
    @SerialName("user_agent") val userAgent: String? = null,
    @SerialName("ip_address") val ipAddress: String? = null,
)

@Serializable
data class ConfirmQrSceneRequest(
    @SerialName("scanner_uid") val scannerUid: Long,
    @SerialName("scanner_device_id") val scannerDeviceId: String,
    @SerialName("confirm_token") val confirmToken: String,
)

@Serializable
data class ConfirmQrSceneResponse(
    @SerialName("scene_id") val sceneId: String,
    val state: String,
    val uid: Long,
    /** 创建 scene 时由 Web 提供的 device_id；application 用它给 Web 签发登录 token。 */
    @SerialName("web_device_id") val webDeviceId: String,
    /** Web 设备快照；application 用它构造 issueImToken 的 DeviceInfo。 */
    @SerialName("web_device_info") val webDeviceInfo: WebDeviceSnapshot,
)

@Serializable
data class PushQrAuthorizedRequest(
    /** 透传 application 端 LoginResponse 的 JSON；server 不解析 schema。 */
    val data: kotlinx.serialization.json.JsonElement,
)

@Serializable
data class PushQrAuthorizedResponse(
    @SerialName("scene_id") val sceneId: String,
    /** false = publisher 找不到 binding（Web 已断开）；调用方应 log only，不回滚 confirm。 */
    val delivered: Boolean,
)

@Serializable
data class RejectQrSceneRequest(
    @SerialName("scanner_uid") val scannerUid: Long,
    @SerialName("confirm_token") val confirmToken: String,
)

@Serializable
data class RejectQrSceneResponse(
    @SerialName("scene_id") val sceneId: String,
    val state: String,
)

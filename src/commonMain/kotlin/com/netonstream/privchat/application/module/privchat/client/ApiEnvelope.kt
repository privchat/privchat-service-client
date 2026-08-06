package com.netonstream.privchat.application.module.privchat.client

import kotlinx.serialization.Serializable

/**
 * HTTP API 统一响应信封。与 privchat-server `src/http/envelope.rs` 对齐。
 *
 * 详见 `privchat-docs/spec/02-server/SERVICE_RESPONSE_ENVELOPE_SPEC.md`：
 * - 成功 `{ code: 0, message: "OK", data: { ... } }`
 * - 错误 `{ code: <protocol::ErrorCode 数字>, message: "<TAG>: <detail>", data: null }`
 *
 * `code` 来源唯一是 `privchat-protocol::ErrorCode`，与 RPC / TCP 共用一套数字码。
 * 客户端**优先**根据 [code] 判定，HTTP status 是辅助。
 */
@Serializable
data class ApiEnvelope<T>(
    val code: Int,
    val message: String,
    val data: T? = null,
) {
    val isOk: Boolean get() = code == 0
}

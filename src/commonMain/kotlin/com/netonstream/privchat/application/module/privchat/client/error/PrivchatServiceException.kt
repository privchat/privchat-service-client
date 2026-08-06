package com.netonstream.privchat.application.module.privchat.client.error

/**
 * `PrivchatServiceClient` 调用 server 失败时抛出的异常。
 *
 * `code` 字段是权威业务码（来自 `privchat-protocol::ErrorCode` 数字值，与 server envelope 一致）；
 * 子类按 HTTP status 粗分类，业务侧应优先按 [code] 精细分支。
 *
 * `Network` / `Timeout` / `Decode` 子类的 [code] 为负值（[LOCAL_CLIENT_ERROR_CODE]），
 * 表示**本地**客户端错误，不会与 protocol 数字码冲突；用 [isLocalClientError] 判断。
 */
sealed class PrivchatServiceException(
    val code: Int,
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause) {

    class BadRequest(code: Int, message: String) : PrivchatServiceException(code, message)
    class Unauthorized(code: Int, message: String) : PrivchatServiceException(code, message)
    class Forbidden(code: Int, message: String) : PrivchatServiceException(code, message)
    /** HTTP 404，业务键未找到。`getUserByMobile` 不抛此异常（404 → null）。 */
    class NotFound(code: Int, message: String) : PrivchatServiceException(code, message)
    /** HTTP 409，含 IDENTITY_CONFLICT (10205) / DuplicateEntry。 */
    class Conflict(code: Int, message: String) : PrivchatServiceException(code, message)
    class TooManyRequests(code: Int, message: String) : PrivchatServiceException(code, message)
    class ServerError(code: Int, message: String) : PrivchatServiceException(code, message)

    class Timeout(message: String, cause: Throwable? = null) :
        PrivchatServiceException(LOCAL_CLIENT_ERROR_CODE, message, cause)
    class Network(message: String, cause: Throwable? = null) :
        PrivchatServiceException(LOCAL_CLIENT_ERROR_CODE, message, cause)
    class Decode(message: String, cause: Throwable? = null) :
        PrivchatServiceException(LOCAL_CLIENT_ERROR_CODE, message, cause)

    val isLocalClientError: Boolean get() = code == LOCAL_CLIENT_ERROR_CODE

    // ── 业务码精细分支（与 protocol::ErrorCode 数字对齐）──
    val isAuthRequired: Boolean get() = code == 10000
    val isInvalidToken: Boolean get() = code == 10001
    val isPermissionDenied: Boolean get() = code == 10004
    /** USER_API §3 INVALID_USER_IDENTITY → MissingRequiredParam = 10101 */
    val isInvalidUserIdentity: Boolean get() = code == 10101
    /** USER_API §3 INVALID_PHONE_FORMAT → InvalidFormat = 10104 */
    val isInvalidPhoneFormat: Boolean get() = code == 10104
    /** USER_API §3 IDENTITY_CONFLICT → OperationConflict = 10205 */
    val isIdentityConflict: Boolean get() = code == 10205
    val isUserNotFound: Boolean get() = code == 20200

    companion object {
        const val LOCAL_CLIENT_ERROR_CODE: Int = -1
    }
}

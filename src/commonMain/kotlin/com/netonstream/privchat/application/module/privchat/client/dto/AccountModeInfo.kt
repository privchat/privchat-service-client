package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.Serializable

/** `GET /api/service/account-mode` 响应：server `[account] mode`。 */
@Serializable
data class AccountModeInfo(
    /** `BUILTIN` 或 `PLATFORM`。 */
    val mode: String,
) {
    val isPlatform: Boolean get() = mode == PLATFORM

    companion object {
        const val PLATFORM: String = "PLATFORM"
    }
}

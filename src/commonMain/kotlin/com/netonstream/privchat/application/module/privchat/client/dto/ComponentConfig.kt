package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.Serializable

@Serializable
data class ComponentConfigView(
    val schemaVersion: Int,
    val revision: String,
    val maxAgeSeconds: Int,
    val components: Components,
    val settings: RtcSettings,
    /** Privileged status only; false still exposes the stored revision for a repair apply. */
    val runtimeAvailable: Boolean = true,
)
@Serializable
data class Components(val rtc: RtcComponent)
@Serializable
data class RtcComponent(
    val schemaVersion: Int,
    val enabled: Boolean,
    val livekitUrl: String,
    val peerEnabled: Boolean,
    val allowTurn: Boolean,
)
/** Environment variable names only. Never pass actual API credentials. */
@Serializable
data class RtcSettings(
    val enabled: Boolean,
    val livekitUrl: String,
    val apiKeyEnv: String,
    val apiSecretEnv: String,
)
@Serializable
data class UpdateRtcSettings(val expectedRevision: String, val settings: RtcSettings)

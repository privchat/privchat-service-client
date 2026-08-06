package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * IM 平台级隐私配置(PROFILE_VISIBILITY P2)。
 * server: GET/PUT /api/service/privacy-config。
 */
@Serializable
data class PrivacyConfig(
    @SerialName("username_searchable")
    val usernameSearchable: Boolean,
)

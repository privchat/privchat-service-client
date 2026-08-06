package com.netonstream.privchat.application.module.privchat.client

import io.ktor.client.HttpClient
import io.ktor.client.engine.winhttp.WinHttp

internal actual fun defaultHttpClient(): HttpClient = HttpClient(WinHttp) {
    installPrivchatDefaults()
}

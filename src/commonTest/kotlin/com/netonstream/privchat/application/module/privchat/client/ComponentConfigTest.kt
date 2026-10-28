package com.netonstream.privchat.application.module.privchat.client

import com.netonstream.privchat.application.module.privchat.client.dto.RtcSettings
import com.netonstream.privchat.application.module.privchat.client.dto.ComponentConfigView
import com.netonstream.privchat.application.module.privchat.client.dto.UpdateRtcSettings
import com.netonstream.privchat.application.module.privchat.client.error.PrivchatServiceException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import neton.core.http.HttpHeaders
import neton.http.client.*
import kotlin.test.*

class ComponentConfigTest {
    @Test fun typedGetAndPutUseServiceEnvelopeAndRevision() = runTest {
        val requests = mutableListOf<HttpClientRequest>()
        var status = 200
        val http = object : HttpClient {
            override val capabilities = emptySet<HttpClientCapability>()
            override suspend fun request(request: HttpClientRequest): HttpClientResponse {
                requests += request
                val body = if (status == 409) """{"code":1,"message":"revision conflict","data":null}"""
                    else """{"code":0,"message":"OK","data":{"schemaVersion":1,"revision":"revision-2","maxAgeSeconds":300,"components":{"rtc":{"schemaVersion":1,"enabled":true,"livekitUrl":"wss://rtc.example.invalid","peerEnabled":true,"allowTurn":false}},"settings":{"enabled":true,"livekitUrl":"wss://rtc.example.invalid","apiKeyEnv":"RTC_KEY","apiSecretEnv":"RTC_SECRET"}}}"""
                return HttpClientResponse(status, HttpHeaders.EMPTY, body)
            }
            override fun stream(request: HttpClientRequest): Flow<HttpClientStreamChunk> = error("not used")
            override suspend fun close() { error("borrowed client must not be closed") }
        }
        val client = PrivchatServiceClientImpl("http://service.invalid", "service-only-key", httpClient = http)
        val view = client.getComponentConfig()
        assertEquals("revision-2", view.revision)
        assertTrue(view.runtimeAvailable)
        assertFalse(view.components.rtc.allowTurn)
        assertEquals("RTC_SECRET", view.settings.apiSecretEnv)
        assertEquals(HttpClientMethod.Get, requests.single().method)
        assertEquals("http://service.invalid/api/service/config/components", requests.single().url)
        val request = UpdateRtcSettings("revision-1", RtcSettings(true, "wss://rtc.example.invalid", "RTC_KEY", "RTC_SECRET"))
        assertEquals(view, client.updateRtcComponentConfig(request))
        val sent = requests.last()
        assertEquals(HttpClientMethod.Put, sent.method)
        assertEquals("http://service.invalid/api/service/config/components/rtc", sent.url)
        val json = Json.parseToJsonElement((sent.body as HttpClientBody.Json).text).jsonObject
        assertEquals("revision-1", json["expectedRevision"]!!.jsonPrimitive.content)
        assertEquals(setOf("enabled", "livekitUrl", "apiKeyEnv", "apiSecretEnv"), json["settings"]!!.jsonObject.keys)
        status = 409
        assertFailsWith<PrivchatServiceException.Conflict> { client.updateRtcComponentConfig(request) }
        client.close()
    }

    @Test fun privilegedRuntimeAvailabilityIsBackwardCompatible() {
        val raw = """{"schemaVersion":1,"revision":"repair-revision","maxAgeSeconds":300,"components":{"rtc":{"schemaVersion":1,"enabled":true,"livekitUrl":"wss://rtc.example.invalid","peerEnabled":false,"allowTurn":false}},"settings":{"enabled":true,"livekitUrl":"wss://rtc.example.invalid","apiKeyEnv":"RTC_KEY","apiSecretEnv":"RTC_SECRET"}}"""
        val legacy = Json.decodeFromString<ComponentConfigView>(raw)
        assertTrue(legacy.runtimeAvailable)
        val unavailable = Json.decodeFromString<ComponentConfigView>(raw.replaceFirst("{", "{\"runtimeAvailable\":false,"))
        assertFalse(unavailable.runtimeAvailable)
        assertEquals(legacy.revision, unavailable.revision)
        assertEquals(legacy.settings, unavailable.settings)
    }
}

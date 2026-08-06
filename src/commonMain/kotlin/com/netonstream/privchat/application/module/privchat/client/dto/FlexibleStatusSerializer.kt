package com.netonstream.privchat.application.module.privchat.client.dto

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

/**
 * UserInfo.status 的兼容序列化器：privchat-server 不同端点返回不同形态：
 *   - `GET /users` (list)         → `"status": 0`           // 数值
 *   - `GET /users/{uid}` (detail) → `"status": "Active"`    // 字符串枚举名
 *
 * 服务端 v1.x 期内不统一两边，应用层统一吃成 String：
 *   0 → "Active"
 *   1 → "Inactive"
 *   2 → "Suspended"
 *   3 → "Deleted"
 *   其他数值 → 数值的字符串表示（占位，避免硬丢失信息）
 *
 * 反序列化器只在 JsonDecoder 下做兼容；非 JSON 流（比如未来切 protobuf）走 fallback
 * 直接 decodeString。
 */
object FlexibleStatusSerializer : KSerializer<String> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleStatus", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: return decoder.decodeString()
        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonPrimitive) return ""
        if (element.isString) return element.content
        return when (element.intOrNull) {
            0 -> "Active"
            1 -> "Inactive"
            2 -> "Suspended"
            3 -> "Deleted"
            else -> element.content
        }
    }
}

/**
 * 群成员 role 的兼容序列化器：server 序列化为 i16 数值（0/1/2），admin 端按字符串
 * 名展示更直观。映射：
 *   0 → "owner"
 *   1 → "admin"
 *   2 → "member"
 */
object FlexibleMemberRoleSerializer : KSerializer<String> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FlexibleMemberRole", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) {
        encoder.encodeString(value)
    }

    override fun deserialize(decoder: Decoder): String {
        val jsonDecoder = decoder as? JsonDecoder ?: return decoder.decodeString()
        val element = jsonDecoder.decodeJsonElement()
        if (element !is JsonPrimitive) return ""
        if (element.isString) return element.content
        return when (element.intOrNull) {
            0 -> "owner"
            1 -> "admin"
            2 -> "member"
            else -> element.content
        }
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

// 独立库坐标：privchat-server service API 的 Kotlin/Native 服务端到服务端 SDK。
// 抽离自 neton-application-module-privchat/client —— 它从来不是 application module
// （无 @Module / controller / migration），任何模块都可单独依赖它。
group = "com.netonstream"
version = "1.0.0"

kotlin {
    macosArm64()
    linuxX64()
    linuxArm64()
    mingwX64()

    sourceSets {
        val nativeMain by creating { dependsOn(commonMain.get()) }
        val posixMain by creating { dependsOn(nativeMain) }
        val macosMain by creating { dependsOn(posixMain) }
        val linuxMain by creating { dependsOn(posixMain) }
        val macosArm64Main by getting { dependsOn(macosMain) }
        val linuxX64Main by getting { dependsOn(linuxMain) }
        val linuxArm64Main by getting { dependsOn(linuxMain) }
        val mingwX64Main by getting { dependsOn(nativeMain) }

        commonMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        macosMain.dependencies { implementation(libs.ktor.client.darwin) }
        linuxMain.dependencies { implementation(libs.ktor.client.cio) }
        mingwX64Main.dependencies { implementation(libs.ktor.client.winhttp) }
    }
}

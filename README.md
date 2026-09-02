# privchat-service-client

`privchat-server` service API 的 Kotlin/Native **服务端到服务端** SDK。

同族：`privchat-sdk`（Rust 客户端）、`@privchat/sdk`（TS 客户端）。本库是后端之间
调用 `/api/service/*` 的那一条，用 service key 授权，不参与 IM 长连接。

## 定位

**这不是 application module** —— 没有 `@Module`、controller、migration，依赖只有
coroutines / serialization / Neton `HttpClient` 契约（不带引擎，借用应用绑定的客户端）。任何 Neton application 模块（IM 后台、客服、
游戏、助手）都可以单独依赖它，不必牵连彼此。

```kotlin
dependencies {
    implementation("com.netonstream.privchat:service-client")
}
```

composite build 中由 application 的 `dependencySubstitution` 替换为源码子项目。

## 契约

方法与 DTO 以 `privchat-docs/spec/07-application/SERVICE_CLIENT_CONTRACT.md` 为准，
跟随 `privchat-server` 的 service API 版本演进。

# 1. 仓库简介
放一些平时使用的基础的Android基建类
命名参考我最喜欢的动漫![哆啦A梦](assets/img/readme_bg.webp "哆啦A梦")

# 2. 分别介绍一下吧
## 2.1. app介绍
app目录下是本项目的demoApp，目标是涵盖本项目的所有library的用法
## 2.2. library介绍
### 2.2.1. foundation 
作为一个Android-Library，该模块作为所有模块的基础设施。
涵盖以下几个内容
1. 基础Application
2. 基础的Activity兼容平板与手机（横竖屏）
3. 基础的Fragment兼容平板与手机（横竖屏）
4. Activity的基础共享元素动画配置
5. 在Activity，ViewModel，Fragment等含有context的环境下无痛使用协程的扩展函数：launchIO，launchMain，withMain，withIO。主要是省略了每次用作用域调用的看起来很累的写法

### 2.2.2. log
高性能、可扩展的日志模块，支持多渠道分发与环境自适应策略。
主要特性：
1. **环境感知策略**：根据 `isDebug` 标志位自动切换分发模式。Debug 模式仅输出控制台，Release 模式同步输出至控制台、磁盘文件及外部扩展插件。
2. **DSL 初始化**：支持简洁的 Kotlin DSL 风格配置。
3. **自动对象序列化**：通过内置的 GSON 适配层，支持在调用时直接传入对象并自动转换为美化的 JSON 格式。
4. **磁盘持久化**：内置基于协程互斥锁的高性能磁盘打印器，支持按天滚动存储及自动过期清理。
5. **可扩展**：支持自定义 `LogPrinter`（输出目的地）和 `LogFormatter`（消息装饰规则）。

**快速开始：**
```kotlin
// 1. 在 Application 中初始化
LogUtils.init(isDebug = BuildConfig.DEBUG) {
    globalTag = "MyDoraApp"
    diskLogPath = getExternalFilesDir("logs")?.absolutePath
    setupDefaultFormatter(showThread = true, stackDepth = 2)
}

// 2. 使用扩展函数（推荐）
anyObject.logD()
"User login failed".logE(tag = "Login", throwable = ex)

// 3. 静态调用
LogUtils.i("System initialized")
```

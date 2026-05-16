package com.dorameet.log

import com.dorameet.log.printer.ConsolePrinter
import com.dorameet.log.printer.DiskPrinter
import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * 日志核心工具类。
 * 提供静态调用入口，负责日志的格式化及多渠道分发。
 * 使用前必须先通过 [init] 进行初始化。
 */
object LogUtils {

    /** 存储当前配置信息，必须显式初始化 */
    private lateinit var config: LogConfig

    /** 内置控制台打印器，负责 Logcat 输出 */
    private var internalConsolePrinter: ConsolePrinter? = null

    /** 内置磁盘打印器，负责文件输出 */
    private var internalDiskPrinter: DiskPrinter? = null

    /** 默认的 Gson 实例，采用懒加载以优化性能 */
    private val defaultGson by lazy {
        GsonBuilder().setPrettyPrinting().create()
    }

    /** 获取当前有效的 Gson 实例，优先使用用户配置的实例 */
    private val gson: Gson
        get() = config.gson ?: defaultGson

    /**
     * 手动初始化日志配置。
     *
     * @param config 完成构建的 [LogConfig] 对象。
     */
    fun init(config: LogConfig) {
        this.config = config

        // 核心逻辑：无论 Debug 还是 Release，控制台总是开启
        internalConsolePrinter = ConsolePrinter()

        // 只有在非 Debug (Release) 模式下且配置了有效路径，才初始化磁盘打印器
        internalDiskPrinter = if (!config.isDebug) {
            config.diskLogPath?.let { DiskPrinter(it) }
        } else {
            null
        }
    }

    /**
     * 使用 DSL 风格初始化日志配置。
     *
     * @param isDebug 是否为调试模式。
     * @param block 配置构建闭包。
     */
    fun init(isDebug: Boolean, block: LogConfig.Builder.() -> Unit) {
        init(LogConfig.Builder(isDebug).apply(block).build())
    }

    /**
     * 检查日志库是否已成功初始化。
     * @return 如果已调用过 [init] 则返回 true。
     */
    fun isInitialized(): Boolean = this::config.isInitialized

    /**
     * 打印 Verbose 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     */
    fun v(msg: String, tag: String? = null) = log(LogLevel.V, tag, msg)

    /**
     * 打印 Debug 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     */
    fun d(msg: String, tag: String? = null) = log(LogLevel.D, tag, msg)

    /**
     * 打印 Info 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     */
    fun i(msg: String, tag: String? = null) = log(LogLevel.I, tag, msg)

    /**
     * 打印 Warn 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     */
    fun w(msg: String, tag: String? = null) = log(LogLevel.W, tag, msg)

    /**
     * 打印 Error 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     * @param throwable 关联的异常。
     */
    fun e(msg: String, tag: String? = null, throwable: Throwable? = null) =
        log(LogLevel.E, tag, msg, throwable)

    /**
     * 打印 WTF (严重错误) 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     * @param throwable 关联的异常。
     */
    fun wtf(msg: String, tag: String? = null, throwable: Throwable? = null) =
        log(LogLevel.WTF, tag, msg, throwable)

    /**
     * 仅在调试模式下打印 Debug 级别日志。
     * @param msg 日志内容。
     * @param tag 可选的标签。
     */
    fun debug(msg: String, tag: String? = null) {
        if (config.isDebug) log(LogLevel.D, tag, msg)
    }

    /**
     * 将任意对象格式化为 JSON 字符串。
     * 仅供模块内部（如扩展函数）使用。
     *
     * @param any 待格式化的对象。
     * @return 格式化后的字符串。
     */
    internal fun formatJson(any: Any?): String {
        return when (any) {
            null -> "null"
            is String -> any
            else -> runCatching { gson.toJson(any) }.getOrDefault(any.toString())
        }
    }

    /**
     * 统一日志打印入口。
     * 负责检查初始化状态、应用格式化规则以及执行分发策略。
     *
     * @param level 日志级别。
     * @param tag 日志标签。
     * @param msg 日志消息。
     * @param throwable 异常信息。
     */
    private fun log(level: LogLevel, tag: String?, msg: String, throwable: Throwable? = null) {
        // 强制契约：必须初始化
        require(isInitialized()) {
            "LogUtils has not been initialized. Please call LogUtils.init() in your Application.onCreate()."
        }

        val finalTag = tag ?: config.globalTag
        val finalMsg = config.formatter.format(msg)

        // 策略 1：内置控制台总是分发
        internalConsolePrinter?.print(level, finalTag, finalMsg, throwable)

        // 策略 2：只有在 Release 模式下，才分发给磁盘和外部扩展渠道
        if (!config.isDebug) {
            // 内置磁盘持久化
            internalDiskPrinter?.print(level, finalTag, finalMsg, throwable)

            // 外部扩展打印器
            config.extraPrinters.forEach {
                it.print(level, finalTag, finalMsg, throwable)
            }
        }
    }
}

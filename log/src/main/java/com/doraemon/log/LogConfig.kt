package com.doraemon.log

import com.doraemon.log.formatter.DefaultLogFormatter
import com.doraemon.log.formatter.LogFormatter
import com.doraemon.log.printer.LogPrinter
import com.google.gson.Gson

/**
 * 日志配置类。
 * 采用私有构造函数，强制通过 [Builder] 构建以确保配置的完整性。
 *
 * @property isDebug 是否为调试模式。
 * @property globalTag 全局默认的日志标签。
 * @property diskLogPath 磁盘日志的存放文件夹路径。
 * @property extraPrinters 外部扩展的打印器列表。
 * @property formatter 日志消息格式化规则。
 * @property gson 用于将对象序列化为 JSON 的实例。
 */
class LogConfig private constructor(
    val isDebug: Boolean,
    val globalTag: String,
    val diskLogPath: String?,
    val extraPrinters: List<LogPrinter>,
    val formatter: LogFormatter,
    val gson: Gson?
) {
    /**
     * 日志配置建造者类。
     *
     * @param isDebug 是否为调试模式，此参数决定了内置打印器的分发策略。
     */
    class Builder(val isDebug: Boolean) {
        /** 全局默认标签 */
        private var globalTag: String = "DoraLog"

        /** 磁盘日志存放路径，设为 null 则不开启磁盘打印 */
        private var diskLogPath: String? = null

        /** 消息格式化规则，默认为 [DefaultLogFormatter] */
        private var formatter: LogFormatter = DefaultLogFormatter()

        /** 可选的 Gson 实例，用于扩展函数自动转换对象为 JSON */
        private var gson: Gson? = null

        /** 外部扩展打印器列表 */
        private var extraPrinters: MutableList<LogPrinter>? = null

        /** 设置全局默认标签 */
        fun setGlobalTag(tag: String) = apply { this.globalTag = tag }

        /** 设置磁盘日志存放路径 */
        fun setDiskLogPath(path: String?) = apply { this.diskLogPath = path }

        /** 设置自定义的消息格式化规则 */
        fun setFormatter(formatter: LogFormatter) = apply { this.formatter = formatter }

        /** 设置自定义的 Gson 实例 */
        fun setGson(gson: Gson?) = apply { this.gson = gson }

        /**
         * 添加一个外部扩展打印器。
         *
         * @param printer 实现 [LogPrinter] 接口的对象。
         */
        fun addExtraPrinter(printer: LogPrinter) = apply {
            if (extraPrinters == null) extraPrinters = mutableListOf()
            extraPrinters?.add(printer)
        }

        /**
         * 快速配置默认的日志格式化规则。
         *
         * @param showThread 是否显示线程信息。
         * @param stackDepth 堆栈追踪的深度，0 表示不显示。
         */
        fun setupDefaultFormatter(showThread: Boolean = false, stackDepth: Int = 0) = apply {
            this.formatter = DefaultLogFormatter(showThread, stackDepth)
        }

        /**
         * 构建日志配置对象。
         *
         * @throws IllegalArgumentException 如果 [globalTag] 为空。
         */
        internal fun build(): LogConfig {
            require(globalTag.isNotBlank()) { "globalTag 不能为空" }
            return LogConfig(
                isDebug = isDebug,
                globalTag = globalTag,
                diskLogPath = diskLogPath,
                extraPrinters = extraPrinters?.toList() ?: emptyList(),
                formatter = formatter,
                gson = gson
            )
        }
    }
}

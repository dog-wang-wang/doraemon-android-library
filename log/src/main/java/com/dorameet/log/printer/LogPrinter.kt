package com.dorameet.log.printer

import com.dorameet.log.LogLevel

/**
 * 日志打印接口。
 * 实现此接口可自定义日志的输出目的地，例如：上报到服务器、写入数据库或显示在悬浮窗。
 */
interface LogPrinter {
    /**
     * 执行具体的打印操作。
     *
     * @param level 日志级别 ([LogLevel])。
     * @param tag 日志标签。
     * @param msg 经过 [com.dorameet.log.formatter.LogFormatter] 处理后的最终消息内容。
     * @param throwable 可选的异常信息。
     */
    fun print(level: LogLevel, tag: String, msg: String, throwable: Throwable?)
}

package com.doraemon.log.printer

import android.util.Log
import com.doraemon.log.LogLevel

/**
 * 标准控制台日志打印器。
 * 直接将日志输出到系统的 Logcat 缓冲区。
 */
internal class ConsolePrinter : LogPrinter {
    override fun print(level: LogLevel, tag: String, msg: String, throwable: Throwable?) {
        when (level) {
            LogLevel.V -> Log.v(tag, msg)
            LogLevel.D -> Log.d(tag, msg)
            LogLevel.I -> Log.i(tag, msg)
            LogLevel.W -> Log.w(tag, msg)
            LogLevel.E -> Log.e(tag, msg, throwable)
            LogLevel.WTF -> Log.wtf(tag, msg, throwable)
        }
    }
}

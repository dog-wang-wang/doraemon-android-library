package com.dorameet.log.formatter

/**
 * 默认的消息格式化规则实现。
 * 支持在日志内容前附带当前线程信息，以及在内容后追加堆栈追踪信息。
 *
 * @property showThreadInfo 是否开启线程信息展示，如 "[Thread: main]"。
 * @property stackTraceDepth 堆栈追踪打印的行数深度，0 表示不开启。
 */
class DefaultLogFormatter(
    private val showThreadInfo: Boolean = false,
    private val stackTraceDepth: Int = 0
) : LogFormatter {

    override fun format(msg: String): String {
        var result = msg
        
        // 1. 添加线程信息
        if (showThreadInfo) {
            result = "[Thread: ${Thread.currentThread().name}] $result"
        }
        
        // 2. 添加堆栈信息
        if (stackTraceDepth > 0) {
            val stackTrace = Throwable().stackTrace
            var offset = -1
            
            // 查找第一个不在 com.dorameet.log 包下的堆栈帧作为调用点
            for (i in stackTrace.indices) {
                val className = stackTrace[i].className
                if (!className.contains("com.dorameet.log") && !className.contains("java.lang.Throwable")) {
                    offset = i
                    break
                }
            }

            if (offset != -1) {
                val depth = stackTraceDepth.coerceAtMost(stackTrace.size - offset)
                val sb = StringBuilder(result).append("\n")

                for (i in 0 until depth) {
                    if (i + offset < stackTrace.size) {
                        sb.append("\tat ").append(stackTrace[i + offset].toString()).append("\n")
                    }
                }
                result = sb.toString()
            }
        }
        return result
    }
}

package com.doraemon.log.printer

import com.doraemon.log.LogLevel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 磁盘文件日志打印器。
 * 支持异步写入、按天分文件以及自动清理过期日志。
 *
 * @property logFolderPath 日志文件存放的根目录路径。
 * @property retentionDays 日志文件保留的天数，默认 7 天。
 */
internal class DiskPrinter(
    private val logFolderPath: String,
    private val retentionDays: Int = 7
) : LogPrinter {

    /** 专用 IO 作用域，SupervisorJob 保证局部失败不影响全局 */
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    /** 互斥锁，确保多协程环境下日志写入文件的顺序性 */
    private val writeMutex = Mutex()
    
    /** 文件名日期格式 */
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    /** 日志行内容的时间戳格式 */
    private val timeFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault())

    init {
        // 初始化时触发一次过期日志清理
        cleanOldLogs()
    }

    override fun print(level: LogLevel, tag: String, msg: String, throwable: Throwable?) {
        val timestamp = timeFormatter.format(Date())
        val threadName = Thread.currentThread().name
        // 组装最终写入文件的行内容
        val logLine = "$timestamp [$threadName] $level/$tag: $msg\n" +
                (throwable?.let { "\n${android.util.Log.getStackTraceString(it)}\n" } ?: "")

        scope.launch {
            // 异步排队写入
            writeMutex.withLock {
                writeLogToFile(logLine)
            }
        }
    }

    /**
     * 执行文件 IO 写入操作。
     */
    private fun writeLogToFile(content: String) {
        try {
            val folder = File(logFolderPath)
            if (!folder.exists()) folder.mkdirs()

            val fileName = "${dateFormatter.format(Date())}.log"
            val logFile = File(folder, fileName)

            // 追加模式写入
            BufferedWriter(FileWriter(logFile, true)).use { writer ->
                writer.write(content)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 自动清理超过保留期限的旧日志文件。
     */
    private fun cleanOldLogs() {
        scope.launch {
            try {
                val folder = File(logFolderPath)
                if (!folder.exists() || !folder.isDirectory) return@launch

                val currentTime = System.currentTimeMillis()
                val threshold = retentionDays * 24 * 60 * 60 * 1000L

                folder.listFiles()?.forEach { file ->
                    // 根据最后修改时间判断是否过期
                    if (currentTime - file.lastModified() > threshold) {
                        file.delete()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

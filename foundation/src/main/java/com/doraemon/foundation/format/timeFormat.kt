package com.doraemon.foundation.format

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 将秒数格式化为时间字符串。
 * 如果小时小于等于 0，则返回 "mm:ss" 格式，否则返回 "HH:mm:ss" 格式。
 *
 * @return 格式化后的时间字符串
 */
fun Int.secondsFormat(): String {
    val s = this % 60
    val m = this / 60 % 60
    val h = this / 60 / 60
    return if (h <= 0) String.format(Locale.getDefault(), "%02d:%02d", m, s)
    else String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
}

/**
 * 将毫秒数格式化为时间字符串 ("mm:ss" 或 "HH:mm:ss")。
 *
 * @return 格式化后的时间字符串
 */
fun Int.millisFormat(): String {
    return (this / 1000).secondsFormat()
}

/**
 * 将毫秒数(Long)格式化为时间字符串 ("mm:ss" 或 "HH:mm:ss")。
 *
 * @return 格式化后的时间字符串
 */
fun Long.millisFormat(): String {
    return (this / 1000).toInt().secondsFormat()
}

/**
 * 将毫秒数格式化为 SRT 字幕格式的时间字符串 ("HH:mm:ss,SSS")。
 *
 * @return SRT 格式的时间字符串
 */
fun Int.srtFormat(): String {
    val second = this / 1000
    val s = second % 60
    val m = second / 60 % 60
    val h = second / 60 / 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d,%03d", h, m, s, this % 1000)
}

/**
 * 使用指定的格式化字符串格式化时间戳(Long)。
 *
 * @param format DateTimeFormatter 格式字符串 (如 "yyyy-MM-dd HH:mm:ss")
 * @return 格式化后的日期时间字符串
 */
fun Long.format(format: String): String {
    val formatter = DateTimeFormatter.ofPattern(format).withLocale(Locale.ENGLISH).withZone(ZoneId.systemDefault())
    return formatter.format(Instant.ofEpochMilli(this))
}

/**
 * 将毫秒数转换为分钟数。
 *
 * @return 对应的分钟数
 */
fun Long.minutes(): Long {
    return this / 1000 / 60
}

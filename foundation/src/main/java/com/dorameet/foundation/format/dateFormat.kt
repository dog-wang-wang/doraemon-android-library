package com.dorameet.foundation.format

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * 中文日期格式化器 (yyyy/MM/dd)
 */
val DATE_FORMATTER_ZH: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

/**
 * 英文日期格式化器 (MMM dd/yyyy)
 */
val DATE_FORMATTER_EN: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM dd/yyyy")

/**
 * 将时间戳格式化为日期字符串。
 *
 * @return 格式化后的日期字符串
 */
fun Long.formatDateString(): String =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate().toFormattedString()

/**
 * 字符串转 [LocalDate]。
 * 使用中文格式 (yyyy/MM/dd) 进行解析，解析失败则返回当前日期。
 *
 * @return 解析后的 [LocalDate] 对象
 */
fun String.toLocalDate(): LocalDate = try {
    LocalDate.parse(this, DATE_FORMATTER_ZH)
} catch (e: Exception) {
    LocalDate.now()
}

/**
 * [LocalDate] 转 String。
 * 使用中文格式 (yyyy/MM/dd) 进行格式化。
 *
 * @return 格式化后的日期字符串
 */
fun LocalDate.toFormattedString(): String = this.format(DATE_FORMATTER_ZH)

/**
 * 根据语言选择 [LocalDate] 不同的转换格式。
 *
 * @param localDate 要格式化的日期
 * @return 格式化后的日期字符串（中文为 yyyy/MM/dd，其他为 MMM dd/yyyy）
 */
fun Locale.formatDate(localDate: LocalDate): String {
    val formatter = when (language) {
        "zh" -> DATE_FORMATTER_ZH
        else -> DATE_FORMATTER_EN
    }
    return localDate.format(formatter)
}

/**
 * 判断 [LocalDate] 是否在一周内。
 *
 * @return 如果日期在过去 7 天内，则返回 true
 */
fun LocalDate.withinOneWeek(): Boolean {
    val oneWeekAgo = LocalDate.now().minusWeeks(1)
    return this.isAfter(oneWeekAgo)
}

/**
 * 判断 [LocalDate] 是否在一月内。
 *
 * @return 如果日期在过去 30 天内，则返回 true
 */
fun LocalDate.withinOneMonth(): Boolean {
    val oneMonthAgo = LocalDate.now().minusMonths(1)
    return this.isAfter(oneMonthAgo)
}

/**
 * 判断日期字符串是否是一周内。
 *
 * @return 如果日期在过去 7 天内，则返回 true
 */
fun String.withinOneWeek(): Boolean = toLocalDate().withinOneWeek()

/**
 * 判断日期字符串是否是一月内。
 *
 * @return 如果日期在过去 30 天内，则返回 true
 */
fun String.withinOneMonth(): Boolean = toLocalDate().withinOneMonth()

/**
 * 将日期字符串按照当前语言环境进行本地化转换。
 *
 * @return 格式化后的本地化日期字符串
 */
fun String.toLocalizedDate(): String = Locale.getDefault().formatDate(toLocalDate())

/**
 * 获取今天的日期字符串。
 * 使用 yyyy/MM/dd 格式。
 *
 * @return 今天的日期字符串
 */
fun formatToday(): String = LocalDate.now().toFormattedString()

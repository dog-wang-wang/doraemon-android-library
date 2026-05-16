package com.dorameet.log.formatter

/**
 * 日志装饰器/格式化器接口。
 * 用于在最终打印前对原始消息进行加工（如添加元数据、美化样式等）。
 */
interface LogFormatter {
    /**
     * 执行格式化逻辑。
     *
     * @param msg 原始日志内容。
     * @return 经过加工后的完整消息内容。
     */
    fun format(msg: String): String
}

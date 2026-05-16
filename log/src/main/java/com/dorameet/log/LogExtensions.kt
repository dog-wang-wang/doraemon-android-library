package com.dorameet.log

/**
 * 日志扩展函数集合。
 * 提供便捷语法支持在任意对象上直接发起日志打印。
 * 会根据对象类型自动决定是原样输出还是转换为 JSON。
 */

/**
 * 打印 Verbose 级别日志。
 * @param tag 可选的自定义标签，不传则使用配置中的全局标签。
 */
fun Any?.logV(tag: String? = null) {
    LogUtils.v(LogUtils.formatJson(this), tag)
}

/**
 * 打印 Debug 级别日志。
 * @param tag 可选的自定义标签。
 */
fun Any?.logD(tag: String? = null) {
    LogUtils.d(LogUtils.formatJson(this), tag)
}

/**
 * 打印 Info 级别日志。
 * @param tag 可选的自定义标签。
 */
fun Any?.logI(tag: String? = null) {
    LogUtils.i(LogUtils.formatJson(this), tag)
}

/**
 * 打印 Warn 级别日志。
 * @param tag 可选的自定义标签。
 */
fun Any?.logW(tag: String? = null) {
    LogUtils.w(LogUtils.formatJson(this), tag)
}

/**
 * 打印 Error 级别日志。
 * @param tag 可选的自定义标签。
 * @param throwable 关联的异常信息。
 */
fun Any?.logE(tag: String? = null, throwable: Throwable? = null) {
    LogUtils.e(LogUtils.formatJson(this), tag, throwable)
}

/**
 * 快捷打印调试日志。
 * @param msg 日志内容。
 * @param tag 日志标签。
 */
fun debug(msg: String, tag: String? = null) = LogUtils.debug(msg, tag)

/**
 * 快捷打印错误日志。
 * @param msg 日志内容。
 * @param tag 日志标签。
 * @param throwable 异常信息。
 */
fun error(msg: String, tag: String? = null, throwable: Throwable? = null) = LogUtils.e(msg, tag, throwable)

/**
 * 快捷打印普通日志（默认使用 Debug 级别）。
 * @param msg 日志内容。
 * @param tag 日志标签。
 */
fun log(msg: String, tag: String) = msg.logD(tag)

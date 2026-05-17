package com.doraemon.foundation.utils

import androidx.annotation.StringRes
import com.doraemon.foundation.ContextProvider
import com.doraemon.log.debug
import com.doraemon.log.error
import com.doraemon.log.log

fun debugWithEvent(event: String, msg: String, tag: String? = null) {
    val msg = wrapperEventLog(event, msg)
    debug(msg, tag)
}

fun errorWithEvent(event: String, msg: String, tag: String? = null, throwable: Throwable? = null) {
    val msg = wrapperEventLog(event, msg)
    error(msg, tag, throwable)
}

fun logWithEvent(event: String, msg: String, tag: String? = null) {
    val msg = wrapperEventLog(event, msg)
    log(msg, tag)
}

fun debugWithEvent(@StringRes event: Int, msg: String, tag: String? = null) = ContextProvider.appContext?.resources?.getString(event)?.let {
    debugWithEvent(it, msg, tag)
}

fun errorWithEvent(@StringRes event: Int, msg: String, tag: String? = null, throwable: Throwable? = null) = ContextProvider.appContext?.resources?.getString(event)?.let {
    errorWithEvent(it, msg, tag, throwable)
}

fun logWithEvent(@StringRes event: Int, msg: String, tag: String? = null) = ContextProvider.appContext?.resources?.getString(event)?.let {
    logWithEvent(it, msg, tag)
}

fun wrapperEventLog(event: String, msg: String) = "\n[event: $event]\n$msg"
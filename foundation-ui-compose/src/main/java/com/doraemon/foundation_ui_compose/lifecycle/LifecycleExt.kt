package com.doraemon.foundation_ui_compose.lifecycle

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.doraemon.foundation.coroutine.launchIO
import com.doraemon.foundation.coroutine.launchMain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope


fun Context.launchMain(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchMain(errorHandler, action)
}

fun Context.launchIO(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchIO(errorHandler, action)
}
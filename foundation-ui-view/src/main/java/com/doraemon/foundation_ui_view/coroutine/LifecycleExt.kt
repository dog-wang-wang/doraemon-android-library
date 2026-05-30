package com.doraemon.foundation_ui_view.coroutine

import android.content.Context
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.doraemon.foundation.coroutine.launchIO
import com.doraemon.foundation.coroutine.launchMain
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope

fun Fragment.launchMain(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchMain(errorHandler, action)
}

fun Fragment.launchIO(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchIO(errorHandler, action)
}

fun Context.launchMain(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchMain(errorHandler, action)
}

fun Context.launchIO(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchIO(errorHandler, action)
}

fun View.launchMain(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    context.launchMain(errorHandler, action)
}

fun View.launchIO(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    context.launchIO(errorHandler, action)
}
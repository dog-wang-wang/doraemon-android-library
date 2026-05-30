package com.doraemon.foundation.coroutine

import android.app.Dialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.launchIO(errorHandler: CoroutineExceptionHandler, action: suspend CoroutineScope.() -> Unit) {
    launch(
        Dispatchers.IO + errorHandler,
        block = action
    )
}

fun CoroutineScope.launchMain(errorHandler: CoroutineExceptionHandler, action: suspend CoroutineScope.() -> Unit) {
    launch(
        Dispatchers.Main + errorHandler,
        block = action
    )
}

fun ViewModel.launchIO(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launchIO(errorHandler, action)
}

fun ViewModel.launchMain(errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> }, action: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launchMain(errorHandler, action)
}

fun CoroutineScope.launchMainWithDialog(
    dialog: Dialog,
    errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> },
    action: suspend CoroutineScope.() -> Unit
) {
    launch(
        Dispatchers.Main + errorHandler,
        block = {
            dialog.show()
            action.invoke(this)
            dialog.dismiss()
        }
    )
}

suspend fun <T> withIO(
    errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> },
    block: suspend () -> T
) = withContext(Dispatchers.IO + errorHandler) { block() }

suspend fun <T> withMain(
    errorHandler: CoroutineExceptionHandler = CoroutineExceptionHandler { _, _ -> },
    block: suspend () -> T
) = withContext(Dispatchers.Main + errorHandler) { block() }

fun LifecycleOwner.addOnDestroy(onDestroy: () -> Unit) {
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                onDestroy.invoke()
            }
        }
    )
}
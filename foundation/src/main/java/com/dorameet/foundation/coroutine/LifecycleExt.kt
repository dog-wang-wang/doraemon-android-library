package com.dorameet.foundation.coroutine

import android.app.Dialog
import android.content.Context
import android.view.View
import androidx.activity.ComponentActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun CoroutineScope.launchIO(action: suspend CoroutineScope.() -> Unit) {
    launch(
        Dispatchers.IO + CoroutineExceptionHandler { _, _ -> },
        block = action
    )
}

fun Context.launchIO(action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchIO(action)
}

fun View.launchIO(action: suspend CoroutineScope.() -> Unit) {
    context.launchIO(action)
}

fun ViewModel.launchIO(action: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launchIO(action)
}

fun Fragment.launchIO(action: suspend CoroutineScope.()-> Unit) {
    lifecycleScope.launchIO { action() }
}

fun CoroutineScope.launchMain(action: suspend CoroutineScope.() -> Unit) {
    launch(
        Dispatchers.Main + CoroutineExceptionHandler { _, _ -> },
        block = action
    )
}

fun Context.launchMain(action: suspend CoroutineScope.() -> Unit) {
    (this as? ComponentActivity)?.lifecycleScope?.launchMain(action)
}

fun View.launchMain(action: suspend CoroutineScope.() -> Unit) {
    context.launchMain(action)
}

fun ViewModel.launchMain(action: suspend CoroutineScope.() -> Unit) {
    viewModelScope.launchMain(action)
}

fun Fragment.launchMain(action: suspend CoroutineScope.() -> Unit) {
    lifecycleScope.launchMain(action)
}


fun CoroutineScope.launchMainWithDialog(dialog: Dialog, action: suspend CoroutineScope.() -> Unit) {
    launch(
        Dispatchers.Main + CoroutineExceptionHandler { _, _ -> dialog.dismiss() },
        block = {
            dialog.show()
            action.invoke(this)
            dialog.dismiss()
        }
    )
}

suspend fun <T> withIO(block: suspend () -> T) = withContext(Dispatchers.IO) { block() }

suspend fun <T> withMain(block: suspend () -> T) = withContext(Dispatchers.Main) { block() }

fun LifecycleOwner.addOnDestroy(onDestroy: () -> Unit) {
    lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                onDestroy.invoke()
            }
        }
    )
}
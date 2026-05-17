package com.doraemon.foundation.tips

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import com.doraemon.foundation.ContextProvider

fun View.toastShort(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()
}

fun Context.toastShort(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
}

fun toastShort(text: String?) {
    text ?: return
    val appContext = ContextProvider.appContext ?: return
    Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
}

fun toastShort(@StringRes resId: Int) {
    val appContext = ContextProvider.appContext ?: return
    val text = appContext.getString(resId)
    Toast.makeText(appContext, text, Toast.LENGTH_SHORT).show()
}

fun View.toastLong(@StringRes resId: Int) {
    Toast.makeText(context, resId, Toast.LENGTH_LONG).show()
}

fun Context.toastLong(@StringRes resId: Int) {
    Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
}

fun toastLong(text: String?) {
    text ?: return
    val appContext = ContextProvider.appContext ?: return
    Toast.makeText(appContext, text, Toast.LENGTH_LONG).show()
}
package com.doraemon.foundation_ui_view.nav

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.doraemon.foundation.router.nav
import com.doraemon.foundation.router.navWithAction
import com.doraemon.foundation.router.navWithActionAndOptions


fun <T> View.navWithAction(activity: Class<T>, intentAction: (Intent.() -> Unit)? = null) = context.navWithAction(activity, intentAction)

fun <T> View.nav(activity: Class<T>, bundle: Bundle? = null) = context.nav(activity, bundle)

@JvmName("viewNavWithString")    // 添加唯一 JVM 名称
fun <T> View.nav(activity: Class<T>, stringPair: Pair<String, String>) = context.nav(activity, stringPair)

@JvmName("viewNavWithInteger")    // 添加唯一 JVM 名称
fun <T> View.nav(activity: Class<T>, intPair: Pair<String, Int>) = context.nav(activity, intPair)

@JvmName("viewNavOptionsWithString")
fun <T> View.nav(activity: Class<T>, options: ActivityOptions, stringPair: Pair<String, String>) {
    context.navWithActionAndOptions(activity, options) {
        putExtra(stringPair.first, stringPair.second)
    }
}

@JvmName("viewNavOptionsWithInteger")
fun <T> View.nav(activity: Class<T>, options: ActivityOptions, stringPair: Pair<String, Int>) {
    context.navWithActionAndOptions(activity, options) {
        putExtra(stringPair.first, stringPair.second)
    }
}
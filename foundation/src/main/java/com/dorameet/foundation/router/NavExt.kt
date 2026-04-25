package com.dorameet.foundation.nav.router

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View

const val INTENT_NEXT = 0
const val INTENT_CLEAR_TASK = 1

fun <T> Context.navWithActionAndOptions(activity: Class<T>, options: ActivityOptions, intentAction: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, activity).apply { intentAction?.invoke(this) }
    val bundle = options.toBundle()
    startActivity(intent, bundle)
}

fun <T> Context.navWithAction(activity: Class<T>, intentAction: (Intent.() -> Unit)? = null) {
    val intent = Intent(this, activity)
    intentAction?.let { intent.it() }
    startActivity(intent)
}

fun <T> Context.nav(activity: Class<T>, bundle: Bundle? = null) {
    navWithAction(activity) {
        bundle ?: return@navWithAction
        putExtras(bundle)
    }
}

@JvmName("contextNavWithString") // 添加唯一 JVM 名称
fun <T> Context.nav(activity: Class<T>, stringPair: Pair<String, String>) {
    navWithAction(activity) {
        putExtra(stringPair.first, stringPair.second)
    }
}

@JvmName("contextNavWithInteger") // 添加唯一 JVM 名称
fun <T> Context.nav(activity: Class<T>, stringPair: Pair<String, Int>) {
    navWithAction(activity) {
        putExtra(stringPair.first, stringPair.second)
    }
}

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

inline fun <reified T : Activity> Activity.nav(clazz: Class<T> = T::class.java, mode: Int = INTENT_NEXT) {
    val intent = Intent(this, clazz)
    when (mode) {
        INTENT_CLEAR_TASK -> intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}


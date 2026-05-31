package com.doraemon.foundation.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher

fun Context.checkPermission(permission: String) = PackageManager.PERMISSION_GRANTED == checkSelfPermission(permission)

fun Context.checkPermissions(permissions: Array<String>): Boolean {
    permissions.forEach {
        if (!checkPermission(it)) {
            return false
        }
    }
    return true
}

fun Context.withPermission(permissions: Array<String>, requestCode: Int = 100, block: (() -> Unit)) {
    val activity = this as Activity
    if (checkPermissions(permissions)) {
        block.invoke()
    } else {
        // 没有权限的时候要申请权限
        activity.requestPermissions(permissions, requestCode)
    }
}

fun Context.withPermission(permission: String, requestCode: Int = 100, block: (() -> Unit)) {
    withPermission(arrayOf(permission), requestCode, block)
}

fun ActivityResultLauncher<String>.withPermission(context: Context, permission: String, block: (() -> Unit)) {
    if (context.checkPermissions(arrayOf(permission))) {
        block.invoke()
    } else {
        // 没有权限的时候要申请权限
        launch(permission)
    }
}
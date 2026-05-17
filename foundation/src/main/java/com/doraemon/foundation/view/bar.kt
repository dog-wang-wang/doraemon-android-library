package com.doraemon.foundation.view

import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

/**
 * 沉浸式状态栏
 *
 * @description 当applyTop与applyBottom都为true或都为false时，则应用到顶部和底部
 * @param applyTop 是否应用到顶部
 * @param applyBottom 是否应用到底部
 */
fun ComponentActivity.applyImmersiveMode(
    applyTop: Boolean = false,
    applyBottom: Boolean = false,
): Pair<WindowInsetsControllerCompat, Int> {
    val mode = when {
        applyTop && applyBottom -> WindowInsetsCompat.Type.systemBars()
        applyTop -> WindowInsetsCompat.Type.statusBars()
        applyBottom -> WindowInsetsCompat.Type.navigationBars()
        else -> WindowInsetsCompat.Type.systemBars()
    }
    // 获取控制器
    val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
    // 配置隐藏行为
    windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    // 隐藏起来
    windowInsetsController.hide(mode)
    return windowInsetsController to mode
}

/**
 * 沉浸式状态栏
 *
 * @description 当applyTop与applyBottom都为true或都为false时，则应用到顶部和底部
 * @param clickChangeImmersive 点击屏幕时，是否切换沉浸式状态栏
 * @param applyTop 是否应用到顶部
 * @param applyBottom 是否应用到底部
 */
fun ComponentActivity.applyImmersiveMode(
    clickChangeImmersive: Boolean,
    applyTop: Boolean = false,
    applyBottom: Boolean = false,
) {
    val (controller, mode) = applyImmersiveMode(applyTop, applyBottom)
    if (!clickChangeImmersive) return
    // 配置监听器来设置状态栏和导航栏的显示和隐藏
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
        val home = window.decorView.findViewById<FrameLayout>(Window.ID_ANDROID_CONTENT).getChildAt(0)
        home.clicks {
            // 通过状态栏的显示与否来配置监听事件
            val isNavigationBarVisibility = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
            val isStatusBarVisibility = windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
            if (isNavigationBarVisibility && isStatusBarVisibility) {
                // 显示
                controller.hide(mode)
            } else {
                // 隐藏
                controller.show(mode)
            }
        }
        ViewCompat.onApplyWindowInsets(view, windowInsets)
    }
}

/**
 * 沉浸式状态栏
 *
 * @description 当applyTop与applyBottom都为true或都为false时，则应用到顶部和底部
 * @param viewChangeImmersive 外部传入的用于控制状态栏沉浸式的view
 * @param applyTop 是否应用到顶部
 * @param applyBottom 是否应用到底部
 */
fun ComponentActivity.applyImmersiveMode(
    viewChangeImmersive: View,
    applyTop: Boolean = false,
    applyBottom: Boolean = false,
) {
    val (controller, mode) = applyImmersiveMode(applyTop, applyBottom)
    // 配置监听器来设置状态栏和导航栏的显示和隐藏
    ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
        viewChangeImmersive.clicks {
            // 通过状态栏的显示与否来配置监听事件
            val isNavigationBarVisibility = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
            val isStatusBarVisibility = windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())
            if (isNavigationBarVisibility && isStatusBarVisibility) {
                // 显示
                controller.hide(mode)
            } else {
                // 隐藏
                controller.show(mode)
            }
        }
        ViewCompat.onApplyWindowInsets(view, windowInsets)
    }
}

fun ComponentActivity.uiImmersiveMode() {
    enableEdgeToEdge()
    applyImmersiveMode()
}
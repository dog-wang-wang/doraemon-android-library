package com.doraemon.foundation_ui_compose.ui.navigation.core

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

/**
 * 执行底部 tab 的 top-level 导航。
 *
 * top-level 导航和普通 push 页面不同：
 * - [launchSingleTop]：重复点击同一个 tab 时，不重复创建同一个页面实例。
 * - [restoreState]：回到某个 tab 时，尽量恢复它之前的滚动位置和子页面状态。
 * - popUpTo(startDestination) + saveState：切换 tab 时把当前 tab 的中间状态保存起来，
 *   同时把返回栈整理回导航图的起点，避免 tab 来回切换后栈越来越深。
 */
internal fun NavController.navigateTopLevel(route: String) {
    navigate(route = route) {
        launchSingleTop = true
        restoreState = true
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
    }
}

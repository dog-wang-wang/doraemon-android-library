package com.doraemon.foundation_ui_compose.ui.navigation.core

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavigationBarDestinations

/**
 * 计算安全的首页路由。
 *
 * [NavigationBarDestinations.getDefaultIndex] 会先校验列表非空，再把非法下标修正到合法范围。
 * 因此这里返回的一定是可以传给 NavHost 的 route。
 */
internal fun NavigationBarDestinations.startDestination(defaultIndex: Int): String {
    val safeDefaultIndex = getDefaultIndex()
    val startIndex = if (defaultIndex == NavigationBarDestinations.DEFAULT_INDEX) {
        safeDefaultIndex
    } else {
        defaultIndex.coerceIn(itemDestinations.indices)
    }
    return itemDestinations[startIndex].routerName
}

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

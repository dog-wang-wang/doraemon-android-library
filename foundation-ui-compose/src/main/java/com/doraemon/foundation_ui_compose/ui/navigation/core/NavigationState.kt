package com.doraemon.foundation_ui_compose.ui.navigation.core

import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations

private object NavigationStateTokens {
    const val NOT_FOUND_INDEX = -1
    const val FIRST_INDEX = 0
}

/**
 * 底栏当前选中态。
 *
 * 这个对象只描述“哪个路由被选中、它在底栏里是第几个”，不关心具体 UI。
 * Material 底栏和悬浮底栏都应该复用这套结果，避免两个组件各自实现一份选中逻辑。
 *
 * @param route 当前应该高亮的路由。
 * @param index 当前应该高亮的下标。
 */
internal data class NavSelection(
    val route: String,
    val index: Int,
)

/**
 * 计算安全的首页路由。
 *
 * [NavDestinations.getDefaultIndex] 会先校验列表非空，再把非法下标修正到合法范围。
 * 因此这里返回的一定是可以传给 NavHost 的 route。
 */
internal fun NavDestinations.startDestination(defaultIndex: Int): String {
    val startIndex = startDestinationIndex(defaultIndex)
    return items[startIndex].route
}

/**
 * 根据当前路由计算底栏选中态。
 *
 * 当前路由可能为空：NavHost 第一次组合时，back stack 还没有完全准备好。
 * 当前路由也可能不是底栏路由：例如页面来自深链，或者业务跳到了某个详情页。
 *
 * 这两种情况都不能让 UI 崩溃：
 * - 路由为空时，使用安全首页路由。
 * - 路由不在底栏中时，回退到第一个 tab，让指示器仍然落在合法位置。
 */
internal fun NavDestinations.resolveSelection(
    currentRoute: String?,
    defaultIndex: Int = NavDestinations.DEFAULT_INDEX,
): NavSelection {
    val fallbackRoute = startDestination(defaultIndex)
    val routeToMatch = currentRoute ?: fallbackRoute
    val selectedIndex = items
        .indexOfFirst { it.route == routeToMatch }
        .takeIf { it != NavigationStateTokens.NOT_FOUND_INDEX }
        ?: NavigationStateTokens.FIRST_INDEX
    val selectedRoute = items[selectedIndex].route

    return NavSelection(
        route = selectedRoute,
        index = selectedIndex,
    )
}

private fun NavDestinations.startDestinationIndex(defaultIndex: Int): Int {
    val safeDefaultIndex = getDefaultIndex()
    return if (defaultIndex == NavDestinations.DEFAULT_INDEX) {
        safeDefaultIndex
    } else {
        defaultIndex.coerceIn(items.indices)
    }
}

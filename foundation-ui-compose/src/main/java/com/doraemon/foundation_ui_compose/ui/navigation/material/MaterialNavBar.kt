package com.doraemon.foundation_ui_compose.ui.navigation.material

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.doraemon.foundation_ui_compose.ui.navigation.core.navigateTopLevel
import com.doraemon.foundation_ui_compose.ui.navigation.core.resolveSelection
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations

/**
 * 项目统一 Material 底部导航组件。
 *
 * 这个组件保留 Material3 的 [NavigationBar] 和 [NavigationBarItem] 行为：
 * 适合常规 Android 底部导航，也方便需要 Material 默认无障碍、触摸反馈和布局规则的业务复用。
 *
 * 选中态不单独用 remember 保存，而是从 [NavController] 的 back stack 读取。
 * 这样用户通过系统返回键、深链或代码跳转切换页面时，底栏高亮也能和真实页面保持一致。
 */
@Composable
fun MaterialNavBar(
    controller: NavController,
    destinations: NavDestinations,
    modifier: Modifier = Modifier,
) {
    // currentBackStackEntryAsState 会把导航栈转成 Compose State；路由变化时底栏会自动重组。
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val selection = destinations.resolveSelection(
        currentRoute = navBackStackEntry?.destination?.route,
    )

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        destinations.items.forEach { itemDestination ->
            val selected = itemDestination.route == selection.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    // tab 点击属于 top-level 导航：切换 tab 时复用已有页面，并保留每个 tab 的返回栈状态。
                    controller.navigateTopLevel(itemDestination.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                icon = {
                    Box(modifier = Modifier.size(24.dp)) {
                        itemDestination.icon()
                    }
                },
                label = itemDestination.label,
            )
        }
    }
}

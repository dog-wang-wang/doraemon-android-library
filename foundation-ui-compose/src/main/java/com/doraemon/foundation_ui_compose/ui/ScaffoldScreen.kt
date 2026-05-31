package com.doraemon.foundation_ui_compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.doraemon.foundation_ui_compose.model.NavigationBarDestinations


/**
 * 应用首页的通用脚手架。
 *
 * 这个组件负责统一创建 [NavHostController]、计算默认路由、并把底部导航条交给
 * [DoraemonNavigationBar] 渲染。业务模块只需要提供目的地列表和 NavHost 内容，
 * 就可以复用同一套导航结构，避免每个 App 页面都重复维护底栏状态和路由入口。
 */
@Composable
fun HomeScreen(
    navigationBarDestinations: NavigationBarDestinations,
    navigationBarModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavigationBarDestinations.DEFAULT_INDEX,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit
) {
    // 导航控制器只在 HomeScreen 内创建，保证底栏和 NavHost 使用同一个导航状态源。
    val navController = rememberNavController()
    // 如果调用方显式传入 defaultIndex，就优先使用；否则读取 destinations 自带默认项。
    val startIndex = if (defaultIndex != NavigationBarDestinations.DEFAULT_INDEX) defaultIndex else navigationBarDestinations.getDefaultIndex()
    val startDestination = navigationBarDestinations.itemDestinations[startIndex].routerName

    Scaffold(
        modifier = modifier,
        bottomBar = { DoraemonNavigationBar(navController, navigationBarDestinations, navigationBarModifier) }
    ) { contentPadding ->
        // Scaffold 会把系统栏和底栏占位通过 contentPadding 传进来，业务内容只要消费它即可。
        navHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}

@Composable
fun HomeFloatNavigationScreen(
    navigationBarDestinations: NavigationBarDestinations,
    navigationBarModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavigationBarDestinations.DEFAULT_INDEX,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit
) {
    // 导航控制器只在 HomeScreen 内创建，保证底栏和 NavHost 使用同一个导航状态源。
    val navController = rememberNavController()
    // 如果调用方显式传入 defaultIndex，就优先使用；否则读取 destinations 自带默认项。
    val startIndex = if (defaultIndex != NavigationBarDestinations.DEFAULT_INDEX) defaultIndex else navigationBarDestinations.getDefaultIndex()
    val startDestination = navigationBarDestinations.itemDestinations[startIndex].routerName

    Scaffold(
        modifier = modifier,
    ) { contentPadding ->
        // Scaffold 会把系统栏和底栏占位通过 contentPadding 传进来，业务内容只要消费它即可。
        Box(modifier) {
            navHost(navController, startDestination, Modifier.padding(contentPadding))
            DoraemonNavigationBar(navController, navigationBarDestinations, navigationBarModifier)
        }
    }
}

/**
 * 项目统一底部导航组件。
 */
@Composable
fun DoraemonNavigationBar(
    controller: NavController,
    destinations: NavigationBarDestinations,
    modifier: Modifier = Modifier,
) {
    // 初始选项和 HomeScreen 的 startDestination 使用同一份配置，避免默认页和高亮项不一致。
    val startDestination = destinations.getDefaultIndex()
    // 选中项用 rememberSaveable 保存，旋转屏幕或进程恢复后仍能保留用户当前所在 tab。
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination) }
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.72f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        tonalElevation = 0.dp,
        windowInsets = NavigationBarDefaults.windowInsets,
    ) {
        // 遍历导航栏数据来创建 Item，路由名称仍由业务模块传入，组件本身不感知具体页面。
        destinations.itemDestinations.forEachIndexed { index, itemDestination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    // 点击后先更新本地选中状态，再执行导航；重复点击同一 tab 时不叠加多个实例。
                    selectedDestination = index
                    controller.navigate(route = itemDestination.routerName) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(controller.graph.startDestinationId) {
                            saveState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                icon = {
                    // 统一给 icon 一个稳定尺寸，避免不同图标源尺寸不同导致底栏跳动。
                    Box(modifier = Modifier.size(24.dp)) {
                        itemDestination.icon()
                    }
                },
                label = itemDestination.label,
            )
        }
    }
}

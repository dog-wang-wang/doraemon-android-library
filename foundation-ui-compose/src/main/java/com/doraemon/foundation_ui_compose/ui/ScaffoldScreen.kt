package com.doraemon.foundation_ui_compose.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.doraemon.foundation_ui_compose.model.NavigationBarDestinations


@Composable
fun HomeScreen(
    navigationBarDestinations: NavigationBarDestinations,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavigationBarDestinations.DEFAULT_INDEX,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit
) {
    // 导航控制器
    val navController = rememberNavController()
    // 使用传入的 defaultIndex 或 destinations 自带的默认值
    val startIndex = if (defaultIndex != NavigationBarDestinations.DEFAULT_INDEX) defaultIndex else navigationBarDestinations.getDefaultIndex()
    val startDestination = navigationBarDestinations.itemDestinations[startIndex].routerName

    Scaffold(
        // HomeScreen的modifier
        modifier = modifier,
        // HomeScreen的BottomBar
        bottomBar = { DoraemonNavigationBar(navController, navigationBarDestinations) }
    ) { contentPadding ->
        navHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}

@Composable
fun DoraemonNavigationBar(
    controller: NavController,
    destinations: NavigationBarDestinations,
    modifier: Modifier = Modifier,
) {
    // 初始选项
    val startDestination = destinations.getDefaultIndex()
    // 被选中的选项
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination) }
    NavigationBar(
        // navigationBar设置Modifier
        modifier = modifier,
        // navigationBar设置窗口内衬(适配状态栏)
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        // 遍历导航栏数据来创建Item
        destinations.itemDestinations.forEachIndexed { index, itemDestination ->
            NavigationBarItem(
                selected = selectedDestination == index,
                onClick = {
                    // 点击之后触发导航
                    controller.navigate(route = itemDestination.routerName)
                    // 并且更新当前选项触发DoraemonNavigationBar的重组
                    selectedDestination = index
                },
                icon = itemDestination.icon,
                label = itemDestination.label
            )
        }
    }
}

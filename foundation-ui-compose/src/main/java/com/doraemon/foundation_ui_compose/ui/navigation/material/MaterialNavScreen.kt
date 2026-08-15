package com.doraemon.foundation_ui_compose.ui.navigation.material

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.doraemon.foundation_ui_compose.ui.navigation.core.startDestination
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations

/**
 * Material 底部导航首页脚手架。
 *
 * 它适合“标准 Material 底栏”场景：底栏占据页面底部空间，业务内容会自动避开底栏和系统栏。
 *
 * 初学者可以把它理解成三件事：
 * 1. 创建一个 [NavHostController]，让底栏和 NavHost 使用同一个导航状态。
 * 2. 根据 [destinations] 算出首页路由。
 * 3. 使用 [Scaffold] 的 bottomBar 承载底栏，并把 contentPadding 交给业务页面。
 *
 * @param destinations 底栏数据，包括文案、图标、路由和默认项。
 * @param barModifier 只作用于底栏，适合设置底栏自身的 padding、阴影或形状。
 * @param modifier 作用于整个首页容器。
 * @param defaultIndex 外部指定默认页；不传时使用 [NavDestinations] 自己的默认值。
 * @param navHost 业务侧的 NavHost 内容。基础库负责传入 controller、startDestination 和内容修饰符。
 */
@Composable
fun MaterialNavScreen(
    destinations: NavDestinations,
    barModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavDestinations.DEFAULT_INDEX,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit,
) {
    // rememberNavController 会在重组时复用同一个 controller，避免页面重组后导航状态丢失。
    val navController = rememberNavController()
    val startDestination = destinations.startDestination(defaultIndex)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            MaterialNavBar(
                controller = navController,
                destinations = destinations,
                modifier = barModifier,
            )
        },
    ) { contentPadding ->
        // Scaffold 会计算系统栏和 bottomBar 占用的安全区域；业务页面消费这个 padding 后不会被底栏遮住。
        navHost(navController, startDestination, Modifier.padding(contentPadding))
    }
}

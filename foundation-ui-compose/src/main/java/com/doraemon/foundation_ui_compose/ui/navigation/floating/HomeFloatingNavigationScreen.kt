package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.doraemon.foundation_ui_compose.ui.navigation.core.startDestination
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavigationBarDestinations

/**
 * 带自定义悬浮导航栏的首页脚手架。
 *
 * 它适合类似 iOS 的“悬浮胶囊底栏”：底栏浮在内容上方，页面背景可以从底部透出来。
 *
 * 和 [com.doraemon.foundation_ui_compose.ui.navigation.material.MaterialNavigationScreen] 的区别：
 * - MaterialNavigationScreen 使用 Scaffold.bottomBar，底栏会占据页面空间。
 * - HomeFloatingNavigationScreen 把底栏放在 Box 之上，底栏覆盖在内容上，不参与 Scaffold 的底部占位。
 *
 * @param navigationBarDestinations 底栏数据。
 * @param navigationBarModifier 只作用于悬浮底栏容器，适合设置外边距、阴影、额外裁剪等。
 * 系统栏避让、距离屏幕边缘多远，都应该由调用方通过这个 modifier 明确控制。
 * @param modifier 作用于整个首页容器。
 * @param defaultIndex 外部指定默认页；不传时使用 [NavigationBarDestinations] 自己的默认值。
 * @param navigationBarStyle 悬浮底栏的视觉配置，例如高度、圆角和背景策略。
 * @param navigationBarAlignment 导航栏在页面容器中的位置。默认底部居中，也可以传 [Alignment.Center] 放到屏幕中心。
 * @param navHost 业务侧的 NavHost 内容。
 */
@Composable
fun HomeFloatingNavigationScreen(
    navigationBarDestinations: NavigationBarDestinations,
    navigationBarModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    defaultIndex: Int = NavigationBarDestinations.DEFAULT_INDEX,
    navigationBarStyle: FloatingNavigationBarStyle = FloatingNavigationBarDefaults.style(),
    navigationBarAlignment: Alignment = Alignment.BottomCenter,
    navHost: @Composable (NavHostController, String, Modifier) -> Unit,
) {
    // 底栏和 NavHost 必须共用同一个 NavController，才能保证点击 tab 后页面和高亮状态一致。
    val navController = rememberNavController()
    val startDestination = navigationBarDestinations.startDestination(defaultIndex)

    Scaffold(modifier = modifier) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 业务内容仍然消费 Scaffold 的系统栏 padding，但不会为悬浮底栏额外让位。
            navHost(navController, startDestination, Modifier.padding(contentPadding))
            FloatingNavigationBar(
                controller = navController,
                destinations = navigationBarDestinations,
                modifier = navigationBarModifier
                    .align(navigationBarAlignment),
                style = navigationBarStyle,
            )
        }
    }
}

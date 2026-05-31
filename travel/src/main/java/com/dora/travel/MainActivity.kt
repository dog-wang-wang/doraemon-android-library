package com.dora.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dora.travel.model.Router
import com.dora.travel.ui.theme.DoraTheme
import com.dora.travel.ui.trip.CreateTripScreen
import com.dora.travel.ui.trip.NodeEditorScreen
import com.dora.travel.ui.trip.ProfilePlaceholderScreen
import com.dora.travel.ui.trip.TripTimelineRoute
import com.doraemon.foundation_ui_compose.model.NavigationBarDestinations
import com.doraemon.foundation_ui_compose.ui.HomeScreen

class MainActivity : ComponentActivity() {
    private val navigationBarDestinations = NavigationBarDestinations(
        listOf(
            NavigationBarDestinations.ItemDestination(
                label = { Text(getString(R.string.travel_tab_activity)) },
                icon = { Icon(Icons.Default.Route, contentDescription = null) },
                routerName = Router.HOME,
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text(getString(R.string.travel_tab_map)) },
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                routerName = Router.MINE,
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text(getString(R.string.travel_tab_library)) },
                icon = { Icon(Icons.AutoMirrored.Filled.Article, contentDescription = null) },
                routerName = Router.HISTORY,
            ),
            NavigationBarDestinations.ItemDestination(
                label = { Text(getString(R.string.travel_tab_profile)) },
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                routerName = Router.MINE,
            ),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navigationBarCorner = RoundedCornerShape(999.dp)
            /*
             * 这里仍然使用 Material3 的 [NavigationBar] / [NavigationBarItem]，但把外层形态收敛成
             * 设计稿里的“玻璃胶囊”视觉：圆角、半透明容器、弱边界和轻微发光阴影。颜色全部来自
             * [MaterialTheme.colorScheme]，因此 travel 模块只需要配置自己的主题，导航组件就会自动跟随。
             */
            val navigationModifier = Modifier
                .padding(horizontal = 16.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = navigationBarCorner,
                    ambientColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f),
                    spotColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.12f),
                )
                .clip(navigationBarCorner)
            DoraTheme {
                // 首页
                HomeScreen(navigationBarDestinations, navigationModifier) { navController, startDestination, modifier ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = modifier,
                    ) {
                        composable(Router.HOME) { TripTimelineRoute() }
                        composable(Router.MAP) { NodeEditorScreen() }
                        composable(Router.HISTORY) { CreateTripScreen() }
                        composable(Router.MINE) { ProfilePlaceholderScreen() }
                    }
                }
            }
        }
    }
}
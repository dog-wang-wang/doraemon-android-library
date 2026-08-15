package com.dora.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
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
import com.doraemon.foundation_ui_compose.ui.navigation.floating.FloatingNavDefaults
import com.doraemon.foundation_ui_compose.ui.navigation.floating.FloatingNavScaffold
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations

private object TravelNavigationBarTokens {
    val HorizontalPadding = 16.dp
    val BottomPadding = 24.dp
    val ShadowElevation = 20.dp

    const val ShadowAmbientAlpha = 0.14f
    const val ShadowSpotAlpha = 0.12f
    const val LiquidTintAlpha = 0.70f
    const val LiquidHighlightAlpha = 0.18f
    const val LiquidRefractionAlpha = 0.16f
    const val LiquidInnerShadowAlpha = 0.14f
    const val LiquidBorderAlpha = 0.54f
    const val IndicatorAnimationDurationMillis = 280
}

class MainActivity : ComponentActivity() {
    private val destinations = NavDestinations(
        listOf(
            NavDestinations.Item(
                label = { Text(getString(R.string.travel_tab_activity)) },
                icon = { Icon(Icons.Default.Route, contentDescription = null) },
                route = Router.HOME,
            ),
            NavDestinations.Item(
                label = { Text(getString(R.string.travel_tab_map)) },
                icon = { Icon(Icons.Default.Map, contentDescription = null) },
                route = Router.MAP,
            ),
            NavDestinations.Item(
                label = { Text(getString(R.string.travel_tab_library)) },
                icon = { Icon(Icons.AutoMirrored.Filled.Article, contentDescription = null) },
                route = Router.HISTORY,
            ),
            NavDestinations.Item(
                label = { Text(getString(R.string.travel_tab_profile)) },
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                route = Router.MINE,
            ),
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoraTheme {
                val navigationBarCorner = FloatingNavDefaults.capsuleShape()
                /*
                 * 浮动导航栏的外层形态保持“玻璃胶囊”：圆角、半透明容器、弱边界和轻微发光阴影。
                 * 背景由策略对象负责，后续可以按页面切换为玻璃质感、纯色或图片。
                 */
                val navigationModifier = Modifier
                    .padding(
                        start = TravelNavigationBarTokens.HorizontalPadding,
                        end = TravelNavigationBarTokens.HorizontalPadding,
                        bottom = TravelNavigationBarTokens.BottomPadding,
                    )
                    .shadow(
                        elevation = TravelNavigationBarTokens.ShadowElevation,
                        shape = navigationBarCorner,
                        ambientColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = TravelNavigationBarTokens.ShadowAmbientAlpha,
                        ),
                        spotColor = MaterialTheme.colorScheme.secondary.copy(
                            alpha = TravelNavigationBarTokens.ShadowSpotAlpha,
                        ),
                    )
                val style = FloatingNavDefaults.style(
                    background = FloatingNavDefaults.liquidGlass(
                        tint = MaterialTheme.colorScheme.surface.copy(alpha = TravelNavigationBarTokens.LiquidTintAlpha),
                        highlightColor = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = TravelNavigationBarTokens.LiquidHighlightAlpha,
                        ),
                        refractionColor = MaterialTheme.colorScheme.primary.copy(
                            alpha = TravelNavigationBarTokens.LiquidRefractionAlpha,
                        ),
                        innerShadowColor = MaterialTheme.colorScheme.scrim.copy(
                            alpha = TravelNavigationBarTokens.LiquidInnerShadowAlpha,
                        ),
                        border = FloatingNavDefaults.backgroundBorder(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = TravelNavigationBarTokens.LiquidBorderAlpha,
                            ),
                        ),
                    ),
                    indicatorStrategy = FloatingNavDefaults.iconAndLabelIndicator(),
                    iconStrategy = FloatingNavDefaults.replaceIconOnSelected(),
                    indicatorAnimationSpec = FloatingNavDefaults.tweenIndicatorAnimation(
                        durationMillis = TravelNavigationBarTokens.IndicatorAnimationDurationMillis,
                    ),
                )
                // 这里使用浮动底栏版本，和你当前的 Figma 视觉更贴近。
                FloatingNavScaffold(
                    destinations,
                    barModifier = navigationModifier,
                    style = style,
                ) { navController, startDestination, modifier ->
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

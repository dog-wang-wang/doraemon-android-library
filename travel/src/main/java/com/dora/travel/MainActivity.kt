package com.dora.travel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import com.dora.travel.ui.theme.DoraTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dora.travel.ui.theme.composable.*
import com.doraemon.foundation_ui_compose.model.NavigationBarDestinations
import com.doraemon.foundation_ui_compose.ui.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoraTheme {
                // 1. 定义导航目标数据
                val destinations = NavigationBarDestinations(
                    listOf(
                        NavigationBarDestinations.ItemDestination(
                            label = { Text("探索") },
                            icon = { Icon(Icons.Default.Explore, contentDescription = null) },
                            routerName = "explore"
                        ),
                        NavigationBarDestinations.ItemDestination(
                            label = { Text("订单") },
                            icon = { Icon(Icons.AutoMirrored.Filled.ListAlt, contentDescription = null) },
                            routerName = "orders"
                        ),
                        NavigationBarDestinations.ItemDestination(
                            label = { Text("我的") },
                            icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                            routerName = "profile"
                        )
                    )
                )

                // 2. 传入通用的 HomeScreen，并自定义 navHost 内容
                HomeScreen(
                    navigationBarDestinations = destinations,
                    navHost = { navController, startDestination, modifier ->
                        NavHost(
                            navController = navController,
                            startDestination = startDestination,
                            modifier = modifier
                        ) {
                            composable("explore") { ExploreScreen() }
                            composable("orders") { OrdersScreen() }
                            composable("profile") { ProfileScreen() }
                        }
                    }
                )
            }
        }
    }
}

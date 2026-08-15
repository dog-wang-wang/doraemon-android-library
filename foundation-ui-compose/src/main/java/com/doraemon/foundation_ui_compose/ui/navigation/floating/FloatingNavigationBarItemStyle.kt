package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavigationBarDestinations

/**
 * 单个 tab 的内容颜色。
 *
 * 图标和文字分开配置，是因为很多设计会让选中图标更亮，但文字保持更克制。
 */
@Immutable
data class FloatingNavigationBarItemColors(
    val iconColor: Color,
    val labelColor: Color,
)

/**
 * tab 内容颜色策略。
 *
 * 它负责根据 selected 返回图标和文字颜色。颜色变化是最轻量的选中态表达：
 * 同一套图标不变，只通过 tint 区分选中和未选中。
 */
fun interface FloatingNavigationBarItemColorStrategy {
    @Composable
    fun colors(selected: Boolean): FloatingNavigationBarItemColors
}

/**
 * tab 图标渲染策略。
 *
 * 它负责决定“选中时怎么画图标”。常见两种做法：
 * 1. 颜色策略：始终画同一个图标，只改变 tint。
 * 2. 替换图标策略：选中时优先画 selectedIcon，例如 outline 图标切换成 filled 图标。
 */
fun interface FloatingNavigationBarIconStrategy {
    @Composable
    fun icon(
        itemDestination: NavigationBarDestinations.ItemDestination,
        selected: Boolean,
        iconColor: Color,
    )
}

/**
 * 默认 tab 内容颜色策略。
 */
@Composable
fun defaultNavigationBarItemColors(selected: Boolean): FloatingNavigationBarItemColors =
    FloatingNavigationBarItemColors(
        iconColor = if (selected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        labelColor = if (selected) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
    )

internal fun tintIconStrategy(): FloatingNavigationBarIconStrategy =
    FloatingNavigationBarIconStrategy { itemDestination, _, iconColor ->
        CompositionLocalProvider(LocalContentColor provides iconColor) {
            itemDestination.icon()
        }
    }

internal fun replaceIconStrategy(): FloatingNavigationBarIconStrategy =
    FloatingNavigationBarIconStrategy { itemDestination, selected, iconColor ->
        val icon = if (selected) {
            itemDestination.selectedIcon ?: itemDestination.icon
        } else {
            itemDestination.icon
        }

        CompositionLocalProvider(LocalContentColor provides iconColor) {
            icon()
        }
    }

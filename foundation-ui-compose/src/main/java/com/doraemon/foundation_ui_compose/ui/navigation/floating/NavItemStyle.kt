package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations

/**
 * 单个 tab 的内容颜色。
 *
 * 图标和文字分开配置，是因为很多设计会让选中图标更亮，但文字保持更克制。
 *
 * @param iconColor 当前 tab 图标颜色。
 * @param labelColor 当前 tab 文字颜色。
 */
@Immutable
data class NavItemColors(
    val iconColor: Color,
    val labelColor: Color,
)

/**
 * tab 内容颜色策略。
 *
 * 它负责根据 selected 返回图标和文字颜色。颜色变化是最轻量的选中态表达：
 * 同一套图标不变，只通过 tint 区分选中和未选中。
 *
 * 这是策略模式：基础库只规定“需要一个根据 selected 返回颜色的对象”，
 * 具体颜色来自 MaterialTheme、品牌色，还是业务动态状态，都由调用方决定。
 */
fun interface NavItemColorStrategy {
    /**
     * 根据当前 tab 是否选中返回一组颜色。
     */
    @Composable
    fun colors(selected: Boolean): NavItemColors
}

/**
 * tab 图标渲染策略。
 *
 * 它负责决定“选中时怎么画图标”。常见两种做法：
 * 1. 颜色策略：始终画同一个图标，只改变 tint。
 * 2. 替换图标策略：选中时优先画 selectedIcon，例如 outline 图标切换成 filled 图标。
 */
fun interface NavIconStrategy {
    /**
     * 绘制当前 tab 的图标。
     *
     * @param itemDestination tab 数据，包含默认图标和可选选中图标。
     * @param selected 当前 tab 是否被选中。
     * @param iconColor 颜色策略计算出的图标颜色。
     */
    @Composable
    fun icon(
        itemDestination: NavDestinations.Item,
        selected: Boolean,
        iconColor: Color,
    )
}

/**
 * 默认 tab 内容颜色策略。
 *
 * 颜色来自 [MaterialTheme]，所以它会自然适配深色/浅色主题。
 */
@Composable
fun defaultNavItemColors(selected: Boolean): NavItemColors =
    NavItemColors(
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

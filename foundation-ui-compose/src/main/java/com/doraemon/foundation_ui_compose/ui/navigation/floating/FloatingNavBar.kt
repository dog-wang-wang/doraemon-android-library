package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.doraemon.foundation_ui_compose.common.Constants
import com.doraemon.foundation_ui_compose.ui.navigation.core.navigateTopLevel
import com.doraemon.foundation_ui_compose.ui.navigation.core.startDestination
import com.doraemon.foundation_ui_compose.ui.navigation.model.NavDestinations
import com.doraemon.log.debug

private object FloatingNavTokens {
    const val NOT_FOUND_INDEX = -1
    const val FIRST_INDEX = 0
    const val SELECTED_INDICATOR_ALPHA = 0.18f
    const val ITEM_WEIGHT = 1f
    const val DOUBLE_SPACE_FACTOR = 2

    val IconContainerHeight: Dp = 30.dp
    val IconContainerMinWidth: Dp = 44.dp
    val IconSize: Dp = 24.dp
}

/**
 * iOS 风格的自定义浮动底部导航。
 *
 * 这个组件没有使用 Material3 的 NavigationBarItem，因为悬浮胶囊导航通常需要更自由的形状、
 * 背景和选中态。它只复用 Compose 基础布局能力，自行控制每个 tab 的视觉。
 *
 * @param controller 页面导航控制器。它同时提供当前路由和点击后的跳转能力。
 * @param destinations 导航项配置。
 * @param modifier 作用于整个导航栏容器。
 * @param style 导航栏视觉配置。
 */
@Composable
fun FloatingNavBar(
    controller: NavController,
    destinations: NavDestinations,
    modifier: Modifier = Modifier,
    style: FloatingNavStyle = FloatingNavDefaults.style(),
) {
    // 从导航栈读取当前路由，作为底栏选中态的唯一来源。
    // 这样即使页面不是通过点击底栏进入，高亮状态也能正确同步。
    val navBackStackEntry by controller.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    // 首次组合时 currentRoute 可能为 null，用默认路由兜底。
    // 没有元素的时候会被抛异常
    val fallbackRoute = destinations.startDestination(NavDestinations.DEFAULT_INDEX)
    val selectedRoute = currentRoute ?: fallbackRoute
    val selectedIndex = destinations.items
        .indexOfFirst { it.route == selectedRoute }
        .takeIf { it != FloatingNavTokens.NOT_FOUND_INDEX } ?: FloatingNavTokens.FIRST_INDEX
    val indicatorLimited = style.indicatorStrategy.requiresLabel &&
        style.itemContentMode is NavItemContentMode.IconOnly
    val resolvedIndicatorStrategy = resolveIndicatorStrategy(style)

    if (indicatorLimited) {
        LaunchedEffect(style.indicatorStrategy, style.itemContentMode) {
            debug(
                Constants.LOG_DEBUG_NAVIGATION_INDICATOR_LIMITED,
                Constants.LOG_TAG_NAVIGATION,
            )
        }
    }

    Box(modifier = modifier) {
        FloatingNavContent(
            controller = controller,
            destinations = destinations,
            selectedRoute = selectedRoute,
            selectedIndex = selectedIndex,
            resolvedIndicatorStrategy = resolvedIndicatorStrategy,
            style = style,
        )
    }
}

/**
 * 导航栏真正可见的胶囊实体。
 *
 * [FloatingNavBar] 的外层 modifier 可能包含 align、业务外边距、shadow 等“摆放用”修饰符。
 * 那些修饰符不应该参与胶囊本体的高度和裁剪，否则外部留白会被一起裁进圆角里，
 * 造成导航栏高度被拉大、圆角看起来变形。
 *
 * 因此这里单独创建内层实体：只有它拥有固定高度、最大宽度和 [style.shape] 裁剪。
 */
@Composable
private fun FloatingNavContent(
    controller: NavController,
    destinations: NavDestinations,
    selectedRoute: String,
    selectedIndex: Int,
    resolvedIndicatorStrategy: NavIndicatorStrategy,
    style: FloatingNavStyle,
) {
    BoxWithConstraints(
        modifier = Modifier
            .widthIn(max = style.maxWidth)
            .fillMaxWidth()
            .height(style.height)
            .clip(style.shape),
    ) {
        // 背景先画，tab 内容后画；这就是常见的“背景层 + 内容层”结构。
        style.background.run {
            draw(
                modifier = Modifier.matchParentSize(),
                shape = style.shape,
            )
        }
        if (destinations.items.isNotEmpty()) {
            // 每个 tab 等分可用宽度；指示器策略再基于单个 tab 宽度计算自己的显示范围。
            val itemWidth = (
                maxWidth - style.horizontalPadding * FloatingNavTokens.DOUBLE_SPACE_FACTOR
                ) / destinations.items.size
            val indicatorBounds = resolvedIndicatorStrategy.bounds(
                itemWidth = itemWidth,
                navigationBarHeight = style.height,
                verticalPadding = style.verticalPadding,
            )
            // 计算指示器偏移量
            val indicatorOffset = style.horizontalPadding +
                itemWidth * selectedIndex.toFloat() +
                (itemWidth - indicatorBounds.width) / 2
            val animatedIndicatorOffset by animateDpAsState(
                targetValue = indicatorOffset,
                animationSpec = style.indicatorAnimationSpec,
                label = "FloatingNavigationIndicatorOffset",
            )

            // 共享选中指示器只画一次，并根据 selectedIndex 在 tab 之间滑动。
            // 指示器大小由 style.indicatorStrategy 决定，可以只包图标，也可以包图标和文字。
            Box(
                modifier = Modifier
                    .offset(x = animatedIndicatorOffset, y = indicatorBounds.y)
                    .width(indicatorBounds.width)
                    .height(indicatorBounds.height)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(
                            alpha = FloatingNavTokens.SELECTED_INDICATOR_ALPHA,
                        ),
                        shape = FloatingNavDefaults.capsuleShape(),
                    ),
            )
        }
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = style.horizontalPadding, vertical = style.verticalPadding),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            destinations.items.forEach { itemDestination ->
                val selected = itemDestination.route == selectedRoute

                FloatingNavItem(
                    selected = selected,
                    onClick = {
                        // 每个 tab 都按 top-level 方式跳转，避免重复堆栈并尽量恢复 tab 内状态。
                        controller.navigateTopLevel(itemDestination.route)
                    },
                    itemDestination = itemDestination,
                    itemColorStrategy = style.itemColorStrategy,
                    iconStrategy = style.iconStrategy,
                    itemContentMode = style.itemContentMode,
                    iconLabelSpacing = style.iconLabelSpacing,
                    clickIndication = style.tabClickIndication,
                    modifier = Modifier.weight(FloatingNavTokens.ITEM_WEIGHT),
                )
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    itemDestination: NavDestinations.Item,
    itemColorStrategy: NavItemColorStrategy,
    iconStrategy: NavIconStrategy,
    itemContentMode: NavItemContentMode,
    iconLabelSpacing: Dp,
    clickIndication: Indication?,
    modifier: Modifier = Modifier,
) {
    val targetColors = itemColorStrategy.colors(selected)
    // animateColorAsState 会在 selected 改变时自动补间颜色；真正的选中背景由父组件统一滑动。
    val iconColor by animateColorAsState(
        targetValue = targetColors.iconColor,
        label = "FloatingNavigationIconColor",
    )
    val labelColor by animateColorAsState(
        targetValue = targetColors.labelColor,
        label = "FloatingNavigationLabelColor",
    )
    val interactionSource = remember { MutableInteractionSource() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Role.Tab 会给无障碍服务一个更准确的语义：这是一个 tab，而不是普通按钮。
            .clickable(
                interactionSource = interactionSource,
                indication = clickIndication,
                role = Role.Tab,
                onClick = onClick,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // 图标区域使用固定高度和最小宽度，避免不同图标或选中态造成布局跳动。
        Box(
            modifier = Modifier
                .height(FloatingNavTokens.IconContainerHeight)
                .widthIn(min = FloatingNavTokens.IconContainerMinWidth)
                .background(Color.Transparent, FloatingNavDefaults.capsuleShape()),
            contentAlignment = Alignment.Center,
        ) {
            Box(modifier = Modifier.size(FloatingNavTokens.IconSize)) {
                iconStrategy.icon(
                    itemDestination = itemDestination,
                    selected = selected,
                    iconColor = iconColor,
                )
            }
        }
        if (itemContentMode is NavItemContentMode.IconAndLabel) {
            Spacer(modifier = Modifier.height(iconLabelSpacing))
            // LocalContentColor 让调用方的 Text/Icon 能自然继承当前选中态颜色。
            CompositionLocalProvider(LocalContentColor provides labelColor) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                ) {
                    itemDestination.label()
                }
            }
        }
    }
}

private fun resolveIndicatorStrategy(
    style: FloatingNavStyle,
): NavIndicatorStrategy {
    val indicatorRequiresLabel = style.indicatorStrategy.requiresLabel
    val labelHidden = style.itemContentMode is NavItemContentMode.IconOnly

    return if (indicatorRequiresLabel && labelHidden) {
        FloatingNavDefaults.iconIndicator()
    } else {
        style.indicatorStrategy
    }
}

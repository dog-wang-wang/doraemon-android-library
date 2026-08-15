package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 浮动导航栏视觉配置。
 *
 * 样式对象把“导航逻辑”和“视觉参数”分开：
 * [FloatingNavBar] 只关心如何根据路由选中 tab，而高度、圆角、背景、内边距都交给这个对象。
 *
 * @param background 背景策略。调用方可以传毛玻璃、纯色、图片等不同实现。
 * @param shape 导航栏整体形状，默认是胶囊形。
 * @param height 导航栏高度。
 * @param maxWidth 导航栏最大宽度，平板或横屏时避免底栏被拉得过宽。
 * @param horizontalPadding 导航栏内部左右留白。
 * @param verticalPadding 导航栏内部上下留白。
 * @param itemContentMode tab 内容显示模式。可以只显示图标，也可以显示图标和文字。
 * @param iconLabelSpacing 图标和文字之间的距离，仅在 [NavItemContentMode.IconAndLabel] 下生效。
 * @param indicatorStrategy 选中指示器布局策略。它决定滑动色块是只包裹图标，还是包裹图标和文字。
 * @param indicatorAnimationSpec 选中指示器移动动画。默认是弹簧动画，调用方也可以传 tween 控制时长。
 * @param itemColorStrategy tab 内容颜色策略。它决定选中/未选中时图标和文字分别是什么颜色。
 * @param iconStrategy tab 图标策略。它决定选中时是只改变图标颜色，还是替换为 selectedIcon。
 * @param tabClickIndication tab 点击反馈。默认关闭，避免和自定义滑动指示器同时出现两套视觉反馈；
 * 需要 Material ripple 时可以由业务传入 ripple indication。
 */
@Immutable
data class FloatingNavStyle(
    val background: NavBackground,
    val shape: Shape,
    val height: Dp,
    val maxWidth: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemContentMode: NavItemContentMode,
    val iconLabelSpacing: Dp,
    val indicatorStrategy: NavIndicatorStrategy,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val itemColorStrategy: NavItemColorStrategy,
    val iconStrategy: NavIconStrategy,
    val tabClickIndication: Indication?,
)

/**
 * tab 内容显示模式。
 *
 * 使用 sealed hierarchy 而不是 enum，是为了让模式以后可以自然扩展参数。
 * 例如后续如果需要“只显示选中项文字”或“文字淡入淡出”等模式，可以新增 data object 或 data class，
 * 同时保留 when/is 判断的类型安全和穷尽性。
 */
sealed interface NavItemContentMode {
    /**
     * 只显示图标。适合空间较窄或图标语义足够明确的导航栏。
     */
    data object IconOnly : NavItemContentMode

    /**
     * 同时显示图标和文字。适合需要更清晰表达导航含义的场景。
     */
    data object IconAndLabel : NavItemContentMode
}

/**
 * 选中指示器的矩形区域。
 *
 * @param width 指示器宽度。
 * @param height 指示器高度。
 * @param y 指示器在导航栏内容区域中的纵向偏移。
 */
@Immutable
data class NavIndicatorBounds(
    val width: Dp,
    val height: Dp,
    val y: Dp,
)

/**
 * 选中指示器布局策略。
 *
 * 它只负责计算“滑动色块应该多大、放多高”，不负责绘制颜色，也不负责导航跳转。
 * 这样后续要增加下划线、整格背景、只包文字等样式时，只需要新增策略。
 */
interface NavIndicatorStrategy {
    /**
     * 当前指示器策略是否依赖文字区域。
     *
     * 例如“包裹图标和文字”必须在 item 同时显示文字时才有意义。
     * 当调用方选择了只显示图标时，导航栏会自动降级为只包图标的策略。
     */
    val requiresLabel: Boolean
        get() = false

    fun bounds(
        itemWidth: Dp,
        navigationBarHeight: Dp,
        verticalPadding: Dp,
    ): NavIndicatorBounds
}

/**
 * 浮动导航栏默认配置。
 *
 * 默认值集中在这里，调用方只覆盖自己关心的部分即可。
 * 这比在多个组件里散落 magic number 更稳定，也更方便以后统一调整设计规范。
 */
object FloatingNavDefaults {
    private const val CAPSULE_SHAPE_PERCENT = 50
    private const val DEFAULT_SPRING_DAMPING_RATIO = 0.82f
    private const val DEFAULT_SPRING_STIFFNESS = 520f
    private const val DEFAULT_TWEEN_DURATION_MILLIS = 260
    private const val DEFAULT_TWEEN_DELAY_MILLIS = 0
    private const val DEFAULT_ICON_AND_LABEL_INDICATOR_WIDTH_FRACTION = 0.82f
    private const val DEFAULT_FROSTED_BACKGROUND_ALPHA = 0.24f
    private const val DEFAULT_FROSTED_TINT_ALPHA = 0.18f
    private const val DEFAULT_FROSTED_HIGHLIGHT_ALPHA = 0.30f
    private const val DEFAULT_FROSTED_WARM_GLOW_ALPHA = 0.12f
    private const val DEFAULT_FROSTED_FRESH_GLOW_ALPHA = 0.14f
    private const val DEFAULT_FROSTED_SUN_GLOW_ALPHA = 0.10f
    private const val DEFAULT_FROSTED_BACKDROP_BLUR_RADIUS_DP = 24
    private const val DEFAULT_FROSTED_NOISE_FACTOR = 0.08f
    private const val MIN_FRACTION = 0f
    private const val MAX_FRACTION = 1f

    val DefaultHeight: Dp = 64.dp
    val DefaultMaxWidth: Dp = 560.dp
    val DefaultHorizontalPadding: Dp = 10.dp
    val DefaultVerticalPadding: Dp = 6.dp
    val DefaultIconLabelSpacing: Dp = 4.dp
    val DefaultIconIndicatorWidth: Dp = 56.dp
    val DefaultIconIndicatorHeight: Dp = 34.dp
    val DefaultBackgroundLayerBlurRadius: Dp = 14.dp

    /**
     * 标准胶囊形状。
     *
     * percent = 50 表示圆角半径始终取自身宽高较短边的一半。
     * 这比写 999.dp 更准确：不依赖组件实际尺寸，也不会在极端尺寸下出现畸形圆角。
     */
    fun capsuleShape(): Shape = RoundedCornerShape(percent = CAPSULE_SHAPE_PERCENT)

    /**
     * 默认颜色策略。
     *
     * 选中态和未选中态都会分别返回图标色与文字色。
     */
    fun itemColors(): NavItemColorStrategy =
        NavItemColorStrategy { selected ->
            defaultNavItemColors(selected)
        }

    /**
     * 只通过颜色表达图标选中态。
     */
    fun tintIcon(): NavIconStrategy =
        NavIconStrategy { itemDestination, _, iconColor ->
            // LocalContentColor 是 Compose 图标/文字常用的颜色传递方式。
            // 业务传入的 Icon 如果没有手动指定 tint，就会自动读取这里的颜色。
            CompositionLocalProvider(LocalContentColor provides iconColor) {
                itemDestination.icon()
            }
        }

    /**
     * 选中时优先使用 [NavDestinations.Item.selectedIcon]。
     *
     * 如果某个 tab 没有提供 selectedIcon，会自动退回默认 icon，保证调用方可以按需逐个配置。
     */
    fun replaceIconOnSelected(): NavIconStrategy =
        NavIconStrategy { itemDestination, selected, iconColor ->
            // selectedIcon 是可选的：业务可以只给部分 tab 准备 filled 图标。
            // 没给 selectedIcon 时回退默认 icon，避免调用方为了兼容策略被迫传重复图标。
            val icon = if (selected) {
                itemDestination.selectedIcon ?: itemDestination.icon
            } else {
                itemDestination.icon
            }

            CompositionLocalProvider(LocalContentColor provides iconColor) {
                icon()
            }
        }

    /**
     * 默认背景边框。
     *
     * 边框仍然属于 background strategy 的配置，这个入口只是把常用 API 收到 Defaults 里，
     * 让业务侧不需要额外 import 顶层的 [navBackgroundBorder]。
     */
    fun backgroundBorder(): NavBackgroundBorder = navBackgroundBorder()

    /**
     * 使用自定义颜色创建背景边框。
     *
     * 适合在 Composable 中接入 [androidx.compose.material3.MaterialTheme] 的动态颜色。
     */
    fun backgroundBorder(color: Color): NavBackgroundBorder = navBackgroundBorder(color = color)

    /**
     * 使用自定义宽度和颜色创建背景边框。
     */
    fun backgroundBorder(width: Dp, color: Color): NavBackgroundBorder = navBackgroundBorder(
        width = width,
        color = color,
    )

    /**
     * 常用的毛玻璃背景。
     *
     * 这个入口会启用 Haze backdrop blur，并把毛玻璃底色、tint 和噪点映射给 Haze。
     * Defaults 入口的意义是收敛调用侧 import：业务大多数时候只需要记住
     * `FloatingNavDefaults.frostedGlass()`，不必直接依赖底层顶层函数。
     */
    fun frostedGlass(
        backgroundColor: Color = Color.White.copy(alpha = DEFAULT_FROSTED_BACKGROUND_ALPHA),
        tintColor: Color = Color.White.copy(alpha = DEFAULT_FROSTED_TINT_ALPHA),
        highlightColor: Color = Color.White.copy(alpha = DEFAULT_FROSTED_HIGHLIGHT_ALPHA),
        noiseFactor: Float = DEFAULT_FROSTED_NOISE_FACTOR,
        warmGlowColor: Color = Color(0xFFFFD8CF).copy(alpha = DEFAULT_FROSTED_WARM_GLOW_ALPHA),
        freshGlowColor: Color = Color(0xFFDDF8EA).copy(alpha = DEFAULT_FROSTED_FRESH_GLOW_ALPHA),
        sunGlowColor: Color = Color(0xFFFFF1B8).copy(alpha = DEFAULT_FROSTED_SUN_GLOW_ALPHA),
        border: NavBackgroundBorder = backgroundBorder(),
        backdropBlurRadius: Dp = DEFAULT_FROSTED_BACKDROP_BLUR_RADIUS_DP.dp,
        layerBlurRadius: Dp = DefaultBackgroundLayerBlurRadius,
    ): NavBackground = frostedGlassBackground(
        backgroundColor = backgroundColor,
        tintColor = tintColor,
        highlightColor = highlightColor,
        noiseFactor = noiseFactor,
        warmGlowColor = warmGlowColor,
        freshGlowColor = freshGlowColor,
        sunGlowColor = sunGlowColor,
        border = border,
        backdropBlurRadius = backdropBlurRadius,
        layerBlurRadius = layerBlurRadius,
    )

    /**
     * 默认弹簧动画。
     *
     * 弹簧动画没有固定时长，它根据距离和阻尼自然收敛，适合有弹性的浮动导航手感。
     */
    fun springIndicatorAnimation(
        dampingRatio: Float = DEFAULT_SPRING_DAMPING_RATIO,
        stiffness: Float = DEFAULT_SPRING_STIFFNESS,
    ): AnimationSpec<Dp> = spring(
        dampingRatio = dampingRatio,
        stiffness = stiffness,
    )

    /**
     * 固定时长动画。
     *
     * 如果设计稿明确要求“多少毫秒滑过去”，使用这个工厂比 spring 更直观。
     */
    fun tweenIndicatorAnimation(
        durationMillis: Int = DEFAULT_TWEEN_DURATION_MILLIS,
        delayMillis: Int = DEFAULT_TWEEN_DELAY_MILLIS,
    ): AnimationSpec<Dp> = tween(
        durationMillis = durationMillis,
        delayMillis = delayMillis,
    )

    /**
     * 指示器只包裹图标。
     *
     * 这是比较轻的 iOS 风格：图标背后有一个会滑动的胶囊光影，文字只跟随变色。
     *
     * @param width 指示器宽度，会被限制在单个 tab 宽度以内。
     * @param height 指示器高度。
     */
    fun iconIndicator(
        width: Dp = DefaultIconIndicatorWidth,
        height: Dp = DefaultIconIndicatorHeight,
    ): NavIndicatorStrategy = object : NavIndicatorStrategy {
        override fun bounds(
            itemWidth: Dp,
            navigationBarHeight: Dp,
            verticalPadding: Dp,
        ): NavIndicatorBounds = NavIndicatorBounds(
            width = width.coerceAtMost(itemWidth),
            height = height,
            y = (navigationBarHeight - height) / 2,
        )
    }

    /**
     * 指示器包裹图标和文字。
     *
     * 适合希望选中态更强的导航栏：滑动胶囊会覆盖整个 tab 的主要内容区域。
     *
     * @param widthFraction 指示器占单个 tab 宽度的比例，会被限制在 0f..1f。
     */
    fun iconAndLabelIndicator(
        widthFraction: Float = DEFAULT_ICON_AND_LABEL_INDICATOR_WIDTH_FRACTION,
    ): NavIndicatorStrategy = object : NavIndicatorStrategy {
        override val requiresLabel: Boolean = true

        override fun bounds(
            itemWidth: Dp,
            navigationBarHeight: Dp,
            verticalPadding: Dp,
        ): NavIndicatorBounds = NavIndicatorBounds(
            width = itemWidth * widthFraction.coerceIn(MIN_FRACTION, MAX_FRACTION),
            height = navigationBarHeight - verticalPadding * 2,
            y = verticalPadding,
        )
    }

    /**
     * 创建默认样式。
     *
     * 示例：
     * ```
     * val style = FloatingNavDefaults.style(
     *     background = solidBackground(Color.Black)
     * )
     * ```
     *
     * 这里暴露的是稳定的组合入口。调用方可以只覆盖一个参数，
     * 其余值继续沿用基础库默认规范。
     */
    fun style(
        background: NavBackground = frostedGlass(),
        shape: Shape = capsuleShape(),
        height: Dp = DefaultHeight,
        maxWidth: Dp = DefaultMaxWidth,
        horizontalPadding: Dp = DefaultHorizontalPadding,
        verticalPadding: Dp = DefaultVerticalPadding,
        itemContentMode: NavItemContentMode = NavItemContentMode.IconAndLabel,
        iconLabelSpacing: Dp = DefaultIconLabelSpacing,
        indicatorStrategy: NavIndicatorStrategy = iconIndicator(),
        indicatorAnimationSpec: AnimationSpec<Dp> = springIndicatorAnimation(),
        itemColorStrategy: NavItemColorStrategy = itemColors(),
        iconStrategy: NavIconStrategy = tintIcon(),
        // 悬浮导航已经有滑动指示器作为主反馈，默认关闭 ripple，避免点击时出现重复动画。
        tabClickIndication: Indication? = null,
    ): FloatingNavStyle = FloatingNavStyle(
        background = background,
        shape = shape,
        height = height,
        maxWidth = maxWidth,
        horizontalPadding = horizontalPadding,
        verticalPadding = verticalPadding,
        itemContentMode = itemContentMode,
        iconLabelSpacing = iconLabelSpacing,
        indicatorStrategy = indicatorStrategy,
        indicatorAnimationSpec = indicatorAnimationSpec,
        itemColorStrategy = itemColorStrategy,
        iconStrategy = iconStrategy,
        tabClickIndication = tabClickIndication,
    )
}

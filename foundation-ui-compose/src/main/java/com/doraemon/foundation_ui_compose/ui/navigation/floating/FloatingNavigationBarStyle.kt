package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Indication
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 浮动导航栏视觉配置。
 *
 * 样式对象把“导航逻辑”和“视觉参数”分开：
 * [FloatingNavigationBar] 只关心如何根据路由选中 tab，而高度、圆角、背景、内边距都交给这个对象。
 *
 * @param background 背景策略。调用方可以传纯色、图片、玻璃质感等不同实现。
 * @param shape 导航栏整体形状，默认是胶囊形。
 * @param height 导航栏高度。
 * @param maxWidth 导航栏最大宽度，平板或横屏时避免底栏被拉得过宽。
 * @param horizontalPadding 导航栏内部左右留白。
 * @param verticalPadding 导航栏内部上下留白。
 * @param itemContentMode tab 内容显示模式。可以只显示图标，也可以显示图标和文字。
 * @param iconLabelSpacing 图标和文字之间的距离，仅在 [FloatingNavigationBarItemContentMode.IconAndLabel] 下生效。
 * @param indicatorStrategy 选中指示器布局策略。它决定滑动色块是只包裹图标，还是包裹图标和文字。
 * @param indicatorAnimationSpec 选中指示器移动动画。默认是弹簧动画，调用方也可以传 tween 控制时长。
 * @param itemColorStrategy tab 内容颜色策略。它决定选中/未选中时图标和文字分别是什么颜色。
 * @param iconStrategy tab 图标策略。它决定选中时是只改变图标颜色，还是替换为 selectedIcon。
 * @param tabClickIndication tab 点击反馈。默认关闭，避免和自定义滑动指示器同时出现两套视觉反馈；
 * 需要 Material ripple 时可以由业务传入 ripple indication。
 */
@Immutable
data class FloatingNavigationBarStyle(
    val background: FloatingNavigationBarBackground,
    val shape: Shape,
    val height: Dp,
    val maxWidth: Dp,
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val itemContentMode: FloatingNavigationBarItemContentMode,
    val iconLabelSpacing: Dp,
    val indicatorStrategy: FloatingNavigationBarIndicatorStrategy,
    val indicatorAnimationSpec: AnimationSpec<Dp>,
    val itemColorStrategy: FloatingNavigationBarItemColorStrategy,
    val iconStrategy: FloatingNavigationBarIconStrategy,
    val tabClickIndication: Indication?,
)

/**
 * tab 内容显示模式。
 */
enum class FloatingNavigationBarItemContentMode {
    /**
     * 只显示图标。适合空间较窄或图标语义足够明确的导航栏。
     */
    IconOnly,

    /**
     * 同时显示图标和文字。适合需要更清晰表达导航含义的场景。
     */
    IconAndLabel,
}

/**
 * 选中指示器的矩形区域。
 *
 * @param width 指示器宽度。
 * @param height 指示器高度。
 * @param y 指示器在导航栏内容区域中的纵向偏移。
 */
@Immutable
data class FloatingNavigationBarIndicatorBounds(
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
interface FloatingNavigationBarIndicatorStrategy {
    /**
     * 当前指示器策略是否依赖文字区域。
     *
     * 例如“包裹图标和文字”必须在 item 同时显示文字时才有意义。
     */
    val requiresLabel: Boolean
        get() = false

    fun bounds(
        itemWidth: Dp,
        navigationBarHeight: Dp,
        verticalPadding: Dp,
    ): FloatingNavigationBarIndicatorBounds
}

/**
 * 浮动导航栏默认配置。
 *
 * 默认值集中在这里，调用方只覆盖自己关心的部分即可。
 * 这比在多个组件里散落 magic number 更稳定，也更方便以后统一调整设计规范。
 */
object FloatingNavigationBarDefaults {
    private const val CAPSULE_SHAPE_PERCENT = 50
    private const val DEFAULT_SPRING_DAMPING_RATIO = 0.82f
    private const val DEFAULT_SPRING_STIFFNESS = 520f
    private const val DEFAULT_TWEEN_DURATION_MILLIS = 260
    private const val DEFAULT_TWEEN_DELAY_MILLIS = 0
    private const val DEFAULT_ICON_AND_LABEL_INDICATOR_WIDTH_FRACTION = 0.82f
    private const val MIN_FRACTION = 0f
    private const val MAX_FRACTION = 1f

    val DefaultHeight: Dp = 64.dp
    val DefaultMaxWidth: Dp = 560.dp
    val DefaultHorizontalPadding: Dp = 10.dp
    val DefaultVerticalPadding: Dp = 6.dp
    val DefaultIconLabelSpacing: Dp = 4.dp
    val DefaultIconIndicatorWidth: Dp = 56.dp
    val DefaultIconIndicatorHeight: Dp = 34.dp

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
    fun itemColors(): FloatingNavigationBarItemColorStrategy =
        FloatingNavigationBarItemColorStrategy { selected ->
            defaultNavigationBarItemColors(selected)
        }

    /**
     * 只通过颜色表达图标选中态。
     */
    fun tintIcon(): FloatingNavigationBarIconStrategy = tintIconStrategy()

    /**
     * 选中时优先使用 ItemDestination.selectedIcon。
     *
     * 如果某个 tab 没有提供 selectedIcon，会自动退回默认 icon，保证调用方可以按需逐个配置。
     */
    fun replaceIconOnSelected(): FloatingNavigationBarIconStrategy = replaceIconStrategy()

    /**
     * 默认弹簧动画。
     *
     * 弹簧动画没有固定时长，它根据距离和阻尼自然收敛，适合“液态玻璃”这种带一点弹性的手感。
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
     */
    fun iconIndicator(
        width: Dp = DefaultIconIndicatorWidth,
        height: Dp = DefaultIconIndicatorHeight,
    ): FloatingNavigationBarIndicatorStrategy = object : FloatingNavigationBarIndicatorStrategy {
        override fun bounds(
            itemWidth: Dp,
            navigationBarHeight: Dp,
            verticalPadding: Dp,
        ): FloatingNavigationBarIndicatorBounds = FloatingNavigationBarIndicatorBounds(
            width = width.coerceAtMost(itemWidth),
            height = height,
            y = (navigationBarHeight - height) / 2,
        )
    }

    /**
     * 指示器包裹图标和文字。
     *
     * 适合希望选中态更强的导航栏：滑动胶囊会覆盖整个 tab 的主要内容区域。
     */
    fun iconAndLabelIndicator(
        widthFraction: Float = DEFAULT_ICON_AND_LABEL_INDICATOR_WIDTH_FRACTION,
    ): FloatingNavigationBarIndicatorStrategy = object : FloatingNavigationBarIndicatorStrategy {
        override val requiresLabel: Boolean = true

        override fun bounds(
            itemWidth: Dp,
            navigationBarHeight: Dp,
            verticalPadding: Dp,
        ): FloatingNavigationBarIndicatorBounds = FloatingNavigationBarIndicatorBounds(
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
     * val style = FloatingNavigationBarDefaults.style(
     *     background = solidNavigationBarBackground(Color.Black)
     * )
     * ```
     */
    fun style(
        background: FloatingNavigationBarBackground = frostedGlassNavigationBarBackground(),
        shape: Shape = capsuleShape(),
        height: Dp = DefaultHeight,
        maxWidth: Dp = DefaultMaxWidth,
        horizontalPadding: Dp = DefaultHorizontalPadding,
        verticalPadding: Dp = DefaultVerticalPadding,
        itemContentMode: FloatingNavigationBarItemContentMode = FloatingNavigationBarItemContentMode.IconAndLabel,
        iconLabelSpacing: Dp = DefaultIconLabelSpacing,
        indicatorStrategy: FloatingNavigationBarIndicatorStrategy = iconIndicator(),
        indicatorAnimationSpec: AnimationSpec<Dp> = springIndicatorAnimation(),
        itemColorStrategy: FloatingNavigationBarItemColorStrategy = itemColors(),
        iconStrategy: FloatingNavigationBarIconStrategy = tintIcon(),
        // 悬浮导航已经有滑动指示器作为主反馈，默认关闭 ripple，避免点击时出现重复动画。
        tabClickIndication: Indication? = null,
    ): FloatingNavigationBarStyle = FloatingNavigationBarStyle(
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

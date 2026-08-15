package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private object NavBackgroundTokens {
    const val BORDER_ALPHA = 0.34f
    const val NO_BLUR_RADIUS_DP = 0

    const val DEFAULT_IMAGE_OVERLAY_ALPHA = 0.28f

    const val LIGHT_GLASS_TINT_ALPHA = 0.18f

    const val FROSTED_TINT_ALPHA = 0.82f
    const val FROSTED_HIGHLIGHT_ALPHA = 0.22f
    const val FROSTED_NOISE_ALPHA = 0.14f
    const val FROSTED_NOISE_STEP_DP = 4
    const val FROSTED_NOISE_RADIUS_DP = 0.55f
    const val FROSTED_NOISE_MIN_ALPHA_FACTOR = 0.24f
    const val FROSTED_NOISE_ALPHA_FACTOR_STEP = 0.05f
    const val FROSTED_NOISE_PATTERN_ROWS = 31
    const val FROSTED_NOISE_PATTERN_COLUMNS = 17
    const val FROSTED_NOISE_PATTERN_BUCKETS = 5

    const val LIQUID_TINT_ALPHA = 0.64f
    const val LIQUID_HIGHLIGHT_ALPHA = 0.48f
    const val LIQUID_REFRACTION_ALPHA = 0.18f
    const val LIQUID_INNER_SHADOW_ALPHA = 0.10f
    const val LIQUID_TOP_HIGHLIGHT_ALPHA_FACTOR = 0.62f
    const val LIQUID_UPPER_FLOW_ALPHA_FACTOR = 0.46f
    const val LIQUID_LOWER_FLOW_ALPHA_FACTOR = 0.72f
    const val LIQUID_UPPER_FLOW_STROKE_DP = 1.4f
    const val LIQUID_LOWER_FLOW_STROKE_DP = 2.2f

    val LiquidDefaultRefractionColor: Color = Color(0xFFB8C3FF)
    val DefaultBorderWidth: Dp = 1.dp
}

private object LiquidGlassGeometry {
    const val TOP_HIGHLIGHT_CENTER_X = 0.16f
    const val TOP_HIGHLIGHT_CENTER_Y = 0.02f
    const val TOP_HIGHLIGHT_RADIUS_WIDTH = 0.46f
    const val TOP_HIGHLIGHT_LEFT_X = -0.08f
    const val TOP_HIGHLIGHT_TOP_Y = -0.55f
    const val TOP_HIGHLIGHT_WIDTH = 0.74f
    const val TOP_HIGHLIGHT_HEIGHT = 1.15f

    const val REFRACTION_CENTER_X = 0.78f
    const val REFRACTION_CENTER_Y = 0.82f
    const val REFRACTION_RADIUS_WIDTH = 0.42f
    const val REFRACTION_LEFT_X = 0.36f
    const val REFRACTION_TOP_Y = 0.02f
    const val REFRACTION_WIDTH = 0.78f
    const val REFRACTION_HEIGHT = 1.2f

    const val UPPER_FLOW_START_X = 0.08f
    const val UPPER_FLOW_START_Y = 0.36f
    const val UPPER_FLOW_CONTROL_1_X = 0.24f
    const val UPPER_FLOW_CONTROL_1_Y = 0.12f
    const val UPPER_FLOW_CONTROL_2_X = 0.46f
    const val UPPER_FLOW_CONTROL_2_Y = 0.50f
    const val UPPER_FLOW_END_1_X = 0.66f
    const val UPPER_FLOW_END_1_Y = 0.28f
    const val UPPER_FLOW_CONTROL_3_X = 0.78f
    const val UPPER_FLOW_CONTROL_3_Y = 0.16f
    const val UPPER_FLOW_CONTROL_4_X = 0.88f
    const val UPPER_FLOW_CONTROL_4_Y = 0.22f
    const val UPPER_FLOW_END_2_X = 0.96f
    const val UPPER_FLOW_END_2_Y = 0.14f

    const val LOWER_FLOW_START_X = 0.02f
    const val LOWER_FLOW_START_Y = 0.72f
    const val LOWER_FLOW_CONTROL_1_X = 0.24f
    const val LOWER_FLOW_CONTROL_1_Y = 0.88f
    const val LOWER_FLOW_CONTROL_2_X = 0.36f
    const val LOWER_FLOW_CONTROL_2_Y = 0.54f
    const val LOWER_FLOW_END_1_X = 0.58f
    const val LOWER_FLOW_END_1_Y = 0.70f
    const val LOWER_FLOW_CONTROL_3_X = 0.72f
    const val LOWER_FLOW_CONTROL_3_Y = 0.80f
    const val LOWER_FLOW_CONTROL_4_X = 0.88f
    const val LOWER_FLOW_CONTROL_4_Y = 0.62f
    const val LOWER_FLOW_END_2_Y = 0.74f
}

/**
 * 浮动导航栏背景边框配置。
 *
 * 边框是背景材质的边缘收口：纯色、玻璃、图片背景都可能需要不同的边缘颜色和强度。
 * 因此它跟随 background strategy，而不是作为 [FloatingNavStyle] 的平级参数。
 *
 * @param width 边框宽度。设置为 0.dp 可以关闭边框。
 * @param color 边框颜色。设置为 [Color.Transparent] 可以关闭边框颜色。
 */
@Immutable
data class NavBackgroundBorder(
    val width: Dp,
    val color: Color,
)

/**
 * 创建默认背景边框。
 *
 * 默认使用浅色低透明度边框，让玻璃背景在深浅页面上都有基本边缘。
 * 业务需要适配主题色时，可以在 Composable 中传入基于 MaterialTheme 的颜色。
 */
fun navBackgroundBorder(
    width: Dp = NavBackgroundTokens.DefaultBorderWidth,
    color: Color = Color.White.copy(alpha = NavBackgroundTokens.BORDER_ALPHA),
): NavBackgroundBorder = NavBackgroundBorder(
    width = width,
    color = color,
)

private fun Modifier.navBackgroundBorderModifier(
    border: NavBackgroundBorder,
    shape: Shape,
): Modifier = if (border.width > 0.dp && border.color.alpha > 0f) {
    this.border(
        width = border.width,
        color = border.color,
        shape = shape,
    )
} else {
    this
}

/**
 * 浮动导航栏背景策略。
 *
 * 这是一个很小的策略模式接口：
 * - [FloatingNavBar] 负责摆放 tab、处理点击和选中态。
 * - [NavBackground] 只负责“背景怎么画”。
 *
 * 这样新增背景效果时不用修改导航栏本体，只需要新增一个返回
 * [NavBackground] 的函数即可，例如纯色、图片、渐变、毛玻璃质感。
 */
fun interface NavBackground {
    /**
     * 绘制背景内容。
     *
     * @param modifier 导航栏传进来的尺寸修饰符，通常应该完整使用，保证背景铺满导航栏。
     * @param shape 导航栏整体形状，背景应该使用这个 shape 裁剪，避免内容溢出胶囊边缘。
     */
    @Composable
    fun BoxScope.draw(modifier: Modifier, shape: Shape)
}

/**
 * 半透明背景，适合模拟轻量玻璃质感。
 *
 * 它不会真正模糊背后的内容，只是通过透明色让底部内容隐约透出。
 */
fun translucentBackground(
    color: Color,
    border: NavBackgroundBorder = navBackgroundBorder(color = Color.Transparent),
): NavBackground = NavBackground { modifier, shape ->
    Box(
        modifier = modifier
            .background(color, shape)
            .navBackgroundBorderModifier(border, shape),
    )
}

/**
 * 毛玻璃背景。
 *
 * 毛玻璃和普通透明背景的区别在于：它不是单纯降低 alpha，而是让背景变得“雾化、发白、带细颗粒”。
 * 这里用四层叠加来模拟这种观感：
 * 1. 较高不透明度的 tint，让背后内容只隐约透出。
 * 2. 斜向高光，让玻璃有柔和反光。
 * 3. 细颗粒噪点，让表面有磨砂感。
 * 4. 细边框，让胶囊边缘在复杂背景上仍然清晰。
 *
 * 说明：这个策略负责做稳定的“磨砂玻璃视觉”。如果要真正采样并模糊导航栏背后的实时内容，
 * 需要调用方提供已模糊的背景内容，或在更高层接入平台级 RenderEffect/backdrop blur。
 *
 * @param tint 雾化底色。alpha 通常应高于普通透明背景，才会有“毛玻璃”而不是“透明塑料”的感觉。
 * @param highlightColor 斜向高光颜色。
 * @param noiseColor 磨砂颗粒颜色。
 * @param border 背景边框配置。
 * @param blurRadius 作用于毛玻璃背景层自身的柔化半径，不会自动模糊背后的页面内容。
 */
fun frostedGlassBackground(
    tint: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_TINT_ALPHA),
    highlightColor: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_HIGHLIGHT_ALPHA),
    noiseColor: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_NOISE_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
    blurRadius: Dp = NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp,
): NavBackground = NavBackground { modifier, shape ->
    Box(
        modifier = modifier
            .clip(shape)
            .then(if (blurRadius > NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp) Modifier.blur(blurRadius) else Modifier)
            .background(tint, shape)
            .navBackgroundBorderModifier(border, shape),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(highlightColor, Color.Transparent),
                    start = Offset.Zero,
                    end = Offset(size.width, size.height),
                ),
            )

            val step = NavBackgroundTokens.FROSTED_NOISE_STEP_DP.dp.toPx()
            val radius = NavBackgroundTokens.FROSTED_NOISE_RADIUS_DP.dp.toPx()
            var y = step / 2
            var row = 0
            while (y < size.height) {
                var x = step / 2
                var column = 0
                while (x < size.width) {
                    val noiseAlpha = NavBackgroundTokens.FROSTED_NOISE_MIN_ALPHA_FACTOR +
                        ((row * NavBackgroundTokens.FROSTED_NOISE_PATTERN_ROWS +
                            column * NavBackgroundTokens.FROSTED_NOISE_PATTERN_COLUMNS) %
                            NavBackgroundTokens.FROSTED_NOISE_PATTERN_BUCKETS) *
                        NavBackgroundTokens.FROSTED_NOISE_ALPHA_FACTOR_STEP
                    drawCircle(
                        color = noiseColor.copy(alpha = noiseColor.alpha * noiseAlpha),
                        radius = radius,
                        center = Offset(x, y),
                    )
                    x += step
                    column += 1
                }
                y += step
                row += 1
            }
        }
    }
}

/**
 * 液态玻璃背景。
 *
 * 液态玻璃和毛玻璃的重点不同：
 * - 毛玻璃强调“雾化、磨砂、看不清背后”。
 * - 液态玻璃强调“润、透、边缘有折射感，高光像液体一样顺着表面流动”。
 *
 * 这里仍然是一个稳定的 Compose 绘制策略，不会真正改变背后内容的光学折射。
 * 它通过多层叠加模拟 iPhone 风格的液态玻璃观感：
 * 1. 半透明但偏厚的 tint，保留玻璃的透感。
 * 2. 顶部和边缘高光，制造凸起的玻璃边缘。
 * 3. 柔和的流动光带，模拟液体表面折射。
 * 4. 很弱的内部阴影，避免玻璃变成一块纯白贴片。
 *
 * @param tint 玻璃主体颜色。
 * @param highlightColor 高光颜色。
 * @param refractionColor 折射光带颜色，通常可以带一点主题色。
 * @param innerShadowColor 内部暗部颜色，用来增加厚度。
 * @param border 背景边框配置。
 * @param blurRadius 作用于背景层自身的柔化半径，不会自动模糊背后的页面内容。
 */
fun liquidGlassBackground(
    tint: Color = Color.White.copy(alpha = NavBackgroundTokens.LIQUID_TINT_ALPHA),
    highlightColor: Color = Color.White.copy(alpha = NavBackgroundTokens.LIQUID_HIGHLIGHT_ALPHA),
    refractionColor: Color = NavBackgroundTokens.LiquidDefaultRefractionColor.copy(
        alpha = NavBackgroundTokens.LIQUID_REFRACTION_ALPHA,
    ),
    innerShadowColor: Color = Color.Black.copy(alpha = NavBackgroundTokens.LIQUID_INNER_SHADOW_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
    blurRadius: Dp = NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp,
): NavBackground = NavBackground { modifier, shape ->
    Box(
        modifier = modifier
            .clip(shape)
            .then(if (blurRadius > NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp) Modifier.blur(blurRadius) else Modifier)
            .background(tint, shape)
            .navBackgroundBorderModifier(border, shape),
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        highlightColor.copy(
                            alpha = highlightColor.alpha *
                                NavBackgroundTokens.LIQUID_TOP_HIGHLIGHT_ALPHA_FACTOR,
                        ),
                        Color.Transparent,
                        innerShadowColor,
                    ),
                ),
            )

            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(highlightColor, Color.Transparent),
                    center = Offset(
                        size.width * LiquidGlassGeometry.TOP_HIGHLIGHT_CENTER_X,
                        size.height * LiquidGlassGeometry.TOP_HIGHLIGHT_CENTER_Y,
                    ),
                    radius = size.width * LiquidGlassGeometry.TOP_HIGHLIGHT_RADIUS_WIDTH,
                ),
                topLeft = Offset(
                    size.width * LiquidGlassGeometry.TOP_HIGHLIGHT_LEFT_X,
                    size.height * LiquidGlassGeometry.TOP_HIGHLIGHT_TOP_Y,
                ),
                size = Size(
                    size.width * LiquidGlassGeometry.TOP_HIGHLIGHT_WIDTH,
                    size.height * LiquidGlassGeometry.TOP_HIGHLIGHT_HEIGHT,
                ),
            )

            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(refractionColor, Color.Transparent),
                    center = Offset(
                        size.width * LiquidGlassGeometry.REFRACTION_CENTER_X,
                        size.height * LiquidGlassGeometry.REFRACTION_CENTER_Y,
                    ),
                    radius = size.width * LiquidGlassGeometry.REFRACTION_RADIUS_WIDTH,
                ),
                topLeft = Offset(
                    size.width * LiquidGlassGeometry.REFRACTION_LEFT_X,
                    size.height * LiquidGlassGeometry.REFRACTION_TOP_Y,
                ),
                size = Size(
                    size.width * LiquidGlassGeometry.REFRACTION_WIDTH,
                    size.height * LiquidGlassGeometry.REFRACTION_HEIGHT,
                ),
            )

            val upperFlow = Path().apply {
                moveTo(
                    size.width * LiquidGlassGeometry.UPPER_FLOW_START_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_START_Y,
                )
                cubicTo(
                    size.width * LiquidGlassGeometry.UPPER_FLOW_CONTROL_1_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_CONTROL_1_Y,
                    size.width * LiquidGlassGeometry.UPPER_FLOW_CONTROL_2_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_CONTROL_2_Y,
                    size.width * LiquidGlassGeometry.UPPER_FLOW_END_1_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_END_1_Y,
                )
                cubicTo(
                    size.width * LiquidGlassGeometry.UPPER_FLOW_CONTROL_3_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_CONTROL_3_Y,
                    size.width * LiquidGlassGeometry.UPPER_FLOW_CONTROL_4_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_CONTROL_4_Y,
                    size.width * LiquidGlassGeometry.UPPER_FLOW_END_2_X,
                    size.height * LiquidGlassGeometry.UPPER_FLOW_END_2_Y,
                )
            }
            drawPath(
                path = upperFlow,
                color = highlightColor.copy(
                    alpha = highlightColor.alpha *
                        NavBackgroundTokens.LIQUID_UPPER_FLOW_ALPHA_FACTOR,
                ),
                style = Stroke(
                    width = NavBackgroundTokens.LIQUID_UPPER_FLOW_STROKE_DP.dp.toPx(),
                    cap = StrokeCap.Round,
                ),
            )

            val lowerFlow = Path().apply {
                moveTo(
                    size.width * LiquidGlassGeometry.LOWER_FLOW_START_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_START_Y,
                )
                cubicTo(
                    size.width * LiquidGlassGeometry.LOWER_FLOW_CONTROL_1_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_CONTROL_1_Y,
                    size.width * LiquidGlassGeometry.LOWER_FLOW_CONTROL_2_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_CONTROL_2_Y,
                    size.width * LiquidGlassGeometry.LOWER_FLOW_END_1_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_END_1_Y,
                )
                cubicTo(
                    size.width * LiquidGlassGeometry.LOWER_FLOW_CONTROL_3_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_CONTROL_3_Y,
                    size.width * LiquidGlassGeometry.LOWER_FLOW_CONTROL_4_X,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_CONTROL_4_Y,
                    size.width,
                    size.height * LiquidGlassGeometry.LOWER_FLOW_END_2_Y,
                )
            }
            drawPath(
                path = lowerFlow,
                color = refractionColor.copy(
                    alpha = refractionColor.alpha *
                        NavBackgroundTokens.LIQUID_LOWER_FLOW_ALPHA_FACTOR,
                ),
                style = Stroke(
                    width = NavBackgroundTokens.LIQUID_LOWER_FLOW_STROKE_DP.dp.toPx(),
                    cap = StrokeCap.Round,
                ),
            )
        }
    }
}

/**
 * 纯色背景。
 *
 * 适合强调稳定和可读性的场景，例如管理后台、工具类 App、对比度要求更高的主题。
 */
fun solidBackground(
    color: Color,
    border: NavBackgroundBorder = navBackgroundBorder(color = Color.Transparent),
): NavBackground = translucentBackground(color, border)

/**
 * 图片背景。
 *
 * 适合品牌化或活动页场景。为了保证图标和文字可读，默认会叠加一层半透明遮罩。
 *
 * @param painter 背景图片。
 * @param contentScale 图片裁剪方式，默认铺满导航栏。
 * @param overlayColor 图片上方的遮罩色，用于压暗或统一图片色调。
 * @param border 背景边框配置。
 */
fun imageBackground(
    painter: Painter,
    contentScale: ContentScale = ContentScale.Crop,
    overlayColor: Color = Color.Black.copy(alpha = NavBackgroundTokens.DEFAULT_IMAGE_OVERLAY_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
): NavBackground = NavBackground { modifier, shape ->
    Box(modifier = modifier.clip(shape)) {
        // 图片层放在最底部，负责提供纹理或品牌视觉。
        Image(
            painter = painter,
            contentDescription = null,
            contentScale = contentScale,
            modifier = Modifier.matchParentSize(),
        )
        // 遮罩层放在图片上方，避免背景太花导致图标和文案看不清。
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(overlayColor),
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .navBackgroundBorderModifier(border, shape),
        )
    }
}

/**
 * 轻量透明玻璃背景。
 *
 * 这里的“玻璃”只由三部分组成：裁剪形状、半透明 tint、细边框。
 * 它更像透明玻璃片，不是磨砂毛玻璃；需要磨砂朦胧效果时优先使用
 * [frostedGlassBackground]。
 *
 * [blurRadius] 只作用于背景层自身；若需要真正模糊导航栏背后的内容，应由调用方提供
 * 已经处理好的背景内容或另行接入平台级 RenderEffect。
 *
 * 这个限制写清楚很重要：Compose 的 Modifier.blur 默认不会像 iOS 那样采样并模糊背后的内容。
 */
fun glassBackground(
    tint: Color = Color.White.copy(alpha = NavBackgroundTokens.LIGHT_GLASS_TINT_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
    blurRadius: Dp = NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp,
): NavBackground = NavBackground { modifier, shape ->
    Box(
        modifier = modifier
            .clip(shape)
            .then(if (blurRadius > NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp) Modifier.blur(blurRadius) else Modifier)
            .background(tint, shape)
            .navBackgroundBorderModifier(border, shape),
    )
}

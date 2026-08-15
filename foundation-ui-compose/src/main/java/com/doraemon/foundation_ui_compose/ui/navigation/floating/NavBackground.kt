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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private object NavBackgroundTokens {
    // 这个文件里的数值都集中在 tokens 中，避免绘制逻辑里散落 magic number。
    // 如果后续视觉规范需要调整，只优先改这里或 Defaults 暴露的参数。
    const val BORDER_ALPHA = 0.34f
    const val NO_BLUR_RADIUS_DP = 0

    const val DEFAULT_IMAGE_OVERLAY_ALPHA = 0.28f

    const val FROSTED_BACKGROUND_ALPHA = 0.24f
    const val FROSTED_TINT_ALPHA = 0.18f
    const val FROSTED_HIGHLIGHT_ALPHA = 0.30f
    const val FROSTED_BACKDROP_BLUR_RADIUS_DP = 24
    const val FROSTED_LAYER_BLUR_RADIUS_DP = 14
    const val FROSTED_LUMINOSITY_ALPHA = 0.14f
    const val FROSTED_TOP_GLOW_ALPHA_FACTOR = 0.72f
    const val FROSTED_WARM_GLOW_ALPHA = 0.12f
    const val FROSTED_FRESH_GLOW_ALPHA = 0.14f
    const val FROSTED_SUN_GLOW_ALPHA = 0.10f
    const val FROSTED_HAZE_NOISE_FACTOR = 0.08f

    val DefaultBorderWidth: Dp = 1.dp
}

private object FrostedGlassGeometry {
    // 这些比例都是相对于导航栏当前尺寸计算的。
    // 使用比例而不是固定 dp，可以让同一套毛玻璃高光在不同宽度的胶囊上保持相似构图。
    const val TOP_GLOW_CENTER_X = 0.26f
    const val TOP_GLOW_CENTER_Y = -0.18f
    const val TOP_GLOW_RADIUS_WIDTH = 0.62f
    const val TOP_GLOW_LEFT_X = -0.18f
    const val TOP_GLOW_TOP_Y = -0.72f
    const val TOP_GLOW_WIDTH = 0.86f
    const val TOP_GLOW_HEIGHT = 1.65f

    const val LOWER_HAZE_CENTER_X = 0.78f
    const val LOWER_HAZE_CENTER_Y = 0.92f
    const val LOWER_HAZE_RADIUS_WIDTH = 0.44f
    const val LOWER_HAZE_LEFT_X = 0.32f
    const val LOWER_HAZE_TOP_Y = -0.08f
    const val LOWER_HAZE_WIDTH = 0.84f
    const val LOWER_HAZE_HEIGHT = 1.28f
    const val LOWER_HAZE_ALPHA_FACTOR = 0.58f
}

private object FrostedAmbientGeometry {
    // ambient glow 是很淡的环境色，用来避免纯白毛玻璃太平。
    // 它不是品牌色，也不承担交互状态，只负责材质层的微弱冷暖变化。
    const val WARM_GLOW_CENTER_X = 0.20f
    const val WARM_GLOW_CENTER_Y = 0.82f
    const val WARM_GLOW_RADIUS_WIDTH = 0.38f

    const val FRESH_GLOW_CENTER_X = 0.52f
    const val FRESH_GLOW_CENTER_Y = 0.98f
    const val FRESH_GLOW_RADIUS_WIDTH = 0.46f

    const val SUN_GLOW_CENTER_X = 0.90f
    const val SUN_GLOW_CENTER_Y = 0.86f
    const val SUN_GLOW_RADIUS_WIDTH = 0.36f
}

/**
 * 浮动导航栏背景边框配置。
 *
 * 边框是背景材质的边缘收口：纯色、玻璃、图片背景都可能需要不同的边缘颜色和强度。
 * 因此它跟随 background strategy，而不是作为 [FloatingNavStyle] 的平级参数。
 */
@Immutable
data class NavBackgroundBorder(
    val width: Dp,
    val color: Color,
)

/**
 * 需要真实 backdrop blur 时传给底层模糊库的配置。
 *
 * 这层配置是背景策略和 Haze 之间的映射对象：
 * - 背景策略用 Compose 基础类型描述自己想要的视觉。
 * - 导航栏内部再把这些值转换成 Haze 的 `backgroundColor`、`tints`、`blurRadius` 和 `noiseFactor`。
 *
 * 这样公开 API 不泄露第三方库类型；以后替换 Haze，也只需要改导航栏内部映射。
 *
 * @param blurRadius 真实背景模糊半径。
 * @param backgroundColor Haze 的 fallback 背景色，也是没有 backdrop source 时的兜底底色。
 * @param tintColor Haze 叠加在模糊内容上的 tint 色。
 * @param noiseFactor Haze 自带噪点强度，用来形成轻微磨砂颗粒感。
 */
@Immutable
data class NavBackdropEffect(
    val blurRadius: Dp,
    val backgroundColor: Color,
    val tintColor: Color,
    val noiseFactor: Float,
)

/**
 * 创建默认背景边框。
 *
 * 默认边框是低透明度白色，适合毛玻璃在深色或彩色内容上保持边界。
 *
 * @param width 边框宽度。传入 `0.dp` 可以关闭边框。
 * @param color 边框颜色。传入 [Color.Transparent] 也可以关闭边框。
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
    border(
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
 * [FloatingNavScaffold] 会把业务内容注册成 Haze 的 backdrop source。
 * 当 [backdropEffect] 不为空时，导航栏胶囊会先按 effect 配置真实模糊背后的页面内容，
 * 然后再调用 [draw] 叠加高光、环境光和边框。
 */
interface NavBackground {
    /**
     * 真实背景模糊配置。为空表示不需要对背后内容做 backdrop blur。
     */
    val backdropEffect: NavBackdropEffect?
        get() = null

    /**
     * 绘制背景材质层。这里不负责导航逻辑，只负责视觉。
     */
    @Composable
    fun BoxScope.draw(modifier: Modifier, shape: Shape)
}

/**
 * 毛玻璃背景。
 *
 * 这套实现吸收了 Haze 这类开源库的核心思路：
 * 1. 先对导航栏背后的页面内容做真实 backdrop blur。
 * 2. 让 Haze 叠加浅白 tint 和噪点，形成雾化、磨砂表面。
 * 3. 再由背景策略补充柔光和高光。
 * 4. 最后用边框收住胶囊边缘。
 *
 * 注意：真正的 backdrop blur 不在这个函数里直接执行。
 * 这个函数只通过 [NavBackdropEffect] 声明“我需要什么样的真实背景模糊”，
 * 具体模糊由 [FloatingNavBar] 根据 [FloatingNavScaffold] 提供的 Haze source 来完成。
 *
 * @param backgroundColor Haze 的 fallback 背景色，也是没有 backdrop source 时的兜底底色。
 * @param tintColor Haze 的雾化 tint。alpha 越高，背后内容越朦胧。
 * @param highlightColor 顶部和斜向高光颜色，用来模拟玻璃反光。
 * @param noiseFactor Haze 自带噪点强度，用来提供磨砂颗粒感。
 * @param warmGlowColor 暖色环境光，默认很弱，只负责增加材质层次。
 * @param freshGlowColor 冷绿色环境光，默认很弱，只负责增加材质层次。
 * @param sunGlowColor 暖黄色环境光，默认很弱，只负责增加材质层次。
 * @param border 背景边框。边框属于背景材质的一部分，所以跟随 background strategy。
 * @param backdropBlurRadius 真实背景模糊半径，交给 Haze 使用。
 * @param layerBlurRadius 背景材质层自身的柔化半径，不等于背后内容模糊半径。
 */
fun frostedGlassBackground(
    backgroundColor: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_BACKGROUND_ALPHA),
    tintColor: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_TINT_ALPHA),
    highlightColor: Color = Color.White.copy(alpha = NavBackgroundTokens.FROSTED_HIGHLIGHT_ALPHA),
    noiseFactor: Float = NavBackgroundTokens.FROSTED_HAZE_NOISE_FACTOR,
    warmGlowColor: Color = Color(0xFFFFD8CF).copy(alpha = NavBackgroundTokens.FROSTED_WARM_GLOW_ALPHA),
    freshGlowColor: Color = Color(0xFFDDF8EA).copy(alpha = NavBackgroundTokens.FROSTED_FRESH_GLOW_ALPHA),
    sunGlowColor: Color = Color(0xFFFFF1B8).copy(alpha = NavBackgroundTokens.FROSTED_SUN_GLOW_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
    backdropBlurRadius: Dp = NavBackgroundTokens.FROSTED_BACKDROP_BLUR_RADIUS_DP.dp,
    layerBlurRadius: Dp = NavBackgroundTokens.FROSTED_LAYER_BLUR_RADIUS_DP.dp,
): NavBackground = object : NavBackground {
    override val backdropEffect: NavBackdropEffect = NavBackdropEffect(
        blurRadius = backdropBlurRadius.coerceAtLeast(NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp),
        backgroundColor = backgroundColor,
        tintColor = tintColor,
        noiseFactor = noiseFactor.coerceAtLeast(0f),
    )

    @Composable
    override fun BoxScope.draw(modifier: Modifier, shape: Shape) {
        Box(
            modifier = modifier
                .clip(shape)
                .navBackgroundBorderModifier(border, shape),
        ) {
            // 这一层画大面积柔光和高光，再对它自身做轻微 blur。
            // 它不采样背后内容，只负责让材质表面更像磨砂玻璃，而不是一块纯白半透明板。
            Canvas(
                modifier = Modifier
                    .matchParentSize()
                    .then(
                        if (layerBlurRadius > NavBackgroundTokens.NO_BLUR_RADIUS_DP.dp) {
                            Modifier.blur(layerBlurRadius)
                        } else {
                            Modifier
                        },
                    ),
            ) {
                // 左下角暖光，让白色玻璃不会显得完全平面。
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(warmGlowColor, Color.Transparent),
                        center = Offset(
                            size.width * FrostedAmbientGeometry.WARM_GLOW_CENTER_X,
                            size.height * FrostedAmbientGeometry.WARM_GLOW_CENTER_Y,
                        ),
                        radius = size.width * FrostedAmbientGeometry.WARM_GLOW_RADIUS_WIDTH,
                    ),
                )
                // 底部偏冷的柔光，和暖光叠在一起形成很弱的色彩层次。
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(freshGlowColor, Color.Transparent),
                        center = Offset(
                            size.width * FrostedAmbientGeometry.FRESH_GLOW_CENTER_X,
                            size.height * FrostedAmbientGeometry.FRESH_GLOW_CENTER_Y,
                        ),
                        radius = size.width * FrostedAmbientGeometry.FRESH_GLOW_RADIUS_WIDTH,
                    ),
                )
                // 右下角暖黄高光，模拟环境光在胶囊边缘的轻微反射。
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(sunGlowColor, Color.Transparent),
                        center = Offset(
                            size.width * FrostedAmbientGeometry.SUN_GLOW_CENTER_X,
                            size.height * FrostedAmbientGeometry.SUN_GLOW_CENTER_Y,
                        ),
                        radius = size.width * FrostedAmbientGeometry.SUN_GLOW_RADIUS_WIDTH,
                    ),
                )
                // 竖向白色雾化层，让背后内容被模糊后进一步“发白、发雾”。
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            highlightColor.copy(
                                alpha = highlightColor.alpha * NavBackgroundTokens.FROSTED_TOP_GLOW_ALPHA_FACTOR,
                            ),
                            Color.White.copy(alpha = NavBackgroundTokens.FROSTED_LUMINOSITY_ALPHA),
                            Color.Transparent,
                        ),
                    ),
                )
                // 顶部大面积高光，模拟玻璃上沿的柔和反光。
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(highlightColor, Color.Transparent),
                        center = Offset(
                            size.width * FrostedGlassGeometry.TOP_GLOW_CENTER_X,
                            size.height * FrostedGlassGeometry.TOP_GLOW_CENTER_Y,
                        ),
                        radius = size.width * FrostedGlassGeometry.TOP_GLOW_RADIUS_WIDTH,
                    ),
                    topLeft = Offset(
                        size.width * FrostedGlassGeometry.TOP_GLOW_LEFT_X,
                        size.height * FrostedGlassGeometry.TOP_GLOW_TOP_Y,
                    ),
                    size = Size(
                        size.width * FrostedGlassGeometry.TOP_GLOW_WIDTH,
                        size.height * FrostedGlassGeometry.TOP_GLOW_HEIGHT,
                    ),
                )
                // 底部 haze 让材质有厚度，避免下半部分过于透明。
                drawOval(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            tintColor.copy(
                                alpha = tintColor.alpha * FrostedGlassGeometry.LOWER_HAZE_ALPHA_FACTOR,
                            ),
                            Color.Transparent,
                        ),
                        center = Offset(
                            size.width * FrostedGlassGeometry.LOWER_HAZE_CENTER_X,
                            size.height * FrostedGlassGeometry.LOWER_HAZE_CENTER_Y,
                        ),
                        radius = size.width * FrostedGlassGeometry.LOWER_HAZE_RADIUS_WIDTH,
                    ),
                    topLeft = Offset(
                        size.width * FrostedGlassGeometry.LOWER_HAZE_LEFT_X,
                        size.height * FrostedGlassGeometry.LOWER_HAZE_TOP_Y,
                    ),
                    size = Size(
                        size.width * FrostedGlassGeometry.LOWER_HAZE_WIDTH,
                        size.height * FrostedGlassGeometry.LOWER_HAZE_HEIGHT,
                    ),
                )
            }
        }
    }
}

/**
 * 纯色背景。
 *
 * 纯色策略只绘制一个稳定的实体色块，不启用 backdrop blur。
 * 它适合品牌色、强对比主题，或任何希望导航栏像实体控件一样清晰可读的页面。
 */
fun solidBackground(
    color: Color,
    border: NavBackgroundBorder = navBackgroundBorder(color = Color.Transparent),
): NavBackground = object : NavBackground {
    @Composable
    override fun BoxScope.draw(modifier: Modifier, shape: Shape) {
        Box(
            modifier = modifier
                .background(color, shape)
                .navBackgroundBorderModifier(border, shape),
        )
    }
}

/**
 * 图片背景。
 *
 * 图片会先被裁剪到导航栏形状内，再叠加一层遮罩和边框。
 * 遮罩的作用是保证图标、文字和指示器在复杂图片上仍然可读。
 *
 * @param painter 背景图片。
 * @param contentScale 图片填充方式，默认裁剪铺满导航栏。
 * @param overlayColor 图片遮罩色。需要更亮时可以传白色半透明，需要压暗时传黑色半透明。
 * @param border 背景边框。
 */
fun imageBackground(
    painter: Painter,
    contentScale: ContentScale = ContentScale.Crop,
    overlayColor: Color = Color.Black.copy(alpha = NavBackgroundTokens.DEFAULT_IMAGE_OVERLAY_ALPHA),
    border: NavBackgroundBorder = navBackgroundBorder(),
): NavBackground = object : NavBackground {
    @Composable
    override fun BoxScope.draw(modifier: Modifier, shape: Shape) {
        Box(modifier = modifier.clip(shape)) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier.matchParentSize(),
            )
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
}

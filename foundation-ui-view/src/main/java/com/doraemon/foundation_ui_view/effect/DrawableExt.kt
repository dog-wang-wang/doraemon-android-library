package com.doraemon.foundation_ui_view.effect


import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT
import android.graphics.drawable.LayerDrawable
import androidx.annotation.ColorInt

// 扩展函数生成上圆角数组
fun Float.topCorners(): FloatArray {
    return floatArrayOf(this, this, this, this, 0f, 0f, 0f, 0f)
}

// 扩展函数生成下圆角数组
fun Float.bottomCorners(): FloatArray {
    return floatArrayOf(0f, 0f, 0f, 0f, this, this, this, this)
}

// 扩展函数生成全圆角数组
fun Float.allCorners(): FloatArray {
    return floatArrayOf(this, this, this, this, this, this, this, this)
}

fun createSolidDrawable(@ColorInt solidColor: Int, radii: FloatArray? = null): Drawable {
    return GradientDrawable().apply {
        setColor(solidColor)
        orientation
        cornerRadii = radii
    }
}

fun createGradientDrawable(
    gradientColors: IntArray,
    radii: FloatArray? = null,
    orientation: Orientation = LEFT_RIGHT
): GradientDrawable {
    return GradientDrawable().apply {
        colors = gradientColors
        this.orientation = orientation
        cornerRadii = radii
    }
}

fun Drawable.withStroke(@ColorInt strokeColor: Int, strokeWidth: Int, corner: Float = 0f): StrokeDrawable {
    return StrokeDrawable(this, strokeWidth, corner, strokeColor)
}

fun Drawable.withStroke(
    gradientColors: IntArray,
    strokeWidth: Int,
    corner: Float = 0f,
    orientation: Orientation = LEFT_RIGHT
): StrokeDrawable {
    return StrokeDrawable(this, strokeWidth, corner, 0, gradientColors, orientation)
}

fun Drawable.withShadow(
    @ColorInt shadowColor: Int,
    shadowRadius: Float,
    corner: Float = 0f,
): ShadowDrawable {
    return ShadowDrawable(this, shadowRadius, corner, shadowColor)
}

fun Drawable.withShadow(
    gradientColors: IntArray,
    shadowRadius: Float,
    corner: Float = 0f,
    orientation: Orientation = LEFT_RIGHT
): ShadowDrawable {
    return ShadowDrawable(this, shadowRadius, corner, 0, gradientColors, orientation)
}

fun createGradientShader(bounds: RectF, gradientColors: IntArray?, orientation: Orientation): LinearGradient? {
    val colors = gradientColors ?: return null
    val coordinate = GradientCoordinate.create(orientation, bounds)
    return LinearGradient(
        coordinate.getStartX(), coordinate.getStartY(),
        coordinate.getEndX(), coordinate.getEndY(),
        colors,
        null, Shader.TileMode.CLAMP
    )
}

/**
 * 将一个Drawable转变为一个带有inset的Drawable
 */
fun Drawable.withInset(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): Drawable {
    return LayerDrawable(arrayOf(this)).apply {
        setLayerInset(0, left, top, right, bottom)
    }
}

/**
 * 将一个Drawable转变为一个带圆角的Drawable
 */
fun Drawable.withCorner(radius: Float): Drawable {
    return RoundedDrawable(radius, this)
}


package com.doraemon.foundation.effect

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.doraemon.foundation.utils.dp
import com.doraemon.foundation.view.getColor
import com.doraemon.foundation.view.getDrawable
import com.doraemon.foundation.view.resolveAttr

fun Drawable.toRipple(context: Context, radius: Float = 0f): Drawable {
    return RippleDrawable(
        AppCompatResources.getColorStateList(
            context,
            context.resolveAttr(R.attr.colorControlHighlight)
        ),
        this,
        GradientDrawable().apply {
            cornerRadius = radius
            setColor(Color.WHITE)
        }
    )
}

fun Drawable.toRipple(context: Context, radii: FloatArray): Drawable {
    return RippleDrawable(
        AppCompatResources.getColorStateList(
            context,
            context.resolveAttr(R.attr.colorControlHighlight)
        ),
        this,
        GradientDrawable().apply {
            cornerRadii = radii
            setColor(Color.WHITE)
        }
    )
}

/**
 * 给View设置水波纹的点击效果
 * @param radius 背景圆角
 * @param strokeWidth 背景描边的线宽
 * @param strokeColor 背景描边的颜色，没有描边的时候传入null
 * @param solidColor 背景的填充颜色，没有填充的时候传入null
 */
fun View.applyRippleBackground(
    radius: Float = 0f,
    solidColor: Int? = null,
    strokeWidth: Int = 0,
    strokeColor: Int? = null
) {
    val d = createShapeDrawable(radius, strokeWidth, strokeColor, solidColor)
    background = d.toRipple(context, radius)
}

fun View.applyGradientRippleBackground(
    radius: Float,
    colors: IntArray,
    orientation: GradientDrawable.Orientation = GradientDrawable.Orientation.LEFT_RIGHT
) {
    val d = GradientDrawable().apply {
        cornerRadius = radius
        setColors(colors)
        this.orientation = orientation
    }
    background = d.toRipple(context, radius)
}

/**
 * 设置View的背景
 * @param radius 背景圆角
 * @param strokeWidth 背景描边的线宽
 * @param strokeColor 背景描边的颜色，没有描边的时候传入null
 * @param solidColor 背景的填充颜色，没有填充的时候传入null
 */
fun View.applyShapeBackground(
    radius: Float = 0f,
    strokeWidth: Int = 0,
    strokeColor: Int? = null,
    solidColor: Int? = null
) {
    background = createShapeDrawable(radius, strokeWidth, strokeColor, solidColor)
}

/**
 * 构造shape类型的Drawable
 * @param radius 圆角
 * @param strokeWidth 描边的线宽
 * @param strokeColor 描边的颜色，没有描边的时候传入null
 * @param solidColor 的填充颜色，没有填充的时候传入null
 */
fun createShapeDrawable(
    radius: Float,
    strokeWidth: Int = 0,
    strokeColor: Int? = null,
    solidColor: Int? = null
): Drawable {
    return GradientDrawable().apply {
        solidColor?.let { setColor(it) }
        strokeColor?.let { setStroke(strokeWidth, it) }
        cornerRadius = radius
    }
}

/**
 * 设置圆环为渐变色的Drawable
 */
fun View.applyGradientRingDrawable(
    radius: Float = 0f,
    strokeWidth: Float = 0f,
    soldColor: Int? = null,
    gradientColors: IntArray
): Drawable {
    // 背景填充色
    val roundDrawable: Drawable? = soldColor?.run {
        GradientDrawable().apply {
            cornerRadius = radius
            setColor(soldColor)
        }
    }
    // 圆环渐变背景
    val roundRingDrawable = ShapeDrawable().apply {
        shape = RoundRectShape(
            FloatArray(8) { radius },
            RectF(strokeWidth, strokeWidth, strokeWidth, strokeWidth),
            // 0.5dp作为内环与外环圆角的偏移，这样看起来更加圆滑
            FloatArray(8) { radius - 0.5.dp }
        )
        paint.shader = LinearGradient(
            0f, 0f, measuredWidth.toFloat(), 0f, gradientColors,
            null, Shader.TileMode.CLAMP
        )
    }
    return LayerDrawable(
        if (roundDrawable != null) arrayOf(roundDrawable, roundRingDrawable)
        else arrayOf(roundRingDrawable)
    )
}

/**
 * 使用代码设置 background="?attr/selectableItemBackground"
 */
fun View.applySelectableItemBackground() {
    setBackgroundResource(context.resolveAttr(R.attr.selectableItemBackground))
}

/**
 * 使用代码设置 background="?attr/selectableItemBackgroundBorderless"
 */
fun View.applySelectableItemBackgroundBorderless() {
    setBackgroundResource(context.resolveAttr(R.attr.selectableItemBackgroundBorderless))
}

/**
 * 使用代码设置 background="?attr/actionBarItemBackground"
 */
fun View.applyActionBarItemBackground() {
    setBackgroundResource(context.resolveAttr(R.attr.actionBarItemBackground))
}

fun View.selectorDrawable(
    @DrawableRes normal: Int,
    @DrawableRes selected: Int
): Drawable = selectorDrawable(getDrawable(normal), getDrawable(selected))

fun View.selectorDrawable(
    normal: Drawable,
    selected: Drawable
): Drawable {
    return StateListDrawable().apply {
        addState(intArrayOf(R.attr.state_selected), selected)
        addState(intArrayOf(), normal)
    }
}

fun View.selectorColor(
    @ColorRes normal: Int,
    @ColorRes selected: Int
): ColorStateList {
    return ColorStateList(
        arrayOf(intArrayOf(R.attr.state_selected), intArrayOf()),
        intArrayOf(getColor(selected), getColor(normal))
    )
}

fun View.applySelectorDrawable(
    @DrawableRes normal: Int,
    @DrawableRes selected: Int
): Drawable = applySelectorDrawable(getDrawable(normal), getDrawable(selected))

fun View.applySelectorDrawable(
    normal: Drawable,
    selected: Drawable
): Drawable {
    background = selectorDrawable(normal, selected)
    return background
}


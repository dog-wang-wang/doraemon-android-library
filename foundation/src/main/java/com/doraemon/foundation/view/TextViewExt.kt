package com.doraemon.foundation.view

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ArrayRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.StringRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.doOnLayout
import androidx.core.widget.addTextChangedListener

fun TextView.setDrawableRelativeWithIntrinsicBounds(
    l: Drawable? = null,
    t: Drawable? = null,
    r: Drawable? = null,
    b: Drawable? = null
) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(l, t, r, b)
}

fun TextView.setDrawableRelativeWithIntrinsicBounds(
    l: Int? = null,
    t: Int? = null,
    r: Int? = null,
    b: Int? = null
) {
    val left = if (l == null) null else getDrawable(l)
    val top = if (t == null) null else getDrawable(t)
    val right = if (r == null) null else getDrawable(r)
    val bottom = if (b == null) null else getDrawable(b)
    setCompoundDrawablesRelativeWithIntrinsicBounds(left, top, right, bottom)
}

fun TextView.setDrawableTopVerticalCenter(@DrawableRes iconResId: Int, padding: Int = 0) {
    setDrawableTopVerticalCenter(getDrawable(iconResId), padding)
}

fun TextView.setDrawableTopVerticalCenter(icon: Drawable, padding: Int = 0) {
    compoundDrawablePadding = padding
    setCompoundDrawablesRelativeWithIntrinsicBounds(null, icon, null, null)
    doOnLayout {
        compoundDrawables[1]?.apply {
            val offsetY = baseline + paint.ascent().toInt() - intrinsicHeight - padding
            bounds.offset(0, offsetY)
        }
    }
}


// TextView文字渐变
fun TextView.applyTextGradient(colors: IntArray, positions: FloatArray? = null) {
    paint.shader = LinearGradient(0f, measuredHeight.toFloat(), measuredWidth.toFloat(), measuredHeight.toFloat(),
        colors, positions, Shader.TileMode.CLAMP).apply {
    }
    invalidate()
}

fun TextView.firstLineCentralAxisHeight(): Float {
    return with(paint.fontMetrics) { (descent - ascent - leading) / 2 }
}

fun TextView.setFont(@FontRes id: Int) {
    if (isInEditMode) return
    typeface = ResourcesCompat.getFont(context, id)
}

fun TextView.setFontWeight(weight: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        typeface = Typeface.create(null, weight, false)
    } else {
        when (weight) {
            400 -> typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
            600 -> typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)
            700 -> typeface = Typeface.create("sans-serif-black", Typeface.NORMAL)
        }
    }
}

// 设置文字渐变效果
fun TextView.applyShader(colors: IntArray, positions: FloatArray? = null) {
    fun setShader() {
        var drawableSum = 0
        if (compoundDrawables[0] != null) {
            drawableSum += compoundDrawables[0].intrinsicWidth + compoundDrawablePadding
        }
        if (compoundDrawables[2] != null) {
            drawableSum += compoundDrawables[2].intrinsicWidth + compoundDrawablePadding
        }
        val textWidth = paint.measureText(text.toString())
        val startX = (width - textWidth - drawableSum) / 2
        val endX = startX + textWidth
        val mLinearGradient = LinearGradient(
            startX, 0f, endX, 0f,
            colors,
            positions,
            Shader.TileMode.CLAMP
        )
        paint.shader = mLinearGradient
    }
    doOnLayout { setShader() }
    addTextChangedListener { setShader() }
}

fun TextView.applyShader(@ArrayRes colorsId: Int, @ArrayRes positionsId: Int) {
    val colors = context.resources.getIntArray(colorsId)
    val positionIntArray = context.resources.getIntArray(positionsId)
    val positionFloatArray = FloatArray(positionIntArray.size) { positionIntArray[it] / 10000f }
    applyShader(colors, positionFloatArray)
}

fun TextView.applyVerticalShader(topColor: Int, bottomColor: Int) {
    val h = paint.descent() - paint.ascent()
    val fix = 10f // 这里有一个字体和控件的边距，修正一下
    val mLinearGradient = LinearGradient(0f, fix, 0f, h + fix,
        topColor, bottomColor,
        Shader.TileMode.CLAMP
    )
    paint.shader = mLinearGradient
}

fun View.setMargins(margin: Int) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(margin, margin, margin, margin)
}

fun View.setMargins(l: Int = 0, t: Int = 0, r: Int = 0, b: Int = 0) {
    (layoutParams as? ViewGroup.MarginLayoutParams)?.setMargins(l, t, r, b)
}

fun TextView.setAttributes(textSize: Float, @ColorRes resId: Int, fontWeight: Int = 500) {
    this.textSize = textSize
    setFontWeight(fontWeight)
    this.setTextColor(getColor(resId))
}

fun TextView.updateTextColor(@ColorRes normalColor: Int, @ColorRes selectedColor: Int) {
    val colorId = if (isSelected) selectedColor else normalColor
    val color = getColor(colorId)
    setTextColor(color)
}

fun TextView.spannable(
    @StringRes originalTextId: Int,
    @StringRes targetTextId: Int,
    @ColorRes colorId: Int
): SpannableString {
    val original = getString(originalTextId)
    val target = getString(targetTextId)
    val color = getColor(colorId)
    val startIndex = original.indexOf(target)
    val endIndex = startIndex + target.length
    return SpannableString(original).apply {
        setSpan(
            ForegroundColorSpan(color),
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}

// 动画：淡入 + 放大 → 淡出 + 缩小
fun TextView.startScaleAnimation(duration: Long = 3000,endAction: (() -> Unit)? = null) {
    this.animate()
        .alpha(1f).scaleX(1f).scaleY(1f)
        .setDuration(duration)
        .withEndAction {
            this.animate()
                .alpha(0f).scaleX(0f).scaleY(0f)
                .setDuration(duration)
                .withEndAction end@{
                    endAction ?: return@end
                    endAction()
                }
                .start()
        }
        .start()
}
package com.doraemon.foundation.effect

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable.Orientation
import androidx.core.graphics.toRectF
import androidx.core.graphics.withSave
import kotlin.math.ceil

class StrokeDrawable(
    private val drawable: Drawable,
    private var strokeWidth: Int = 0,
    private val corner: Float = 0f,
    private val strokeColor: Int = Color.TRANSPARENT,
    private val gradientColors: IntArray? = null,
    private val orientation: Orientation = Orientation.LEFT_RIGHT
) : Drawable() {
    private val halfWidth = ceil(strokeWidth / 2.0)
    private val path = Path()
    private val rectF = RectF()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeWidth = this@StrokeDrawable.strokeWidth.toFloat()
        if (strokeColor != Color.TRANSPARENT) {
            color = strokeColor
        }
    }

    override fun onBoundsChange(bounds: Rect) {
        val rect = Rect(bounds)
        rect.inset(-halfWidth.toInt(), -halfWidth.toInt())
        drawable.bounds = rect
        paint.shader = createGradientShader(bounds.toRectF(), gradientColors, orientation)
    }

    override fun draw(canvas: Canvas) {
        val corner = corner - halfWidth.toInt()
        canvas.withSave {
            path.reset()
            rectF.set(bounds)
            path.addRoundRect(rectF, corner + halfWidth.toInt(), corner + halfWidth.toInt(), Path.Direction.CW)
            canvas.clipPath(path)

            drawable.draw(canvas)

            path.reset()
            rectF.inset(halfWidth.toFloat(), halfWidth.toFloat())
            path.addRoundRect(rectF, corner, corner, Path.Direction.CW)
            canvas.drawPath(path, paint)
        }
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
        paint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
        paint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
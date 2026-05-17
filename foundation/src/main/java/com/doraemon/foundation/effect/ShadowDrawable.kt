package com.doraemon.foundation.effect

import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable.Orientation
import androidx.core.graphics.toRectF
import androidx.core.graphics.withSave
import com.doraemon.foundation.utils.dpf

class ShadowDrawable(
    private val drawable: Drawable,
    private val shadowRadius: Float = 0f,
    private val corner: Float = 0f,
    private val shadowColor: Int = Color.TRANSPARENT,
    private val gradientColors: IntArray? = null,
    private var orientation: Orientation = Orientation.LEFT_RIGHT
) : Drawable() {
    private val shadowPaint = Paint()
    private val clipRectF = RectF()
    private val clipPaint = Paint().apply {
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    }
    private val path = Path()

    override fun onBoundsChange(bounds: Rect) {
        drawable.bounds = bounds
        clipRectF.set(bounds)
        clipRectF.inset(-shadowRadius, -shadowRadius)
        path.reset()
        path.addRoundRect(bounds.toRectF(), corner, corner, Path.Direction.CW)
        shadowPaint.shader = createGradientShader(bounds.toRectF(), gradientColors, orientation)
        shadowPaint.setShadowLayer(shadowRadius, 0.dpf, 0.dpf, shadowColor)
    }

    override fun draw(canvas: Canvas) {
        val layer = canvas.saveLayer(clipRectF, shadowPaint)
        canvas.drawPath(path, shadowPaint)
        canvas.drawPath(path, clipPaint)
        canvas.restoreToCount(layer)
        canvas.withSave {
            canvas.clipPath(path)
            drawable.draw(canvas)
        }
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
        shadowPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
        shadowPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
package com.doraemon.foundation.effect

import android.graphics.*
import android.graphics.drawable.Drawable
import androidx.annotation.Px
import androidx.core.graphics.toRectF

class RoundedDrawable(
    @Px val radius: Float,
    private val drawable: Drawable
) : Drawable() {
    private val path = Path()

    override fun draw(canvas: Canvas) {
        canvas.save()
        canvas.clipPath(path)
        drawable.draw(canvas)
        canvas.restore()
    }

    override fun onBoundsChange(bounds: Rect) {
        drawable.bounds.set(bounds)
        path.reset()
        path.addRoundRect(bounds.toRectF(), radius, radius, Path.Direction.CW)
    }

    override fun setAlpha(alpha: Int) {
        drawable.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        drawable.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java", ReplaceWith("PixelFormat.TRANSLUCENT", "android.graphics.PixelFormat"))
    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}
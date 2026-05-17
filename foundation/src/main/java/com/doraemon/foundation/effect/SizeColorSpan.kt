package com.doraemon.foundation.effect

import android.graphics.LinearGradient
import android.graphics.Shader
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

class SizeColorSpan(
    private val colors: IntArray,
    private val size: Float,
    private val _typeface: Typeface?,
    private val text: String
) : TypefaceSpan(null) {
    override fun updateDrawState(ds: TextPaint) {
        ds.typeface = _typeface
        val shader = LinearGradient(
            0f, 0f, ds.measureText(text), 0f,
            colors, null, Shader.TileMode.CLAMP
        )
        ds.shader = shader
        ds.textSize = size
        super.updateDrawState(ds)
    }
}
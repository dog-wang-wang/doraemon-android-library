package com.doraemon.foundation_ui_view.effect

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.os.Parcel
import android.os.Parcelable
import android.text.style.ReplacementSpan

class ShaderSpan(private val shader: Shader, private val typeface: Typeface, private val textSize: Float) : ReplacementSpan(), Parcelable {
    constructor(parcel: Parcel) : this(TODO("shader"), TODO("typeface"), parcel.readFloat()) {}

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int, fm: Paint.FontMetricsInt?): Int {
        paint.textSize = textSize
        return Math.round(paint.measureText(text, start, end))
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint) {
        val shaderPaint = Paint(paint)
        shaderPaint.shader = shader
        shaderPaint.textSize = textSize
        shaderPaint.typeface = typeface
        canvas.drawText(text, start, end, x, y.toFloat(), shaderPaint)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(textSize)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShaderSpan> {
        override fun createFromParcel(parcel: Parcel): ShaderSpan {
            return ShaderSpan(parcel)
        }

        override fun newArray(size: Int): Array<ShaderSpan?> {
            return arrayOfNulls(size)
        }
    }
}
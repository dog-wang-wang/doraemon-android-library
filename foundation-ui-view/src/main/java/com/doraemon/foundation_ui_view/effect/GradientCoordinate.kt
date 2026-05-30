package com.doraemon.foundation_ui_view.effect

import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation.*

class GradientCoordinate(
    private val start: PointF,
    private val end: PointF
) {
    fun getStartX() = start.x

    fun getStartY() = start.y

    fun getEndX() = end.x

    fun getEndY() = end.y

    companion object {
        fun create(orientation: GradientDrawable.Orientation, bounds: RectF): GradientCoordinate {
            val leftTop = PointF(0f, bounds.top)
            val leftBottom = PointF(0f, bounds.bottom)
            val rightTop = PointF(bounds.right, 0f)
            val rightBottom = PointF(bounds.right, bounds.bottom)
            return when (orientation) {
                TOP_BOTTOM -> GradientCoordinate(leftTop, leftBottom)
                BOTTOM_TOP -> GradientCoordinate(leftBottom, leftTop)
                LEFT_RIGHT -> GradientCoordinate(leftTop, rightTop)
                RIGHT_LEFT -> GradientCoordinate(rightTop, leftTop)
                TL_BR -> GradientCoordinate(leftTop, rightBottom)
                TR_BL -> GradientCoordinate(rightTop, leftBottom)
                BL_TR -> GradientCoordinate(leftBottom, rightTop)
                BR_TL -> GradientCoordinate(rightBottom, leftTop)
            }
        }
    }
}
package com.doraemon.foundation.utils

import android.content.res.Resources
import android.graphics.Color
import android.view.View
import kotlin.math.roundToInt

/**
 * dp转成px
 * 在代码中直接调用8.dp这种就可以使用dp
 */
val Number.dp: Int
    get() = (toFloat() * Resources.getSystem().displayMetrics.density).roundToInt()

/**
 * dp转成px
 * 在代码中直接调用8.dpf这种就可以使用dp
 */
val Number.dpf: Float
    get() = toFloat() * Resources.getSystem().displayMetrics.density

/**
 * dp转成px
 * 在代码中直接调用8.dp这种就可以使用dp
 */
val Number.px: Int
    get() = dp / 2

/**
 * dp转成px
 * 在代码中直接调用8.dpf这种就可以使用dp
 */
val Number.pxf: Float
    get() = dpf / 2

val Number.sp: Float
    get() = toFloat() * getScale()

fun getScale(): Float {
    var scale: Float
    with(Resources.getSystem()){
        scale = configuration.fontScale * displayMetrics.density
    }
    return scale
}

fun Int.alpha(alpha: Float): Int {
    return Color.alpha(this)
}

fun Int.withAlpha(alpha: Float): Int {
    return Color.argb((255 * alpha).roundToInt(), Color.red(this), Color.green(this), Color.blue(this))
}

fun Int.withExactlyMode(): Int {
    return View.MeasureSpec.makeMeasureSpec(this, View.MeasureSpec.EXACTLY)
}
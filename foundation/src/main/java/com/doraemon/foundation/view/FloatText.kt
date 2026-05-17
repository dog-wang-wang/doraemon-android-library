package com.doraemon.foundation.view

import android.R
import android.graphics.Paint
import android.graphics.Typeface
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.lifecycle.lifecycleScope
import com.doraemon.foundation.utils.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 在LifecycleViewGroup中添加多个浮动文字, 无限添加
 * @param randomTexts 随机文字
 * @param floatingTextSize 文字大小
 * @param floatingTextColorRes 颜色
 * @param floatingTextDuration 浮动文字显示时长
 * @param num 每次循环显示浮动文字的数量
 * @param floatingTextInterval 显示间隔
 */
fun LifecycleViewGroup.moreSpawnFloatingText(
    randomTexts: List<String>,
    floatingTextSize: Float = 18f,
    @ColorRes floatingTextColorRes: Int = R.color.white,
    floatingTextDuration: Long = 3000,
    num: Int = 5,
    floatingTextInterval: Long = 1_000L
) {
    lifecycleScope.launch(Dispatchers.IO) {
        while (isActive) {
            repeat(num) {
                spawnFloatingText(randomTexts, floatingTextSize, floatingTextColorRes, floatingTextDuration)
            }
            delay(floatingTextInterval)
        }
    }
}

/**
 * 在ViewGroup中添加一个浮动文字
 * @param randomTexts 随机文字列表
 * @param floatingTextSize 浮动文字大小
 * @param floatingTextColorRes 浮动文字颜色
 * @param floatingTextDuration 浮动文字显示时长
 */
suspend fun ViewGroup.spawnFloatingText(
    randomTexts: List<String>,
    floatingTextSize: Float = 18f,
    @ColorRes floatingTextColorRes: Int = R.color.white,
    floatingTextDuration: Long = 3000,
) {
    // 1. 计算文字、尺寸、坐标（耗时）
    val randomText = randomTexts.random()
    val paint = Paint().apply {
        textSize = floatingTextSize.sp
        typeface = Typeface.DEFAULT_BOLD
    }
    val w = paint.measureText(randomText).toInt()
    val h = (paint.descent() - paint.ascent()).toInt()
    val textLeft = (0..measuredWidth - w).random()
    val textTop = (0..measuredHeight - h).random()
    // 2. 主线程添加 TextView
    withContext(Dispatchers.Main) {
        val tv = textView {
            setMargins(l = textLeft, t = textTop)
            setAttributes(floatingTextSize, floatingTextColorRes)
            text = randomText
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
        }
        tv.startScaleAnimation(floatingTextDuration) {
            removeView(tv)
        }
    }
}
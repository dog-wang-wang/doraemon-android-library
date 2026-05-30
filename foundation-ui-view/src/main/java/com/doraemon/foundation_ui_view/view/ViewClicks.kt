package com.doraemon.foundation_ui_view.view

import android.view.HapticFeedbackConstants
import android.view.View
import android.view.View.OnClickListener

/**
 * 控制按钮在240毫秒内不能重复点击
 */
fun View.clicks(action: (View) -> Unit) {
    setOnClickListener(object : OnClickListener {
        private var hit = 0L
        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - hit > 240) {
                action(v)
            }
            hit = now
        }
    })
}

/**
 * 控制按钮在500毫秒内不能重复点击
 */
fun <T> View.clicks(any: T, action: (View, T) -> Unit) {
    setOnClickListener(object : OnClickListener {
        private var hit = 0L
        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - hit > 500) {
                action(v, any)
            }
            hit = now
        }
    })
}

/**
 * 连续点击N次触发点击事件，用于进入Debug页面
 * @param times 连续点击的次数
 * @param action 点击事件的回调
 */
fun View.debugClicks(times: Int = 5, action: (View) -> Unit) {
    setOnClickListener(object : OnClickListener {
        private var hit = 0L
        private var count = 0
        override fun onClick(v: View) {
            val now = System.currentTimeMillis()
            if (now - hit < 500) {
                count++
                if (count >= times - 1) {
                    action(v)
                }
            } else {
                count = 0
            }
            hit = now
        }
    })
}

/**
 * 触发普通震感反馈
 * @param action 操作
 */
fun View.clicksWithHaptics(action: (View) -> Unit) {
    clicks {
        performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        action(it)
    }
}

/**
 * 触发自定义触感反馈
 * @param feedbackConstants 反馈类型
 * @param action 操作
 */
fun View.clicksWithHaptics(feedbackConstants: Int, action: (View) -> Unit) {
    clicks {
        performHapticFeedback(feedbackConstants)
        action(it)
    }
}
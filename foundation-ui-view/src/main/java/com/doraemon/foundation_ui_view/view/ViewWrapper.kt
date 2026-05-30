package com.doraemon.foundation_ui_view.view

import android.view.View
import androidx.annotation.Keep

/**
 * @author zhaojiahao
 * @date 2025.04.16
 * @description 这个类的作用是获取View的宽高，以及设置宽高
 */
class ViewWrapper(private val view: View) {
    //这个注解的意思是要让这个函数不被代码混淆
    @Keep
    fun getWidth(): Int {
        return view.layoutParams.width
    }

    @Keep
    fun setWidth(width: Int) {
        view.layoutParams.width = width
        view.requestLayout()
    }

    @Keep
    fun getHeight(): Int {
        return view.layoutParams.height
    }

    @Keep
    fun setHeight(height: Int) {
        view.layoutParams.height = height
        view.requestLayout()
    }
}
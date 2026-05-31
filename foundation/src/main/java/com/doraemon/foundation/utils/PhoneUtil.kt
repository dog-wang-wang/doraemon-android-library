package com.doraemon.foundation.utils

import java.util.regex.Pattern

fun String.isPhoneNumber(): Boolean {
    //这里是手机号的正则表达式
    val mobileRegEx = "^1[3-9][0-9]{9}$"
    //函数语法 匹配的正则表达式
    val pattern = Pattern.compile(mobileRegEx)
    //进行匹配
    val matcher = pattern.matcher(this)
    return matcher.matches()
}

fun String.changePhoneNumber(): String {
    var number = this
    val length: Short = 11
    if (number.length > length) {
        number = number.substring(3)
    }
    val changed = number.replace("(\\d{3})(\\d{4})(\\d{4})".toRegex(), "$1****$3")
    return changed
}

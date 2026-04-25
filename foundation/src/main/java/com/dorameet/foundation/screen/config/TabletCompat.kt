package com.dorameet.foundation.screen.config

import com.dorameet.foundation.screen.ScreenState
import com.dorameet.foundation.screen.utils.getCurrentScreenState

/**
 * 默认情况下需实现四个具体的方法
 * onTablet与onPhone默认会直接进行分发
 */
interface TabletCompat {
    fun onPhonePortrait()

    // 手机横屏
    fun onPhoneLandScape()

    // 平板竖屏
    fun onTabletPortrait()

    // 平板横屏
    fun onTabletLandScape()

    // 平板
    fun onTablet() {
        dispatch()
    }

    // 手机
    fun onPhone() {
        dispatch()
    }

    fun dispatch() {
        when (getCurrentScreenState()) {
            is ScreenState.PhonePortrait -> onPhonePortrait()
            is ScreenState.PhoneLandScape -> onPhoneLandScape()
            is ScreenState.TabletPortrait -> onTabletPortrait()
            is ScreenState.TabletLandScape -> onTabletLandScape()
        }
    }
}
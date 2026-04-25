package com.dorameet.foundation.screen

sealed class ScreenState {
    // 手机竖屏
    object PhonePortrait: ScreenState()
    // 手机横屏
    object PhoneLandScape: ScreenState()
    // 平板竖屏
    object TabletPortrait: ScreenState()
    // 平板横屏
    object TabletLandScape: ScreenState()
}
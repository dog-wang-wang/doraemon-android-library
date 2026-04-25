package com.dorameet.foundation.screen.utils

import android.content.res.Configuration
import com.dorameet.foundation.R
import com.dorameet.foundation.DoraBaseApplication.Companion.app
import com.dorameet.foundation.screen.ScreenState

// 判断手机or平板
fun isTablet() = app.resources.getBoolean(R.bool.is_tablet)

// 判断横屏or竖屏
fun isLandscape() = app.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

// 获取当前屏幕状态
fun getCurrentScreenState() = when {
    !isTablet() && !isLandscape() -> ScreenState.PhonePortrait
    isTablet() && isLandscape() -> ScreenState.TabletLandScape
    isTablet() && !isLandscape() -> ScreenState.TabletPortrait
    else -> ScreenState.PhoneLandScape
}

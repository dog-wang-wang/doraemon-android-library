package com.dorameet.foundation.screen.activity

import android.os.Bundle
import com.dorameet.foundation.screen.config.TabletCompat
import com.dorameet.foundation.screen.utils.isTablet

/**
 * 检测平板的边到边AppCompatActivity
 */
open class CheckTabletAppCompatActivity : EdgeToEdgeAppCompatActivity(), TabletCompat {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 判断手机or平板
        if (isTablet()) onTablet() else onPhone()
    }

    /**
     * 手机竖屏特殊操作
     */
    override fun onPhonePortrait() = Unit

    /**
     * 手机横屏特殊操作
     */
    override fun onPhoneLandScape() = Unit

    /**
     * 平板竖屏特殊操作
     */
    override fun onTabletPortrait() = Unit

    /**
     * 平板横屏特殊操作
     */
    override fun onTabletLandScape() = Unit
}
package com.dorameet.foundation.screen.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dorameet.foundation.screen.config.TabletCompat
import com.dorameet.foundation.screen.utils.isTablet

abstract class CheckTabletFragment : BaseFragment(), TabletCompat {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val superView = super.onCreateView(inflater, container, savedInstanceState)
        // 先检测机型执行机型相关操作
        if (isTablet()) onTablet() else onPhone()
        return superView
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
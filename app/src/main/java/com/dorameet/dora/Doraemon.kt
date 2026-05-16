package com.dorameet.dora

import com.dorameet.foundation.DoraBaseApplication
import com.dorameet.log.LogUtils

open class Doraemon: DoraBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.init(isDebug()) {

        }
    }
}
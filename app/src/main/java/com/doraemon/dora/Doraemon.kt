package com.doraemon.dora

import com.doraemon.foundation.DoraBaseApplication
import com.doraemon.log.LogUtils

open class Doraemon: DoraBaseApplication() {
    override fun onCreate() {
        super.onCreate()
        LogUtils.init(isDebug()) {

        }
    }
}
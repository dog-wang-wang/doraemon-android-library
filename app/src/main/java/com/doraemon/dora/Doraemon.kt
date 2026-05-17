package com.doraemon.dora

import com.doraemon.foundation.DoraBaseApplication
import com.doraemon.log.LogConfig

open class Doraemon() : DoraBaseApplication() {
    override val isDebug = isDebug()
    override val logConfiguration: LogConfig.Builder.() -> Unit
        get() = super.logConfiguration
}
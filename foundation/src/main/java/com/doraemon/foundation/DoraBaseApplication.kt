package com.doraemon.foundation

import android.app.Application
import com.doraemon.foundation.utils.MMKVUtil
import com.doraemon.log.LogConfig
import com.doraemon.log.LogUtils

abstract class DoraBaseApplication : Application() {
    abstract val isDebug: Boolean
    /**
     * 子类可以重写此属性来详细配置日志参数。
     * 示例：override val logConfiguration: LogConfig.Builder.() -> Unit = { diskLogPath = ... }
     */
    open val logConfiguration: LogConfig.Builder.() -> Unit = {}

    override fun onCreate() {
        super.onCreate()
        app = this
        // 初始化MMKV
        MMKVUtil.init(this)
        // 初始化log能力
        LogUtils.init(isDebug, logConfiguration)
    }

    companion object {
        lateinit var app: DoraBaseApplication
    }
}
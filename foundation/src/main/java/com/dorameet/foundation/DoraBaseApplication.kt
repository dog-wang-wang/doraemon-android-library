package com.dorameet.foundation

import android.app.Application

open class DoraBaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        app = this
    }

    companion object {
        lateinit var app: DoraBaseApplication
    }
}
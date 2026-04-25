package com.dorameet.foundation.screen.activity

import android.os.Bundle

interface BaseActivityInterface {
    /**
     * 早于基类创建时调用其他方法时的方法
     */
    fun beforeCreate(savedInstanceState: Bundle?)
}
package com.doraemon.foundation.commonExt

import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel

inline fun <reified VM : ViewModel> View.viewModels(): Lazy<VM> {
    return (context as ComponentActivity).viewModels()
}
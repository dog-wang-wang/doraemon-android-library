package com.doraemon.foundation_ui_view.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.view.children
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

abstract class LifecycleViewGroup(
    context: Context, attrs: AttributeSet? = null
) : ViewGroup(context, attrs), LifecycleOwner {
    private var mRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override val lifecycle: Lifecycle
        get() = mRegistry

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        children.forEach { v ->
            if (v.layoutParams is MarginLayoutParams) {
                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0)
            } else {
                measureChild(v, widthMeasureSpec, heightMeasureSpec)
            }
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        if (visibility == VISIBLE) {
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        } else {
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        }
    }

    // 绑定监听事件
    abstract fun bindObservers()

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (lifecycle.currentState <= Lifecycle.State.DESTROYED) {
            // 有一些LifecycleViewGroup是可复用的，第二次onAttachedToWindow的时候会走到这里
            // 创建一个新的lifecycle
            mRegistry = LifecycleRegistry(this)
        }
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        bindObservers()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    }
}
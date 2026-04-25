package com.dorameet.foundation.screen.controller

import android.app.Activity
import android.app.ActivityOptions
import android.transition.ChangeBounds
import android.util.Pair
import android.view.View
import com.dorameet.foundation.screen.config.SharedAnimationConfig

open class SharedAnimationController(
    config: SharedAnimationConfig
) {
    // 共享元素动画配置
    protected open var sharedAnimationConfig: SharedAnimationConfig = SharedAnimationConfig()

    // 共享元素
    private var transitionViews: ArrayList<View> = arrayListOf()

    /**
     * 添加共享元素
     */
    internal fun addTransitionView(view: View) {
        transitionViews.add(view)
    }

    internal fun getSharedTransitionPotions(source: Activity): ActivityOptions {
        val transitionsList = transitionViews.map { Pair(it, it.transitionName) }
        val transitions = transitionsList.toTypedArray()
        return ActivityOptions.makeSceneTransitionAnimation(source, *transitions)
    }

    /**
     * 进行共享元素动画的配置
     */
    internal fun configSharedAnimation(activity: Activity) {
        if (!sharedAnimationConfig.allowAnimation) return
        activity.apply {
            window.sharedElementEnterTransition = ChangeBounds().apply { duration = sharedAnimationConfig.enterTime }
            window.sharedElementExitTransition = ChangeBounds().apply { duration = sharedAnimationConfig.exitTime }
            window.sharedElementReturnTransition = ChangeBounds().apply { duration = sharedAnimationConfig.returnTime }
            window.sharedElementReenterTransition = ChangeBounds().apply { duration = sharedAnimationConfig.reenterTime }
        }
    }
}
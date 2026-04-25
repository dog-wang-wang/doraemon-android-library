package com.dorameet.foundation.screen.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dorameet.foundation.nav.router.navWithActionAndOptions
import com.dorameet.foundation.screen.config.SharedAnimationConfig
import com.dorameet.foundation.screen.controller.SharedAnimationController
import kotlinx.coroutines.launch

/**
 * 边到边ComponentActivity
 */
open class EdgeToEdgeComponentActivity : ComponentActivity(), BaseActivityInterface {
    // 管理共享元素动画的控制器
    private val sharedAnimationController = SharedAnimationController(createSharedAnimationConfig())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 启用边缘到边缘
        enableEdgeToEdge()
        beforeCreate(savedInstanceState)
        // 触发Observers
        lifecycleScope.launch { repeatOnLifecycle(Lifecycle.State.STARTED) { bindObservers() } }
        // 配置共享元素动画
        sharedAnimationController.configSharedAnimation(this)
    }

    override fun beforeCreate(savedInstanceState: Bundle?) {}

    open fun bindObservers() = Unit

    /**
     * 设置共享元素相关配置
     */
    open fun createSharedAnimationConfig() = SharedAnimationConfig()

    /**
     * 添加共享元素
     */
    internal fun addTransitionView(view: View) = sharedAnimationController.addTransitionView(view)

    /**
     * 导航到其他页面
     * @param activity 目标页面
     * @param intentAction intentAction 对Intent的一些额外操作
     */
    internal fun <T : EdgeToEdgeAppCompatActivity> navigate(activity: Class<T>, intentAction: (Intent.() -> Unit)? = null) {
        val options = sharedAnimationController.getSharedTransitionPotions(this)
        navWithActionAndOptions(activity, options, intentAction)
    }
}
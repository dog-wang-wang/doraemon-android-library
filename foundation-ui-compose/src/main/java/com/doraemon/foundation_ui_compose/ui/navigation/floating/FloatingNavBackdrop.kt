package com.doraemon.foundation_ui_compose.ui.navigation.floating

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource

/**
 * 浮动导航栏真实背景模糊的内部绑定对象。
 *
 * 毛玻璃不是单纯画一层半透明颜色，它需要两件事同时成立：
 * 1. 页面内容注册成 backdrop source，让模糊库知道“要模糊谁”。
 * 2. 导航栏背景策略提供 [NavBackdropEffect]，让模糊库知道“怎么模糊、怎么染色”。
 *
 * 这两个条件缺一不可，所以用一个内部对象把 [HazeState] 和 [NavBackdropEffect] 绑定起来。
 * 外部业务只选择 [NavBackground]，不需要直接传 Haze 的状态对象，也不会出现状态和背景策略分离配置的问题。
 */
internal data class FloatingNavBackdrop(
    val state: HazeState,
    val effect: NavBackdropEffect,
)

/**
 * 根据背景策略创建毛玻璃 backdrop 绑定。
 *
 * 这里始终先 remember [HazeState]，再根据背景策略决定是否启用它。
 * 这样可以保证 Compose 重组时 remember 调用顺序稳定，避免条件分支导致状态错位。
 */
@Composable
internal fun rememberFloatingNavBackdrop(background: NavBackground): FloatingNavBackdrop? {
    val state = remember { HazeState() }
    val effect = background.backdropEffect ?: return null
    return remember(state, effect) {
        FloatingNavBackdrop(
            state = state,
            effect = effect,
        )
    }
}

/**
 * 把页面内容注册成毛玻璃取样源。
 *
 * 只有 [FloatingNavBackdrop] 存在时才接入 Haze；纯色和图片背景不会承担真实模糊成本。
 */
internal fun Modifier.floatingBackdropSource(
    backdrop: FloatingNavBackdrop?,
): Modifier = if (backdrop != null) {
    hazeSource(backdrop.state)
} else {
    this
}

/**
 * 在导航栏胶囊上应用真实 backdrop blur。
 *
 * [FloatingNavBackdrop.effect] 来自背景策略，因此毛玻璃的底色、tint、模糊半径和噪点都由背景策略统一控制。
 */
internal fun Modifier.floatingBackdropEffect(
    backdrop: FloatingNavBackdrop?,
): Modifier = if (backdrop != null) {
    hazeEffect(state = backdrop.state) {
        backgroundColor = backdrop.effect.backgroundColor
        tints = listOf(HazeTint(backdrop.effect.tintColor))
        blurRadius = backdrop.effect.blurRadius
        noiseFactor = backdrop.effect.noiseFactor
    }
} else {
    this
}

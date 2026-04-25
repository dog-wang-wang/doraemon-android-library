package com.dorameet.foundation.screen.config

/**
 * 共享元素动画相关配置
 * @param allowAnimation 是否允许动画
 * @param enterTime 入场动画时间
 * @param exitTime 退出动画时间
 * @param returnTime 返回动画时间
 */
data class SharedAnimationConfig(
    val allowAnimation: Boolean = true,
    val enterTime: Long = 200,
    val exitTime: Long = 200,
    val returnTime: Long = 200,
    val reenterTime: Long = 200,
)
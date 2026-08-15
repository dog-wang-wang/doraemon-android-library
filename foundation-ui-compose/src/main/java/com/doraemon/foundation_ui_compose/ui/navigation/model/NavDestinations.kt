package com.doraemon.foundation_ui_compose.ui.navigation.model

import androidx.compose.runtime.Composable
import com.doraemon.foundation_ui_compose.common.Constants
import com.doraemon.log.error
import com.doraemon.log.log


/**
 * 底部导航栏的数据模型。
 *
 * 这个类只描述“有哪些导航项”和“默认选中哪一项”，不直接依赖具体 UI。
 * 这样同一份数据既可以给 Material 风格底栏使用，也可以给自定义悬浮底栏使用。
 *
 * @param items 导航项列表，列表顺序就是底栏从左到右的展示顺序。
 * @param defaultIndex 默认导航项下标。越界时会自动修正到合法范围，避免调用方传错值导致崩溃。
 */
class NavDestinations(
    val items: List<Item>,
    private val defaultIndex: Int
) {

    constructor(list: List<Item>) : this(list, DEFAULT_INDEX)

    /**
     * 返回安全的默认选中下标。
     *
     * 基建库面对外部输入要更保守：
     * 1. 导航列表为空时直接抛错，因为没有任何页面可以作为首页，这是调用方必须修复的问题。
     * 2. 默认下标越界时记录日志并修正到合法范围，避免 App 因配置错误直接崩溃。
     */
    fun getDefaultIndex(): Int {
        require(items.isNotEmpty()) {
            error(Constants.LOG_ERROR_NAVIGATION_EMPTY, Constants.LOG_TAG_NAVIGATION)
            Constants.LOG_ERROR_NAVIGATION_EMPTY
        }

        if (defaultIndex !in items.indices) {
            log(Constants.LOG_ERROR_NAVIGATION_DEFAULT_INVALID, Constants.LOG_TAG_NAVIGATION)
        }

        return defaultIndex.coerceIn(items.indices)
    }

    /**
     * 单个导航目的地。
     *
     * @param label 导航文案。使用 Composable 是为了让调用方可以接入 stringResource、主题字体等 Compose 能力。
     * @param icon 默认导航图标。由业务侧提供，基础库只负责摆放和着色。
     * @param selectedIcon 选中态图标。为空时复用 [icon]，适合只通过颜色表达选中态的场景。
     * @param route 与 NavHost 中 composable(route = ...) 对应的路由字符串。
     */
    data class Item(
        val label: @Composable () -> Unit,
        val icon: @Composable () -> Unit,
        val selectedIcon: (@Composable () -> Unit)? = null,
        val route: String,
    )

    companion object {
        // 默认导航Index
        const val DEFAULT_INDEX = 0
    }
}

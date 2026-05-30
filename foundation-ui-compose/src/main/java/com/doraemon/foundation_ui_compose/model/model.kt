package com.doraemon.foundation_ui_compose.model

import androidx.compose.runtime.Composable
import com.doraemon.foundation_ui_compose.common.Constants
import com.doraemon.log.error
import com.doraemon.log.log


class NavigationBarDestinations(
    val itemDestinations: List<ItemDestination>,
    private val defaultIndex: Int
) {

    constructor(list: List<ItemDestination>) : this(list, DEFAULT_INDEX)

    /**
     * 返回默认选项
     */
    fun getDefaultIndex(): Int {
        // 不能为空
        require(itemDestinations.isNotEmpty()) {
            error(Constants.LOG_ERROR_NAVIGATION_EMPTY, Constants.LOG_TAG_NAVIGATION)
            Constants.LOG_ERROR_NAVIGATION_EMPTY
        }
        // 要防止构造时制定的默认index过大
        return if (itemDestinations.size >= defaultIndex + 1) {
            defaultIndex
        } else {
            log(Constants.LOG_ERROR_NAVIGATION_EMPTY, Constants.LOG_TAG_NAVIGATION)
            itemDestinations.size - 1
        }
    }

    data class ItemDestination(
        val label: @Composable () -> Unit,
        val icon: @Composable () -> Unit,
        val routerName: String,
    )

    companion object {
        // 默认导航Index
        const val DEFAULT_INDEX = 0
    }
}
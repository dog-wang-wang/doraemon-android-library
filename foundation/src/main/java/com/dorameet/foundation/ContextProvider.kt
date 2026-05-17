package com.dorameet.foundation

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri

/**
 * 全局 Context 提供者。
 *
 * 利用 [ContentProvider] 会在 [android.app.Application.onCreate] 之前自动初始化的特性，
 * 实现全局 `appContext` 的自动获取和存储，从而避免手动在 Application 中进行初始化。
 *
 * ### 使用示例：
 * ```kotlin
 * // 在任何地方获取全局 Application Context
 * val context = ContextProvider.appContext
 *
 * // 或者使用强制非空的辅助方法
 * val context = ContextProvider.requireContext()
 * ```
 *
 * ### 注意事项：
 * 必须在 `AndroidManifest.xml` 中注册此 Provider。
 */
class ContextProvider : ContentProvider() {
    companion object {
        /**
         * 全局 Application Context 实例。
         * 在库初始化完成后，此变量将持有有效的上下文引用。
         */
        @JvmStatic
        var appContext: Context? = null
            private set

        /**
         * 获取全局 Context，如果尚未初始化则抛出异常。
         *
         * @return 非空的 [Context] 对象
         * @throws IllegalStateException 如果 appContext 尚未初始化
         */
        @JvmStatic
        fun requireContext(): Context {
            return appContext ?: throw IllegalStateException(
                "ContextProvider 尚未初始化。请确保已在 AndroidManifest.xml 中注册。"
            )
        }
    }

    override fun onCreate(): Boolean {
        // 获取并存储 Application Context
        appContext = context?.applicationContext
        return true
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? = null

    override fun query(uri: Uri, projection: Array<out String>?,
                       selection: String?, selectionArgs: Array<out String>?,
                       sortOrder: String?): Cursor? = null

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<out String>?): Int = 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0

    override fun getType(uri: Uri): String? = null
}

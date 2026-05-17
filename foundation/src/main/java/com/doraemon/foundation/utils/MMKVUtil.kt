package com.doraemon.foundation.utils

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV

object MMKVUtil {

    fun init(context: Context) {
        MMKV.initialize(context)
    }

    fun putString(key: String, value: String) {
        MMKV.defaultMMKV().putString(key, value)
    }

    fun getString(key: String, defValue: String = "") =
        MMKV.defaultMMKV().getString(key, defValue) ?: ""

    fun getStringOrNull(key: String) = MMKV.defaultMMKV().getString(key, null)

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return MMKV.defaultMMKV().getBoolean(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        MMKV.defaultMMKV().putBoolean(key, value)
    }

    fun putLongIfAbsent(key: String, value: Long) {
        val mmkv = MMKV.defaultMMKV()
        if (!mmkv.containsKey(key)) {
            mmkv.putLong(key, value)
        }
    }

    fun getLong(key: String, defValue: Long): Long {
        return MMKV.defaultMMKV().getLong(key, defValue)
    }

    fun putLong(key: String, value: Long) {
        MMKV.defaultMMKV().putLong(key, value)
    }

    fun getInt(key: String, defValue: Int): Int {
        return MMKV.defaultMMKV().getInt(key, defValue)
    }

    fun putInt(key: String, value: Int) {
        MMKV.defaultMMKV().putInt(key, value)
    }

    fun remove(key: String) {
        MMKV.defaultMMKV().removeValueForKey(key)
    }

    /**
     * 存储 Parcelable 对象（纯二进制序列化，性能最高）
     */
    fun putParcelable(key: String, value: Parcelable?) {
        if (value == null) {
            remove(key)
            return
        }
        MMKV.defaultMMKV().encode(key, value)
    }

    /**
     * 读取 Parcelable 对象
     */
    inline fun <reified T : Parcelable> getParcelable(key: String): T? {
        return MMKV.defaultMMKV().decodeParcelable(key, T::class.java)
    }

    /**
     * 自增方法，用于计数
     */
    fun increase(key: String): Int {
        val count = getInt(key, 0) + 1
        putInt(key, count)
        return count
    }
}
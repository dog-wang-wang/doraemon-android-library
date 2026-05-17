package com.doraemon.foundation.utils

inline fun <T> tryCatching(onError: ((Exception) -> Unit) = {}, block: () -> T): T? {
    try {
        return block.invoke()
    } catch (e: Exception) {
        onError.invoke(e)
        return null
    }
}
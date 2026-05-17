package com.doraemon.foundation.commonExt

/**
 * @author zhaojiahao
 * @date 2025.04.16
 */
fun <K, V> MutableMap<K, V>.putIfNotNull(key: K, value: V?) {
    value?.let { this[key] = value }
}

fun Collection<String>.flatten(flag: String = ""): String {
    val builder = StringBuilder()
    mapIndexed { index, value ->
        builder.append(value)
        if (index == size - 1) return@mapIndexed
        builder.append(flag)
    }
    return builder.toString()
}

fun Array<String>.flatten(flag: String = ""): String {
    val builder = StringBuilder()
    mapIndexed { index, value ->
        builder.append(value)
        if (index == size - 1) return@mapIndexed
        builder.append(flag)
    }
    return builder.toString()
}

inline fun <T> List<T>.indexOfFirstFromPos(startPos: Int, predicate: (T) -> Boolean): Int {
    for (index in (startPos until size)) {
        val element = this[index]
        if (predicate(element)) return index
    }
    return Int.MAX_VALUE
}

fun <K, V> Map<K, V>.getOrDefaultValue(key: K, defaultValue: V): V = get(key) ?: defaultValue
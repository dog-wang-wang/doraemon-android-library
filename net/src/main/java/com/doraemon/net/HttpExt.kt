package com.doraemon.net

import com.doraemon.foundation.tips.toastShort


// 该函数用于发起请求并且获取响应结果
suspend inline fun <T> withCatching(block: suspend () -> HttpModel<T>): HttpModel<T> {
    return try {
        block()
    } catch (e: Throwable) {
        HttpModel.ofError(e)
    }
}

// 对于响应结果进行一般同化处理的函数
fun <T> HttpModel<T>.commonHandleResponse(
    onSuccess: () -> Unit,
    onFailure: ((Throwable?) -> Unit)? = null
) {
    when(code) {
        // 请求成功
        HttpUtils.SUCCESS -> onSuccess()
        // 例如后端连访问都没成功，404了，那么这里的error处理就派上用场了)
        HttpUtils.NETWORK_ERROR -> toastShort(R.string.http_err_net)
        // 其他的就直接走错误处理
        else -> onFailure?.invoke(error)
    }
}
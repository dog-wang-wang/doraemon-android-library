package com.doraemon.net

class HttpModel<T>(
    var code: Int = 0,
    var msg: String? = null,
    var data: T? = null,
    var token: String? = null,
    var error: Throwable? = null
) {
    fun isSuccess() = code == 200

    companion object {
        fun <T> ofError(e: Throwable?): HttpModel<T> {
            val message = e?.message ?: "网络异常,请稍后再试"
            return HttpModel(code = HttpUtils.SERVER_ERROR, msg = message, error = e)
        }
    }
}
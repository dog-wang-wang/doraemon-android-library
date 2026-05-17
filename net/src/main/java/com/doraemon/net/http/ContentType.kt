package com.doraemon.net.http

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * 请求内容类型
 */
internal sealed class ContentType(val value: String) {

    /**
     * json
     */
    object Json : ContentType("application/json") {
        override fun getRequestBody(json: String) = json.toRequestBody(this.value.toMediaType())
    }

    abstract fun getRequestBody(json: String): RequestBody
}
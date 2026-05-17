package com.doraemon.net.http

import com.google.gson.Gson
import okhttp3.Request
import okhttp3.RequestBody

/**
 * 拼接url和api地址
 */
fun getRequestPath(url: String, api: String): String {
    val urlSplash = url.endWithSlash()
    val apiSplash = api.startWithSlash()
    return when {
        urlSplash && apiSplash -> "${url.dropLast(1)}$api"
        urlSplash || apiSplash -> url + api
        else -> "$url/$api"
    }
}

/**
 * Builder添加headers
 */
fun Request.Builder.headers(header: Map<String, String>? = null): Request.Builder {
    header?.forEach { this.header(it.key, it.value) }
    return this
}

/**
 * 创建请求Builder
 */
fun getRequestBuilder(path: String) = Request.Builder().url(path)

/**
 * 创建post请求Builder
 */
fun getPostBuilder(path: String, body: RequestBody, header: Map<String, String>? = null) = getRequestBuilder(path).headers(header).method(RequestMethod.POST.name, body)


/**
 * 创建请求
 */
fun getRequest(path: String) = getRequestBuilder(path).build()
/**
 * 创建post请求
 */
fun getPostRequest(path: String, body: RequestBody, header: Map<String, String>? = null) = getPostBuilder(path, body, header).build()

/**
 * 创建json请求体
 */
fun <T> getJsonRequestBody(body: T, gson: Gson? = null): RequestBody {
    val gsonParser = gson ?: Gson()
    val jsonString = gsonParser.toJson(body)
    return ContentType.Json.getRequestBody(jsonString)
}



private fun String.endWithSlash() = endsWith("/")
private fun String.startWithSlash() = startsWith("/")


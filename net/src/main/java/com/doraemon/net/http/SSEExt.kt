package com.doraemon.net.http

import com.google.gson.Gson
import okhttp3.sse.EventSource
import okhttp3.sse.EventSourceListener


/**
 * 发送POST的sse请求
 * @param client sse客户端
 * @param url url
 * @param api 请求的接口
 * @param bodyObject 请求体
 * @param gson gson解析器
 * @param listener sse反馈监听器
 */
fun <T> sendPostSSERequest(
    client: EventSource.Factory,
    url: String,
    api: String,
    bodyObject: T,
    gson: Gson? = null,
    listener: EventSourceListener,
) {
    val requestPath = getRequestPath(url, api)
    sendPostSSERequest(client, requestPath, bodyObject, gson, listener)
}

/**
 * 发送POST的sse请求
 * @param client sse客户端
 * @param requestPath 请求路径
 * @param bodyObject 请求体
 * @param gson gson解析器
 * @param listener sse反馈监听器
 */
fun <T> sendPostSSERequest(
    client: EventSource.Factory,
    requestPath: String,
    bodyObject: T,
    gson: Gson? = null,
    listener: EventSourceListener,
) {
    val requestBody = getJsonRequestBody(bodyObject, gson)
    val request = getPostRequest(requestPath, requestBody)
    client.newEventSource(request, listener)
}
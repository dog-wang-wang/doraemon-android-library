package com.doraemon.net.websocket

import android.util.Log
import com.doraemon.log.debug
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

class WebSocketManager private constructor() : SocketManager {
    private lateinit var client: OkHttpClient
    private lateinit var url: String

    private var webSocketClient: WebSocket? = null


    private val isReconnecting = AtomicBoolean(false) // 避免并发重连
    private val reconnectAttempts = AtomicInteger(0) // 已重连次数
    private var maxReconnectAttempts = 10 // 最大重连次数（可配置）
    private var baseReconnectDelay = 1000L // 基础重连延迟（1秒）
    private var maxReconnectDelay = 30000L // 最大重连延迟（30秒）
    private var isInitiativeDisconnect = AtomicBoolean(false) // 是否主动断开（主动断开不重连）

    private var closedCode = CLOSED_CODE

    override fun connect(listener: SocketListener) {
        isInitiativeDisconnect.set(false)
        webSocketClient = client.connectSocket(url, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                debug(
                    LOG_TAG,
                    "webSocket链接成功：\n\twebsocket: $webSocket\n\turl: ${response.request.url}\n\tmessage: ${response.message}"
                )
                resetController()
                listener.onConnected(response)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                debug(LOG_TAG, "webSocket收到消息:\n\tbytes: $bytes")
                listener.onReceivedBinary(bytes)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                debug(LOG_TAG, "webSocket收到消息:\n\ttext: $text")
                listener.onReceivedText(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                debug(LOG_TAG, "webSocket已关闭:\n\tcode: $code\n\treason: $reason")
                listener.onClosed(code, reason)
                // 主动断开则重置状态
                if (isInitiativeDisconnect.get()) {
                    resetController()
                }
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                debug(LOG_TAG, "webSocket正在关闭:\n\tcode: $code\n\treason: $reason")
                listener.onClosing(code, reason)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                debug(LOG_TAG, "webSocket发生错误:\n\tt: $t\n\tresponse: $response")
                listener.onFailure(t, response)
                debug(LOG_TAG, "<———————————————————触发websocket断线重连———————————————————>")
                reconnect(listener)
            }
        })
    }

    override fun reconnect(listener: SocketListener) {
        // 避免并发重连
        if (isReconnecting.get()) return
        // 获取当前重连次数
        val currentAttempt = reconnectAttempts.incrementAndGet()
        // 判断是否超过最大重连次数
        if (currentAttempt > maxReconnectAttempts) {
            Log.d(LOG_TAG, "websocket重连失败: 退出websocket请求")
            listener.onReconnectFailed()
            isReconnecting.set(false)
            return
        }
        // 计算指数退避延迟：1s → 2s → 4s → ... → 30s
        val delayTime = minOf(
            baseReconnectDelay * (1 shl (currentAttempt - 1)), // 2^(n-1) 倍基础延迟
            maxReconnectDelay
        )
        Log.d(LOG_TAG, "websocket正在重连...")
        listener.onReconnecting(currentAttempt, delayTime)
        // 标记为正在重连
        isReconnecting.set(true)
        // 延迟后执行重连
        try {
            Thread.sleep(delayTime)
            // 双重检查：避免重连过程中已主动断开
            if (!isInitiativeDisconnect.get()) connect(listener) else isReconnecting.set(false)
        } catch (e: InterruptedException) {
            e.printStackTrace()
            isReconnecting.set(false)
        }
    }

    override fun disconnect() {
        isInitiativeDisconnect.set(true)
        webSocketClient?.close(closedCode, CLOSED_MESSAGE)
        webSocketClient = null // 清空引用，避免无效操作
        resetController()
    }

    override fun getClient() = webSocketClient

    private fun resetController() {
        reconnectAttempts.set(0)
        isReconnecting.set(false)
    }

    private fun OkHttpClient.connectSocket(url: String, listener: WebSocketListener): WebSocket {
        val request = Request.Builder().url(url).build()
        return newWebSocket(request, listener)
    }

    class Builder() {
        private lateinit var client: OkHttpClient
        private lateinit var wsUrl: String
        private var maxReconnectAttempts = 10 // 最大重连次数（可配置）
        private var baseReconnectDelay = 1000L // 基础重连延迟（1秒）
        private var maxReconnectDelay = 30000L // 最大重连延迟（30秒）
        private var closedCode = CLOSED_CODE // 主动断连码

        fun setUrl(url: String) = apply { wsUrl = url }
        fun setOkHttpClient(client: OkHttpClient) = apply { this.client = client }
        fun setMaxReconnectAttempts(max: Int) = apply { maxReconnectAttempts = max }
        fun setBaseReconnectDelay(baseDelay: Long) = apply { baseReconnectDelay = baseDelay }
        fun setMaxReconnectDelay(maxDelay: Long) = apply { maxReconnectDelay = maxDelay }
        fun setClosedCode(code: Int) = apply { closedCode = code }

        fun build() = WebSocketManager().apply {
            this@apply.client = this@Builder.client
            this.url = wsUrl
            this@apply.maxReconnectAttempts = this@Builder.maxReconnectAttempts
            this@apply.maxReconnectDelay = this@Builder.maxReconnectDelay
            this@apply.baseReconnectDelay = this@Builder.baseReconnectDelay
            this@apply.closedCode = this@Builder.closedCode
        }
    }


    companion object Companion {
        private const val CLOSED_CODE = -100
        private const val CLOSED_MESSAGE = "主动断开连接"
        private const val LOG_TAG = "doraemon-websocket"
    }
}
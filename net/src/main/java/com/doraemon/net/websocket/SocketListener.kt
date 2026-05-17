package com.doraemon.net.websocket

import okhttp3.Response
import okio.ByteString

interface SocketListener {
    fun onConnected(response: Response) = Unit
    fun onReceivedBinary(bytes: ByteString) = Unit
    fun onReceivedText(text: String) = Unit
    fun onClosing(code: Int, reason: String) = Unit
    fun onClosed(code: Int, reason: String) = Unit
    fun onFailure(t: Throwable, response: Response?) = Unit
    fun onReconnecting(attempt: Int, delay: Long) = Unit
    fun onReconnectFailed() = Unit
}
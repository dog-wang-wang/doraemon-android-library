package com.doraemon.net.websocket

import okhttp3.WebSocket


interface SocketManager {
    fun connect(listener: SocketListener)
    fun disconnect()
    fun reconnect(listener: SocketListener)
    fun getClient(): WebSocket?
}
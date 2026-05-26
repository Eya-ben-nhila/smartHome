package com.smarthome.data.api

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object WebSocketClient {
    private const val TAG = "WebSocketClient"
    private const val WS_URL = "ws://192.168.1.10:8080/api/ws/smarthome"
    
    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS) // Disable timeout for WebSockets
        .build()

    // SharedFlow to emit real-time messages to any observer in the app
    private val _messages = MutableSharedFlow<JSONObject>(extraBufferCapacity = 10)
    val messages = _messages.asSharedFlow()

    fun connect() {
        if (webSocket != null) return

        val request = Request.Builder()
            .url(WS_URL)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d(TAG, "WebSocket Connection Opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d(TAG, "WebSocket Message Received: $text")
                try {
                    val json = JSONObject(text)
                    _messages.tryEmit(json)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to parse WebSocket message", e)
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d(TAG, "Receiving bytes : " + bytes.hex())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d(TAG, "WebSocket Closing: $code / $reason")
                webSocket.close(1000, null)
                this@WebSocketClient.webSocket = null
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e(TAG, "WebSocket Error", t)
                this@WebSocketClient.webSocket = null
                // Attempt to reconnect after a delay could be implemented here
            }
        })
    }

    fun disconnect() {
        webSocket?.close(1000, "App closed")
        webSocket = null
    }

    fun sendMessage(message: String) {
        webSocket?.send(message)
    }
}

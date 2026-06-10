package com.muslima.yya.data.remote

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuizClient {
    private val client = HttpClient {
        install(WebSockets)
    }

    private var session: DefaultClientWebSocketSession? = null

    private val _events = MutableSharedFlow<WsMessage>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    suspend fun connect(ipAddress: String, port: Int = 8080): Boolean {
        return try {
            session = client.webSocketSession(
                method = HttpMethod.Get,
                host = ipAddress,
                port = port,
                path = "/quiz"
            )
            
            // Launch receive loop in the background of the session
            CoroutineScope(Dispatchers.Default).launch {
                try {
                    while (session?.isActive == true) {
                        val frame = session?.incoming?.receive()
                        if (frame is Frame.Text) {
                            val text = frame.readText()
                            val message = try {
                                Json.decodeFromString<WsMessage>(text)
                            } catch (e: Exception) {
                                println("QuizClient decoding error: ${e.message} for text: $text")
                                null
                            }
                            if (message != null) {
                                _events.emit(message)
                            }
                        }
                    }
                } catch (e: Exception) {
                    // Ignore expected disconnects
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun sendMessage(message: WsMessage) {
        try {
            val text = Json.encodeToString(message)
            session?.send(Frame.Text(text))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun disconnect() {
        session?.close(CloseReason(CloseReason.Codes.NORMAL, "Client disconnected"))
        session = null
    }
}

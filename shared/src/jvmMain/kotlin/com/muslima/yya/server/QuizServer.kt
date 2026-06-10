package com.muslima.yya.server

import com.muslima.yya.data.remote.WsMessage
import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.repository.QuizRepository
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds
import java.util.Collections

class QuizServer(
    private val repository: QuizRepository,
    private val port: Int = 8080
) {
    private var server: EmbeddedServer<NettyApplicationEngine, NettyApplicationEngine.Configuration>? = null
    private val connections = Collections.synchronizedSet<WebSocketSession>(LinkedHashSet())
    
    private val _events = MutableSharedFlow<WsMessage>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    fun start() {
        server = embeddedServer(Netty, port = port) {
            install(WebSockets) {
                pingPeriod = 15.seconds
                timeout = 15.seconds
                maxFrameSize = Long.MAX_VALUE
                masking = false
            }
            
            routing {
                webSocket("/quiz") {
                    println("New WebSocket connection established")
                    connections.add(this)
                    try {
                        for (frame in incoming) {
                            if (frame is Frame.Text) {
                                val text = frame.readText()
                                println("Received message from client: $text")
                                val message = try {
                                    Json.decodeFromString<WsMessage>(text)
                                } catch(e: Exception) {
                                    println("QuizServer decoding error: ${e.message} for text: $text")
                                    null
                                }
                                if (message != null) {
                                    handleMessage(this, message)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        connections.remove(this)
                    }
                }
            }
        }.start(wait = false)
    }

    private suspend fun handleMessage(session: WebSocketSession, message: WsMessage) {
        when (message) {
            is WsMessage.Join -> {
                // Client joined
                repository.createStudentIgnore(message.student)
                _events.emit(WsMessage.StudentJoined(message.student, message.quizId))
            }
            is WsMessage.SubmitAnswer -> {
                // Check answer and update score (simplified, real logic can be complex)
                // For simplicity, we just pass to ViewModel/Domain to check or we can check here.
                _events.emit(message)
            }
            else -> {}
        }
    }

    suspend fun broadcast(message: WsMessage) {
        val text = Json.encodeToString(message)
        println("Broadcasting message: $text")
        val frame = Frame.Text(text)
        val snapshot = connections.toList()
        println("Broadcasting to ${snapshot.size} connected clients")
        snapshot.forEach {
            try {
                it.send(frame)
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    fun stop() {
        server?.stop(1000, 1000)
    }
}

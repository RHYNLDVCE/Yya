package com.muslima.yya.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslima.yya.data.remote.QuizClient
import com.muslima.yya.data.remote.WsMessage
import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.model.LeaderboardEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

data class StudentState(
    val isConnected: Boolean = false,
    val studentName: String = "",
    val studentId: String = "",
    val quizTitle: String = "",
    val currentQuizId: String = "",
    val currentQuestion: Question? = null,
    val timeLimitMs: Long = 0,
    val leaderboard: List<LeaderboardEntry> = emptyList(),
    val questionStartTime: Long = 0,
    val answered: Boolean = false,
    val isCorrect: Boolean? = null,
    val quizEnded: Boolean = false,
    val joinError: String? = null
)

class StudentViewModel(
    private val client: QuizClient
) : ViewModel() {

    private val _state = MutableStateFlow(StudentState())
    val state: StateFlow<StudentState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            client.events.collect { event ->
                when (event) {
                    is WsMessage.NextQuestion -> {
                        _state.update {
                            it.copy(
                                currentQuestion = event.question,
                                timeLimitMs = event.timeLimitMs,
                                questionStartTime = Clock.System.now().toEpochMilliseconds(),
                                answered = false,
                                isCorrect = null
                            )
                        }
                    }
                    is WsMessage.AnswerResult -> {
                        if (event.studentId == _state.value.studentId) {
                            _state.update { it.copy(isCorrect = event.isCorrect) }
                        }
                    }
                    is WsMessage.LeaderboardUpdate -> {
                        _state.update { it.copy(leaderboard = event.students) }
                    }
                    is WsMessage.QuizEnded -> {
                        _state.update { it.copy(quizEnded = true, leaderboard = event.leaderboard, currentQuestion = null) }
                    }
                    is WsMessage.JoinRejected -> {
                        if (event.studentId == _state.value.studentId) {
                            disconnect(error = event.reason)
                        }
                    }
                    else -> {}
                }
            }
        }
    }

    fun joinQuiz(qrData: String) {
        // qrData expected to be like ws://192.168.1.5:8080/quiz?studentId=123&quizId=456&name=John_Doe
        val uri = qrData.substringBefore("?")
        val paramsPart = qrData.substringAfter("?", "")
        val params = paramsPart.split("&").filter { it.contains("=") }.associate { 
            val parts = it.split("=")
            parts[0] to parts[1]
        }
        
        val uriWithoutProtocol = uri.removePrefix("ws://").removeSuffix("/quiz")
        val parts = uriWithoutProtocol.split(":")
        val ip = parts.getOrNull(0) ?: return
        val port = parts.getOrNull(1)?.toIntOrNull() ?: 8080

        val studentId = params["studentId"] ?: "unknown"
        val nameStr = params["name"]?.replace("_", " ") ?: "Unknown Student"
        val nameParts = nameStr.split(" ")
        val firstName = nameParts.firstOrNull() ?: ""
        val lastName = nameParts.drop(1).joinToString(" ")
        
        val quizTitle = params["quizTitle"]?.replace("_", " ") ?: "Quiz"
        val quizId = params["quizId"] ?: "unknown_quiz"

        _state.update { it.copy(studentName = nameStr, studentId = studentId, quizTitle = quizTitle, currentQuizId = quizId) }

        val student = Student(
            id = studentId,
            firstName = firstName,
            lastName = lastName,
            studentNumber = "",
            age = 0,
            gender = "",
            yearLevel = ""
        )

        viewModelScope.launch {
            val success = client.connect(ip, port)
            if (success) {
                _state.update { it.copy(isConnected = true) }
                client.sendMessage(WsMessage.Join(student, quizId))
            } else {
                // Connection failed, reset state or show error
            }
        }
    }

    fun submitAnswer(answer: String) {
        if (_state.value.answered) return
        val timeTaken = Clock.System.now().toEpochMilliseconds() - _state.value.questionStartTime
        _state.update { it.copy(answered = true) }
        viewModelScope.launch {
            client.sendMessage(WsMessage.SubmitAnswer(_state.value.studentId, answer, timeTaken))
        }
    }
    
    fun disconnect(error: String? = null) {
        viewModelScope.launch {
            client.disconnect()
            _state.update { StudentState(joinError = error) }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}

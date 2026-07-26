package com.muslima.yya.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muslima.yya.data.remote.WsMessage
import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Quiz
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.model.LeaderboardEntry
import com.muslima.yya.domain.repository.QuizRepository
import com.muslima.yya.server.QuizServer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.UUID

data class AdminState(
    val serverRunning: Boolean = false,
    val serverIp: String = "localhost", // Should be real IP in prod
    val currentQuiz: Quiz? = null,
    val connectedStudents: List<LeaderboardEntry> = emptyList(),
    val currentQuestion: Question? = null,
    val qrCodeContent: String? = null,
    val isLoggedIn: Boolean = false,
    val studentsList: List<Student> = emptyList(),
    val quizzesList: List<Quiz> = emptyList(),
    val selectedQuizDetail: Quiz? = null,
    val dashboardLeaderboards: Map<String, List<LeaderboardEntry>> = emptyMap(),
    val sentQuestionIds: Set<String> = emptySet(),
    val answeredStudentsForCurrentQuestion: Set<String> = emptySet(),
    val isAcceptingAnswers: Boolean = false,
    val isQuizStarted: Boolean = false
)

class AdminViewModel(
    private val repository: QuizRepository,
    private val server: QuizServer
) : ViewModel() {

    private val _state = MutableStateFlow(AdminState())
    val state: StateFlow<AdminState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            loadStudents()
            loadQuizzes()
            server.events.collect { event ->
                when (event) {
                    is WsMessage.StudentJoined -> {
                        val existingEntry = state.value.connectedStudents.find { it.studentId == event.student.id }
                        if (existingEntry == null) {
                            val entry = LeaderboardEntry(studentId = event.student.id, quizId = event.quizId, studentName = "${event.student.firstName} ${event.student.lastName}")
                            _state.update { it.copy(connectedStudents = it.connectedStudents + entry) }
                            repository.createLeaderboardEntry(entry)
                        } else {
                            server.broadcast(WsMessage.JoinRejected(event.student.id, "You have already completed this quiz!"))
                        }
                        server.broadcast(WsMessage.LeaderboardUpdate(_state.value.connectedStudents))
                        loadStudents() // Refresh list just in case
                    }
                    is WsMessage.SubmitAnswer -> {
                        val currentState = state.value
                        if (!currentState.isAcceptingAnswers) {
                            return@collect
                        }
                        if (currentState.answeredStudentsForCurrentQuestion.contains(event.studentId)) {
                            // Ignore duplicate submission
                            return@collect
                        }
                        _state.update { it.copy(answeredStudentsForCurrentQuestion = it.answeredStudentsForCurrentQuestion + event.studentId) }

                        val isCorrect = currentState.currentQuestion?.correctAnswer == event.answer
                        val pointsEarned = if (isCorrect) (currentState.currentQuestion?.points?.toLong() ?: 10L) else 0L
                        val currentQuizId = currentState.currentQuiz?.id
                        if (currentQuizId != null) {
                            if (isCorrect) {
                                repository.updateLeaderboardScore(event.studentId, currentQuizId, pointsEarned, event.timeTakenMs)
                            } else {
                                repository.updateLeaderboardScore(event.studentId, currentQuizId, 0, event.timeTakenMs)
                            }
                        }
                        
                        // Update local state leaderboard
                        _state.update { state ->
                            val updatedStudents = state.connectedStudents.map {
                                if (it.studentId == event.studentId) {
                                    it.copy(score = it.score + pointsEarned, timeTakenMs = it.timeTakenMs + event.timeTakenMs)
                                } else it
                            }
                            state.copy(connectedStudents = updatedStudents)
                        }
                        server.broadcast(WsMessage.LeaderboardUpdate(_state.value.connectedStudents))

                        server.broadcast(WsMessage.AnswerResult(event.studentId, isCorrect, currentState.currentQuestion?.correctAnswer ?: ""))
                    }
                    else -> {}
                }
            }
        }
    }

    private suspend fun loadStudents() {
        val students = repository.getStudents()
        _state.update { it.copy(studentsList = students) }
    }

    private suspend fun loadQuizzes() {
        val quizzes = repository.getQuizzes()
        _state.update { it.copy(quizzesList = quizzes) }
    }

    fun fetchStudents() {
        viewModelScope.launch {
            loadStudents()
        }
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            repository.createStudent(student)
            loadStudents()
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
            loadStudents()
        }
    }

    fun deleteStudent(id: String) {
        viewModelScope.launch {
            repository.deleteStudent(id)
            loadStudents()
        }
    }

    fun startServer(ipAddress: String) {
        server.start()
        _state.update { it.copy(serverRunning = true, serverIp = ipAddress, qrCodeContent = "ws://$ipAddress:8080/quiz") }
    }
    private fun getLocalIpAddress(): String {
        return try {
            java.net.DatagramSocket().use { socket ->
                socket.connect(java.net.InetAddress.getByName("8.8.8.8"), 10002)
                socket.localAddress.hostAddress ?: "127.0.0.1"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "127.0.0.1"
        }
    }

    fun login(user: String, pass: String) {
        if (user == "yya" && pass == "asaddimuslima") {
            _state.update { it.copy(isLoggedIn = true) }
            val ip = getLocalIpAddress()
            startServer(ip)
        }
    }
    
    fun stopServer() {
        server.stop()
        _state.update { it.copy(serverRunning = false, qrCodeContent = null) }
    }

    fun createQuiz(title: String): Quiz {
        val quiz = Quiz(id = UUID.randomUUID().toString(), title = title, questions = emptyList())
        viewModelScope.launch {
            repository.createQuiz(quiz)
            loadQuizzes()
        }
        return quiz
    }

    fun selectQuizForDetail(quiz: Quiz?) {
        _state.update { it.copy(selectedQuizDetail = quiz, isQuizStarted = false) }
        viewModelScope.launch {
            if (quiz != null) {
                val leaderboard = repository.getLeaderboard(quiz.id).first()
                _state.update { it.copy(connectedStudents = leaderboard) }
            } else {
                _state.update { it.copy(connectedStudents = emptyList()) }
            }
        }
    }

    fun loadAllDashboardLeaderboards() {
        viewModelScope.launch {
            val leaderboards = mutableMapOf<String, List<LeaderboardEntry>>()
            for (quiz in state.value.quizzesList) {
                leaderboards[quiz.id] = repository.getLeaderboard(quiz.id).first()
            }
            _state.update { it.copy(dashboardLeaderboards = leaderboards) }
        }
    }

    fun addQuestionToQuiz(quizId: String, text: String, options: List<String>, correctAnswer: String, points: Int, timeLimitSeconds: Int) {
        val quiz = state.value.quizzesList.find { it.id == quizId } ?: return
        val newQuestion = Question(
            id = UUID.randomUUID().toString(),
            quizId = quizId,
            text = text,
            correctAnswer = correctAnswer,
            options = options,
            points = points,
            timeLimitSeconds = timeLimitSeconds
        )
        val updatedQuiz = quiz.copy(questions = quiz.questions + newQuestion)
        updateQuiz(updatedQuiz)
        _state.update { it.copy(selectedQuizDetail = updatedQuiz, currentQuiz = updatedQuiz) }
    }

    fun updateQuestionInQuiz(quizId: String, updatedQuestion: Question) {
        val quiz = state.value.quizzesList.find { it.id == quizId } ?: return
        val updatedQuestions = quiz.questions.map { if (it.id == updatedQuestion.id) updatedQuestion else it }
        val updatedQuiz = quiz.copy(questions = updatedQuestions)
        updateQuiz(updatedQuiz)
        _state.update { it.copy(selectedQuizDetail = updatedQuiz, currentQuiz = updatedQuiz) }
    }

    fun generateQrForStudent(studentId: String, quizId: String) {
        val ip = state.value.serverIp
        val student = state.value.studentsList.find { it.id == studentId }
        val name = student?.let { "${it.firstName}_${it.lastName}" } ?: "Unknown"
        val quiz = state.value.quizzesList.find { it.id == quizId }
        val quizTitle = quiz?.title?.replace(" ", "_") ?: "Quiz"
        val url = "ws://$ip:8080/quiz?studentId=$studentId&quizId=$quizId&name=$name&quizTitle=$quizTitle"
        _state.update { it.copy(qrCodeContent = url) }
    }

    fun updateQuiz(quiz: Quiz) {
        viewModelScope.launch {
            repository.updateQuiz(quiz)
            loadQuizzes()
        }
    }

    fun deleteQuiz(id: String) {
        viewModelScope.launch {
            repository.deleteQuiz(id)
            loadQuizzes()
        }
    }

    fun sendQuestion(question: Question) {
        _state.update { 
            it.copy(
                currentQuestion = question, 
                currentQuiz = state.value.selectedQuizDetail,
                sentQuestionIds = it.sentQuestionIds + question.id,
                answeredStudentsForCurrentQuestion = emptySet(),
                isAcceptingAnswers = true
            ) 
        }
        viewModelScope.launch {
            server.broadcast(WsMessage.NextQuestion(question, timeLimitMs = question.timeLimitSeconds * 1000L))
            
            kotlinx.coroutines.delay(question.timeLimitSeconds * 1000L)
            if (_state.value.currentQuestion?.id == question.id) {
                _state.update { it.copy(isAcceptingAnswers = false) }
            }
        }
    }

    fun startQuiz() {
        _state.update { it.copy(isQuizStarted = true, sentQuestionIds = emptySet()) }
    }

    fun stopQuiz() {
        _state.update { it.copy(isQuizStarted = false, currentQuestion = null) }
    }

    fun endQuizAndPublishScores() {
        viewModelScope.launch {
            server.broadcast(WsMessage.QuizEnded(state.value.connectedStudents))
        }
        stopQuiz()
    }
}

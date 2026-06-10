package com.muslima.yya.data.remote

import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.model.LeaderboardEntry
import kotlinx.serialization.Serializable

@Serializable
sealed class WsMessage {
    @Serializable
    data class Join(val student: Student, val quizId: String) : WsMessage()

    @Serializable
    data class StudentJoined(val student: Student, val quizId: String) : WsMessage()
    
    @Serializable
    data class NextQuestion(val question: Question, val timeLimitMs: Long) : WsMessage()
    
    @Serializable
    data class SubmitAnswer(val studentId: String, val answer: String, val timeTakenMs: Long) : WsMessage()
    
    @Serializable
    data class AnswerResult(val studentId: String, val isCorrect: Boolean, val correctAnswer: String) : WsMessage()
    
    @Serializable
    data class LeaderboardUpdate(val students: List<LeaderboardEntry>) : WsMessage()

    @Serializable
    data class QuizEnded(val leaderboard: List<LeaderboardEntry>) : WsMessage()
}

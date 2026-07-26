package com.muslima.yya.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Quiz(
    val id: String,
    val title: String,
    val questions: List<Question>
)

@Serializable
data class Question(
    val id: String,
    val quizId: String,
    val text: String,
    val correctAnswer: String,
    val options: List<String>,
    val points: Int = 10,
    val timeLimitSeconds: Int = 15
)

@Serializable
data class Student(
    val id: String,
    val firstName: String,
    val lastName: String,
    val studentNumber: String,
    val age: Int,
    val gender: String,
    val yearLevel: String
)

@Serializable
data class LeaderboardEntry(
    val studentId: String,
    val quizId: String,
    val studentName: String,
    val score: Long = 0,
    val timeTakenMs: Long = 0
)

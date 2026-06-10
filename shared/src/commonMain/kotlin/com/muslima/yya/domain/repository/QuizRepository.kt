package com.muslima.yya.domain.repository

import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Quiz
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.model.LeaderboardEntry
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun createQuiz(quiz: Quiz)
    suspend fun getQuizzes(): List<Quiz>
    suspend fun updateQuiz(quiz: Quiz)
    suspend fun deleteQuiz(id: String)
    
    suspend fun createStudent(student: Student)
    suspend fun createStudentIgnore(student: Student)
    suspend fun getStudents(): List<Student>
    suspend fun updateStudent(student: Student)
    suspend fun deleteStudent(id: String)

    suspend fun createLeaderboardEntry(entry: LeaderboardEntry)
    suspend fun updateLeaderboardScore(studentId: String, quizId: String, additionalScore: Long, timeTakenMs: Long)
    suspend fun getLeaderboard(quizId: String): Flow<List<LeaderboardEntry>>
}

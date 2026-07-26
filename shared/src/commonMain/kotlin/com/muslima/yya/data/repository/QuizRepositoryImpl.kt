package com.muslima.yya.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.muslima.yya.database.QuizDatabase
import com.muslima.yya.domain.model.Question
import com.muslima.yya.domain.model.Quiz
import com.muslima.yya.domain.model.Student
import com.muslima.yya.domain.model.LeaderboardEntry
import com.muslima.yya.domain.repository.QuizRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class QuizRepositoryImpl(
    private val db: QuizDatabase
) : QuizRepository {

    private val queries = db.quizQueries

    override suspend fun createQuiz(quiz: Quiz) {
        db.transaction {
            queries.insertQuiz(quiz.id, quiz.title)
            quiz.questions.forEach { q ->
                queries.insertQuestion(
                    id = q.id,
                    quizId = q.quizId,
                    text = q.text,
                    correctAnswer = q.correctAnswer,
                    options = Json.encodeToString(q.options),
                    points = q.points.toLong(),
                    timeLimitSeconds = q.timeLimitSeconds.toLong()
                )
            }
        }
    }

    override suspend fun getQuizzes(): List<Quiz> {
        val quizEntities = queries.getQuizzes().executeAsList()
        return quizEntities.map { entity ->
            val questionEntities = queries.getQuestionsForQuiz(entity.id).executeAsList()
            val questions = questionEntities.map { q ->
                Question(
                    id = q.id,
                    quizId = q.quizId,
                    text = q.text,
                    correctAnswer = q.correctAnswer,
                    options = Json.decodeFromString(q.options),
                    points = q.points.toInt(),
                    timeLimitSeconds = q.timeLimitSeconds.toInt()
                )
            }
            Quiz(id = entity.id, title = entity.title, questions = questions)
        }
    }
    override suspend fun updateQuiz(quiz: Quiz) {
        db.transaction {
            queries.updateQuiz(title = quiz.title, id = quiz.id)
            queries.deleteQuestionsForQuiz(quizId = quiz.id)
            quiz.questions.forEach { q ->
                queries.insertQuestion(
                    id = q.id,
                    quizId = quiz.id,
                    text = q.text,
                    correctAnswer = q.correctAnswer,
                    options = Json.encodeToString(q.options),
                    points = q.points.toLong(),
                    timeLimitSeconds = q.timeLimitSeconds.toLong()
                )
            }
        }
    }

    override suspend fun deleteQuiz(id: String) {
        db.transaction {
            queries.deleteQuestionsForQuiz(quizId = id)
            queries.deleteQuiz(id = id)
        }
    }
    override suspend fun createStudent(student: Student) {
        queries.insertStudent(
            id = student.id,
            firstName = student.firstName,
            lastName = student.lastName,
            studentNumber = student.studentNumber,
            age = student.age.toLong(),
            gender = student.gender,
            yearLevel = student.yearLevel
        )
    }

    override suspend fun createStudentIgnore(student: Student) {
        queries.insertStudentIgnore(
            id = student.id,
            firstName = student.firstName,
            lastName = student.lastName,
            studentNumber = student.studentNumber,
            age = student.age.toLong(),
            gender = student.gender,
            yearLevel = student.yearLevel
        )
    }

    override suspend fun getStudents(): List<Student> {
        val studentEntities = queries.getStudents().executeAsList()
        return studentEntities.map { entity ->
            Student(
                id = entity.id,
                firstName = entity.firstName,
                lastName = entity.lastName,
                studentNumber = entity.studentNumber,
                age = entity.age.toInt(),
                gender = entity.gender,
                yearLevel = entity.yearLevel
            )
        }
    }

    override suspend fun updateStudent(student: Student) {
        queries.updateStudent(
            firstName = student.firstName,
            lastName = student.lastName,
            studentNumber = student.studentNumber,
            age = student.age.toLong(),
            gender = student.gender,
            yearLevel = student.yearLevel,
            id = student.id
        )
    }

    override suspend fun deleteStudent(id: String) {
        queries.deleteStudent(id)
    }

    override suspend fun createLeaderboardEntry(entry: LeaderboardEntry) {
        queries.insertLeaderboardEntry(
            studentId = entry.studentId,
            quizId = entry.quizId,
            studentName = entry.studentName,
            score = entry.score,
            timeTakenMs = entry.timeTakenMs
        )
    }

    override suspend fun updateLeaderboardScore(studentId: String, quizId: String, additionalScore: Long, timeTakenMs: Long) {
        queries.updateLeaderboardScore(score = additionalScore, timeTakenMs = timeTakenMs, studentId = studentId, quizId = quizId)
    }

    override suspend fun getLeaderboard(quizId: String): Flow<List<LeaderboardEntry>> {
        return queries.getLeaderboardEntries(quizId = quizId).asFlow().mapToList(Dispatchers.Default).map { list ->
            list.map { LeaderboardEntry(it.studentId, it.quizId, it.studentName, it.score, it.timeTakenMs) }
        }
    }
}

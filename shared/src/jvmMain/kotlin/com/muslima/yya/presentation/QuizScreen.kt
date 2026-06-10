package com.muslima.yya.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import com.muslima.yya.domain.model.Question
import dev.chrisbanes.haze.hazeEffect

@Composable
fun QuizListSection(viewModel: AdminViewModel, state: AdminState) {
    val cardShape = RoundedCornerShape(32.dp)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
        colors = CardDefaults.cardColors(containerColor = GlassContainer),
        border = BorderStroke(1.dp, GlassBorder),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.padding(40.dp).fillMaxSize()) {
            Text("All Quizzes", style = MaterialTheme.typography.headlineMedium, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.quizzesList) { quiz ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { viewModel.selectQuizForDetail(quiz) },
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(quiz.title, fontWeight = FontWeight.Bold, color = TextPrimary)
                            Text("${quiz.questions.size} Questions", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizUpdateSection(viewModel: AdminViewModel, state: AdminState) {
    var selectedQuiz by remember { mutableStateOf<com.muslima.yya.domain.model.Quiz?>(null) }
    val cardShape = RoundedCornerShape(32.dp)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
        colors = CardDefaults.cardColors(containerColor = GlassContainer),
        border = BorderStroke(1.dp, GlassBorder),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 16.dp)) {
                Text("Select Quiz", style = MaterialTheme.typography.headlineSmall, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(state.quizzesList) { quiz ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedQuiz = quiz },
                            colors = CardDefaults.cardColors(containerColor = if (selectedQuiz?.id == quiz.id) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Text(quiz.title, modifier = Modifier.padding(16.dp), color = TextPrimary, fontWeight = if (selectedQuiz?.id == quiz.id) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            if (selectedQuiz != null) {
                var title by remember(selectedQuiz) { mutableStateOf(selectedQuiz!!.title) }

                Column(modifier = Modifier.weight(2f).fillMaxHeight().padding(16.dp)) {
                    Text("Update Details", style = MaterialTheme.typography.headlineSmall, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Quiz Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = DarkTextFieldColors,
                        shape = RoundedCornerShape(16.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.updateQuiz(selectedQuiz!!.copy(title = title))
                            selectedQuiz = null
                        },
                        modifier = Modifier.align(Alignment.End).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Update Quiz", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                Box(modifier = Modifier.weight(2f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Text("Select a quiz to update", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun QuizDeleteSection(viewModel: AdminViewModel, state: AdminState) {
    val cardShape = RoundedCornerShape(32.dp)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
        colors = CardDefaults.cardColors(containerColor = GlassContainer),
        border = BorderStroke(1.dp, GlassBorder),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.padding(40.dp).fillMaxSize()) {
            Text("Delete Quizzes", style = MaterialTheme.typography.headlineMedium, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.quizzesList) { quiz ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(quiz.title, fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("${quiz.questions.size} Questions", color = TextSecondary)
                            }
                            Button(
                                onClick = { viewModel.deleteQuiz(quiz.id) },
                                colors = ButtonDefaults.buttonColors(containerColor = SoftCoral),
                                shape = RoundedCornerShape(24.dp)
                            ) {
                                Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddQuizSection(viewModel: AdminViewModel) {
    var title by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var newlyCreatedQuiz by remember { mutableStateOf<com.muslima.yya.domain.model.Quiz?>(null) }
    val cardShape = RoundedCornerShape(32.dp)

    Card(
        modifier = Modifier
            .fillMaxSize()
            .hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
        colors = CardDefaults.cardColors(containerColor = GlassContainer),
        border = BorderStroke(1.dp, GlassBorder),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(40.dp)) {
            Text("Create New Quiz", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = VibrantPink)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Quiz Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = DarkTextFieldColors,
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    newlyCreatedQuiz = viewModel.createQuiz(title)
                    showDialog = true
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.align(Alignment.End).height(56.dp).width(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Create Quiz", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                title = ""
            },
            title = { Text("Quiz Created!", color = VibrantPink, fontWeight = FontWeight.Bold) },
            text = { Text("Your quiz was created successfully. Do you want to add questions now?", color = TextPrimary) },
            containerColor = Color(0xFF1E1E2E), // Solid dark color for dialogs
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        title = ""
                        viewModel.selectQuizForDetail(newlyCreatedQuiz)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha)
                ) {
                    Text("Yes", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    title = ""
                }) {
                    Text("Do it later", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
fun QuizDetailSection(viewModel: AdminViewModel, state: AdminState) {
    val quiz = state.selectedQuizDetail ?: return
    var newQuestionText by remember { mutableStateOf("") }
    var optionA by remember { mutableStateOf("") }
    var optionB by remember { mutableStateOf("") }
    var optionC by remember { mutableStateOf("") }
    var optionD by remember { mutableStateOf("") }
    var correctOption by remember { mutableStateOf("A") }
    var points by remember { mutableStateOf("10") }
    var editingQuestionId by remember { mutableStateOf<String?>(null) }

    var selectedStudentId by remember { mutableStateOf<String?>(null) }
    var rightPanelTab by remember { mutableStateOf("Leaderboard") }

    val cardShape = RoundedCornerShape(32.dp)

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Setup left column
        Card(
            modifier = Modifier.weight(1f).fillMaxHeight().hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
            colors = CardDefaults.cardColors(containerColor = GlassContainer),
            border = BorderStroke(1.dp, GlassBorder),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = cardShape
        ) {
            Column(modifier = Modifier.padding(32.dp).fillMaxSize()) {
                Text("Quiz: ${quiz.title}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = VibrantPink)
                Spacer(modifier = Modifier.height(24.dp))

                Text(if (editingQuestionId != null) "Update Question" else "Add Question", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = newQuestionText,
                    onValueChange = { newQuestionText = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = DarkTextFieldColors,
                    shape = RoundedCornerShape(16.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = optionA, onValueChange = { optionA = it }, label = { Text("Option A") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    OutlinedTextField(value = optionB, onValueChange = { optionB = it }, label = { Text("Option B") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = optionC, onValueChange = { optionC = it }, label = { Text("Option C") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    OutlinedTextField(value = optionD, onValueChange = { optionD = it }, label = { Text("Option D") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Correct Answer:", color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(0.6f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("A", "B", "C", "D").forEach { opt ->
                                Button(
                                    onClick = { correctOption = opt },
                                    colors = ButtonDefaults.buttonColors(containerColor = if (correctOption == opt) VibrantPink else Color.White.copy(alpha = 0.1f)),
                                    shape = RoundedCornerShape(16.dp),
                                    elevation = ButtonDefaults.buttonElevation(0.dp)
                                ) {
                                    Text(opt, color = Color.White)
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        value = points,
                        onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) points = it },
                        label = { Text("Points") },
                        modifier = Modifier.width(100.dp),
                        colors = DarkTextFieldColors,
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    if (editingQuestionId != null) {
                        TextButton(onClick = {
                            editingQuestionId = null
                            newQuestionText = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correctOption = "A"; points = "10"
                        }) {
                            Text("Cancel", color = TextSecondary)
                        }
                    }
                    Button(
                        onClick = {
                            val answerText = when (correctOption) {
                                "A" -> optionA
                                "B" -> optionB
                                "C" -> optionC
                                else -> optionD
                            }
                            val pointsInt = points.toIntOrNull() ?: 10
                            if (editingQuestionId != null) {
                                val updatedQuestion = Question(
                                    id = editingQuestionId!!,
                                    quizId = quiz.id,
                                    text = newQuestionText,
                                    correctAnswer = answerText,
                                    options = listOf(optionA, optionB, optionC, optionD),
                                    points = pointsInt
                                )
                                viewModel.updateQuestionInQuiz(quiz.id, updatedQuestion)
                                editingQuestionId = null
                            } else {
                                viewModel.addQuestionToQuiz(quiz.id, newQuestionText, listOf(optionA, optionB, optionC, optionD), answerText, pointsInt)
                            }
                            newQuestionText = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correctOption = "A"; points = "10"
                        },
                        enabled = newQuestionText.isNotBlank() && optionA.isNotBlank() && optionB.isNotBlank() && optionC.isNotBlank() && optionD.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(if (editingQuestionId != null) "Update Question" else "Add Question", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text("Questions (${quiz.questions.size}):", fontWeight = FontWeight.Bold, color = TextPrimary)
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(quiz.questions) { q ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("- ${q.text}", color = TextPrimary, fontWeight = FontWeight.Bold)
                                    Text("Answer: ${q.correctAnswer} (${q.points} pts)", color = TextSecondary, fontSize = 12.sp)
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    IconButton(
                                        onClick = {
                                            editingQuestionId = q.id
                                            newQuestionText = q.text
                                            optionA = q.options.getOrNull(0) ?: ""
                                            optionB = q.options.getOrNull(1) ?: ""
                                            optionC = q.options.getOrNull(2) ?: ""
                                            optionD = q.options.getOrNull(3) ?: ""
                                            correctOption = when (q.correctAnswer) {
                                                optionA -> "A"
                                                optionB -> "B"
                                                optionC -> "C"
                                                optionD -> "D"
                                                else -> "A"
                                            }
                                            points = q.points.toString()
                                        }
                                    ) {
                                        Icon(Icons.Default.Edit, contentDescription = "Edit Question", tint = VibrantPink)
                                    }
                                    Button(
                                        onClick = { viewModel.sendQuestion(q) },
                                        colors = ButtonDefaults.buttonColors(containerColor = VibrantPink),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Text("Send", fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Setup right column
        Card(
            modifier = Modifier.weight(0.6f).fillMaxHeight().hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
            colors = CardDefaults.cardColors(containerColor = GlassContainer),
            border = BorderStroke(1.dp, GlassBorder),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = cardShape
        ) {
            Column(modifier = Modifier.padding(24.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { rightPanelTab = "Leaderboard" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (rightPanelTab == "Leaderboard") DarkMatcha else Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(topStart = 24.dp, bottomStart = 24.dp, topEnd = 0.dp, bottomEnd = 0.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Leaderboard", color = Color.White)
                    }
                    Button(
                        onClick = { rightPanelTab = "Scan" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (rightPanelTab == "Scan") DarkMatcha else Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 24.dp, bottomEnd = 24.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Scan Student", color = Color.White)
                    }
                }

                if (rightPanelTab == "Scan") {
                    Text("Join this Quiz", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(16.dp))

                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        Button(
                            onClick = { expanded = true },
                            colors = ButtonDefaults.buttonColors(containerColor = VibrantPink),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(if (selectedStudentId == null) "Select Student for QR" else state.studentsList.find { it.id == selectedStudentId }?.let { "${it.firstName} ${it.lastName}" } ?: "Select Student", fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier
                                .background(Color(0xFF1E1E2E).copy(alpha = 0.95f), RoundedCornerShape(16.dp))
                                .heightIn(max = 350.dp)
                                .widthIn(min = 250.dp)
                        ) {
                            if (state.studentsList.isEmpty()) {
                                DropdownMenuItem(
                                    text = { Text("No students available", color = TextSecondary) },
                                    onClick = { expanded = false }
                                )
                            } else {
                                state.studentsList.forEach { student ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .background(DarkMatcha.copy(alpha = 0.3f), RoundedCornerShape(20.dp)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = "${student.firstName.firstOrNull() ?: ""}${student.lastName.firstOrNull() ?: ""}".uppercase(),
                                                        color = TextPrimary,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.width(12.dp))

                                                Column {
                                                    Text(
                                                        text = "${student.firstName} ${student.lastName}",
                                                        color = TextPrimary,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 16.sp
                                                    )
                                                    Text(
                                                        text = "No: ${student.studentNumber} • Yr: ${student.yearLevel}",
                                                        color = TextSecondary,
                                                        fontSize = 12.sp
                                                    )
                                                }
                                            }
                                        },
                                        onClick = {
                                            selectedStudentId = student.id
                                            viewModel.generateQrForStudent(student.id, quiz.id)
                                            expanded = false
                                        }
                                    )
                                    if (student != state.studentsList.last()) {
                                        HorizontalDivider(color = Color.White.copy(alpha = 0.05f), thickness = 1.dp)
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    if (selectedStudentId != null) {
                        state.qrCodeContent?.let {
                            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(8.dp)) {
                                val bitmap = com.muslima.yya.utils.QrCodeGenerator.generate(it)
                                Image(bitmap = bitmap, contentDescription = "QR Code", modifier = Modifier.size(200.dp).padding(16.dp))
                            }
                        }
                    }
                } else {
                    Text("Leaderboard", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(state.connectedStudents.sortedByDescending { it.score }) { student ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                                shape = RoundedCornerShape(20.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(student.studentName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                    Text("${student.score} pts", color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
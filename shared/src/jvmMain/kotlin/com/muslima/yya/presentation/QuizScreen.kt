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


@Composable
fun QuizListSection(viewModel: AdminViewModel, state: AdminState) {
    val cardShape = RoundedCornerShape(8.dp)
    Card(
        modifier = Modifier
            .fillMaxSize()
            ,
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.padding(40.dp).fillMaxSize()) {
            Text("All Quizzes", style = MaterialTheme.typography.headlineMedium, color = PrimaryAccent, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.quizzesList) { quiz ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable { viewModel.selectQuizForDetail(quiz) },
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
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
    val cardShape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .fillMaxSize()
            ,
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Column(modifier = Modifier.weight(1f).fillMaxHeight().padding(end = 16.dp)) {
                Text("Select Quiz", style = MaterialTheme.typography.headlineSmall, color = PrimaryAccent, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(state.quizzesList) { quiz ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedQuiz = quiz },
                            colors = CardDefaults.cardColors(containerColor = if (selectedQuiz?.id == quiz.id) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(8.dp),
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
                    Text("Update Details", style = MaterialTheme.typography.headlineSmall, color = PrimaryAccent, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Quiz Title") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = DarkTextFieldColors,
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.updateQuiz(selectedQuiz!!.copy(title = title))
                            selectedQuiz = null
                        },
                        modifier = Modifier.align(Alignment.End).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent),
                        shape = RoundedCornerShape(12.dp)
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
    val cardShape = RoundedCornerShape(8.dp)
    Card(
        modifier = Modifier
            .fillMaxSize()
            ,
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.padding(40.dp).fillMaxSize()) {
            Text("Delete Quizzes", style = MaterialTheme.typography.headlineMedium, color = PrimaryAccent, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.quizzesList) { quiz ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(12.dp),
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
                                colors = ButtonDefaults.buttonColors(containerColor = WarningAccent),
                                shape = RoundedCornerShape(12.dp)
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
    val cardShape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .fillMaxSize()
            ,
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(40.dp)) {
            Text("Create New Quiz", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = PrimaryAccent)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Quiz Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = DarkTextFieldColors,
                shape = RoundedCornerShape(8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    newlyCreatedQuiz = viewModel.createQuiz(title)
                    showDialog = true
                },
                enabled = title.isNotBlank(),
                modifier = Modifier.align(Alignment.End).height(56.dp).width(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent),
                shape = RoundedCornerShape(12.dp)
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
            title = { Text("Quiz Created!", color = PrimaryAccent, fontWeight = FontWeight.Bold) },
            text = { Text("Your quiz was created successfully. Do you want to add questions now?", color = TextPrimary) },
            containerColor = Color(0xFF1E1E2E), // Solid dark color for dialogs
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        title = ""
                        viewModel.selectQuizForDetail(newlyCreatedQuiz)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent)
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
fun QuizRightPanel(viewModel: AdminViewModel, state: AdminState, quiz: com.muslima.yya.domain.model.Quiz, showScanOption: Boolean = true) {
    var selectedStudentId by remember { mutableStateOf<String?>(null) }
    var rightPanelTab by remember { mutableStateOf("Leaderboard") }
    val cardShape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier.fillMaxHeight(),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        border = BorderStroke(1.dp, BorderColor),
        elevation = CardDefaults.cardElevation(0.dp),
        shape = cardShape
    ) {
        Column(modifier = Modifier.padding(24.dp).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (showScanOption) {
                Row(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp), horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { rightPanelTab = "Leaderboard" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (rightPanelTab == "Leaderboard") PrimaryAccent else SecondaryAccent),
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp, topEnd = 0.dp, bottomEnd = 0.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Leaderboard", color = if (rightPanelTab == "Leaderboard") Color.White else TextPrimary)
                    }
                    Button(
                        onClick = { rightPanelTab = "Scan" },
                        colors = ButtonDefaults.buttonColors(containerColor = if (rightPanelTab == "Scan") PrimaryAccent else SecondaryAccent),
                        shape = RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 12.dp, bottomEnd = 12.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Text("Scan Student", color = if (rightPanelTab == "Scan") Color.White else TextPrimary)
                    }
                }
            }

            if (showScanOption && rightPanelTab == "Scan") {
                Text("Join this Quiz", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold), color = TextPrimary)
                Spacer(modifier = Modifier.height(16.dp))

                var expanded by remember { mutableStateOf(false) }
                Box {
                    Button(
                        onClick = { expanded = true },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent, contentColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (selectedStudentId == null) "Select Student for QR" else state.studentsList.find { it.id == selectedStudentId }?.let { "${it.firstName} ${it.lastName}" } ?: "Select Student", fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .background(Color(0xFF1E1E2E).copy(alpha = 0.95f), RoundedCornerShape(8.dp))
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
                                                    .background(SecondaryAccent.copy(alpha = 0.3f), RoundedCornerShape(12.dp)),
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
                        Card(shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(8.dp)) {
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
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(student.studentName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                                Text("${student.score} pts", color = PrimaryAccent, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }
        }
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
    var timeLimitSeconds by remember { mutableStateOf("15") }
    var editingQuestionId by remember { mutableStateOf<String?>(null) }
    
    val cardShape = RoundedCornerShape(8.dp)

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Setup left column
        Card(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            border = BorderStroke(1.dp, BorderColor),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = cardShape
        ) {
            Column(modifier = Modifier.padding(32.dp).fillMaxSize()) {
                Text("Quiz: ${quiz.title}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = PrimaryAccent)
                Spacer(modifier = Modifier.height(24.dp))

                Text(if (editingQuestionId != null) "Update Question" else "Add Question", style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = newQuestionText,
                    onValueChange = { newQuestionText = it },
                    label = { Text("Question Text") },
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = DarkTextFieldColors,
                    shape = RoundedCornerShape(8.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = optionA, onValueChange = { optionA = it }, label = { Text("Option A") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(8.dp))
                    OutlinedTextField(value = optionB, onValueChange = { optionB = it }, label = { Text("Option B") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(8.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = optionC, onValueChange = { optionC = it }, label = { Text("Option C") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(8.dp))
                    OutlinedTextField(value = optionD, onValueChange = { optionD = it }, label = { Text("Option D") }, modifier = Modifier.weight(1f), colors = DarkTextFieldColors, shape = RoundedCornerShape(8.dp))
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
                                    colors = ButtonDefaults.buttonColors(containerColor = if (correctOption == opt) PrimaryAccent else SecondaryAccent),
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = ButtonDefaults.buttonElevation(0.dp)
                                ) {
                                    Text(opt, color = if (correctOption == opt) Color.White else TextPrimary)
                                }
                            }
                        }
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = points,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) points = it },
                            label = { Text("Points") },
                            modifier = Modifier.width(100.dp),
                            colors = DarkTextFieldColors,
                            shape = RoundedCornerShape(8.dp)
                        )
                        OutlinedTextField(
                            value = timeLimitSeconds,
                            onValueChange = { if (it.isEmpty() || it.all { char -> char.isDigit() }) timeLimitSeconds = it },
                            label = { Text("Time (s)") },
                            modifier = Modifier.width(100.dp),
                            colors = DarkTextFieldColors,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    if (editingQuestionId != null) {
                        TextButton(onClick = {
                            editingQuestionId = null
                            newQuestionText = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correctOption = "A"; points = "10"; timeLimitSeconds = "15"
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
                            val timeLimitInt = timeLimitSeconds.toIntOrNull() ?: 15
                            if (editingQuestionId != null) {
                                val updatedQuestion = Question(
                                    id = editingQuestionId!!,
                                    quizId = quiz.id,
                                    text = newQuestionText,
                                    correctAnswer = answerText,
                                    options = listOf(optionA, optionB, optionC, optionD),
                                    points = pointsInt,
                                    timeLimitSeconds = timeLimitInt
                                )
                                viewModel.updateQuestionInQuiz(quiz.id, updatedQuestion)
                                editingQuestionId = null
                            } else {
                                viewModel.addQuestionToQuiz(quiz.id, newQuestionText, listOf(optionA, optionB, optionC, optionD), answerText, pointsInt, timeLimitInt)
                            }
                            newQuestionText = ""; optionA = ""; optionB = ""; optionC = ""; optionD = ""; correctOption = "A"; points = "10"; timeLimitSeconds = "15"
                        },
                        enabled = newQuestionText.isNotBlank() && optionA.isNotBlank() && optionB.isNotBlank() && optionC.isNotBlank() && optionD.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(if (editingQuestionId != null) "Update Question" else "Add Question", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Questions (${quiz.questions.size}):", fontWeight = FontWeight.Bold, color = TextPrimary)
                    Button(
                        onClick = { viewModel.startQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent)
                    ) {
                        Text("Start Quiz", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(quiz.questions) { q ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("- ${q.text}", color = TextPrimary, fontWeight = FontWeight.Bold)
                                    Text("Answer: ${q.correctAnswer} (${q.points} pts, ${q.timeLimitSeconds}s)", color = TextSecondary, fontSize = 12.sp)
                                }
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
                                        timeLimitSeconds = q.timeLimitSeconds.toString()
                                    }
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit Question", tint = PrimaryAccent)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Setup right column
        Box(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
            QuizRightPanel(viewModel, state, quiz)
        }
    }
}

@Composable
fun ActiveQuizSection(viewModel: AdminViewModel, state: AdminState) {
    val quiz = state.selectedQuizDetail ?: return
    val cardShape = RoundedCornerShape(8.dp)

    Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        // Left Column: Questions + Send Button
        Card(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = CardDefaults.cardColors(containerColor = SurfaceColor),
            border = BorderStroke(1.dp, BorderColor),
            elevation = CardDefaults.cardElevation(0.dp),
            shape = cardShape
        ) {
            Column(modifier = Modifier.padding(32.dp).fillMaxSize()) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Active Quiz: ${quiz.title}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold), color = PrimaryAccent)
                    Button(
                        onClick = { viewModel.stopQuiz() },
                        colors = ButtonDefaults.buttonColors(containerColor = WarningAccent)
                    ) {
                        Text("Stop Quiz", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                val allQuestionsSent = quiz.questions.isNotEmpty() && quiz.questions.all { state.sentQuestionIds.contains(it.id) }

                if (allQuestionsSent) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("All questions have been sent!", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { viewModel.endQuizAndPublishScores() },
                                colors = ButtonDefaults.buttonColors(containerColor = SecondaryAccent),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.height(56.dp).padding(horizontal = 32.dp)
                            ) {
                                Text("End Quiz & Publish Scores", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(quiz.questions) { q ->
                            Card(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("- ${q.text}", color = TextPrimary, fontWeight = FontWeight.Bold)
                                        Text("Answer: ${q.correctAnswer} (${q.points} pts, ${q.timeLimitSeconds}s)", color = TextSecondary, fontSize = 12.sp)
                                    }
                                    val isSent = state.sentQuestionIds.contains(q.id)
                                    Button(
                                        onClick = { viewModel.sendQuestion(q) },
                                        enabled = !isSent,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isSent) Color.Gray else PrimaryAccent,
                                            disabledContainerColor = Color.White.copy(alpha = 0.2f)
                                        ),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(if (isSent) "Sent" else "Send", fontWeight = FontWeight.Bold, color = if (isSent) Color.LightGray else Color.White)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Right Column: Leaderboard
        Box(modifier = Modifier.weight(0.6f).fillMaxHeight()) {
            QuizRightPanel(viewModel, state, quiz, showScanOption = false)
        }
    }
}

package com.muslima.yya.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack

// Girly, Cursive Typography to match the aesthetic
val GirlyTypography = Typography(
    headlineLarge = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 46.sp, letterSpacing = 1.sp),
    headlineMedium = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.SemiBold, fontSize = 36.sp, letterSpacing = 0.5.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.SemiBold, fontSize = 28.sp, letterSpacing = 0.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Bold, fontSize = 24.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.SemiBold, fontSize = 22.sp, letterSpacing = 0.15.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Normal, fontSize = 20.sp, letterSpacing = 0.5.sp, lineHeight = 28.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Normal, fontSize = 18.sp, letterSpacing = 0.25.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.Cursive, fontWeight = FontWeight.Medium, fontSize = 18.sp, letterSpacing = 0.1.sp)
)

@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val state by viewModel.state.collectAsState()

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground,
            surface = Color.Transparent,
            onSurface = TextPrimary,
            primary = VibrantPink,
            onPrimary = Color.White
        ),
        typography = GirlyTypography
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            // Floating question marks background
            Canvas(modifier = Modifier.fillMaxSize()) {
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Helper to draw a cute, stylized Question Mark
                fun drawQuestionMark(center: Offset, scale: Float, color: Color) {
                    val stroke = Stroke(width = 12 * scale, cap = StrokeCap.Round)

                    // The Hook of the ?
                    val hookPath = Path().apply {
                        moveTo(center.x, center.y - 40 * scale)
                        cubicTo(
                            center.x - 50 * scale, center.y - 100 * scale,
                            center.x + 50 * scale, center.y - 100 * scale,
                            center.x + 10 * scale, center.y - 10 * scale
                        )
                    }
                    drawPath(hookPath, color = color, style = stroke)

                    // The Dot
                    drawCircle(color = color, radius = 8 * scale, center = Offset(center.x, center.y + 30 * scale))
                }

                // Draw question marks floating around
                drawQuestionMark(Offset(canvasWidth * 0.2f, canvasHeight * 0.25f), 1.2f, DarkMatcha.copy(alpha = 0.2f))
                drawQuestionMark(Offset(canvasWidth * 0.85f, canvasHeight * 0.65f), 2.0f, VibrantPink.copy(alpha = 0.15f))
                drawQuestionMark(Offset(canvasWidth * 0.5f, canvasHeight * 0.45f), 0.8f, VibrantPurple.copy(alpha = 0.15f))
                drawQuestionMark(Offset(canvasWidth * 0.15f, canvasHeight * 0.85f), 1.0f, VibrantRed.copy(alpha = 0.1f))
            }

            Column(modifier = Modifier.fillMaxSize().systemBarsPadding()) {
                if (state.isConnected) {
                    // Glassy Header for connected state
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        colors = CardDefaults.cardColors(containerColor = GlassContainer),
                        border = BorderStroke(1.dp, GlassBorder),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(
                                    onClick = { viewModel.disconnect() },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(VibrantPink.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Leave Quiz",
                                        tint = VibrantPink,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(state.quizTitle, fontWeight = FontWeight.Bold, color = TextPrimary)
                            }
                            Text(state.studentName, color = TextSecondary)
                        }
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    if (!state.isConnected) {
                        JoinQuizSection(viewModel)
                    } else if (state.quizEnded) {
                        QuizEndedSection(state)
                    } else if (state.currentQuestion == null) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Waiting for the next question...", style = MaterialTheme.typography.titleLarge, color = TextSecondary)
                        }
                    } else {
                        QuestionSection(viewModel, state)
                    }
                }
            }
        }
    }
}

// A cute, bouncy Question Mark Icon
@Composable
fun QuestionMarkIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(120.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val scale = 1.5f
        val color = VibrantPink
        val stroke = Stroke(width = 12 * scale, cap = StrokeCap.Round)

        // The Hook of the ?
        val hookPath = Path().apply {
            moveTo(center.x, center.y - 20 * scale)
            cubicTo(
                center.x - 50 * scale, center.y - 80 * scale,
                center.x + 50 * scale, center.y - 80 * scale,
                center.x + 10 * scale, center.y + 10 * scale
            )
        }
        drawPath(hookPath, color = color, style = stroke)

        // The Dot
        drawCircle(color = color, radius = 8 * scale, center = Offset(center.x, center.y + 45 * scale))
    }
}

@Composable
fun JoinQuizSection(viewModel: StudentViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        // Custom Cute Question Mark Icon
        QuestionMarkIcon()
        Spacer(modifier = Modifier.height(16.dp))

        // New Competitive Title
        Text(
            "Enter the Challenge!",
            style = MaterialTheme.typography.headlineLarge,
            color = VibrantPink,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Descriptive text matching your exact screenshot
        Text(
            "Scan the QR code from the Admin and join the quiz!",
            style = MaterialTheme.typography.bodyLarge,
            color = MatchaGreen,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.weight(1f))

        QrScannerButton(modifier = Modifier.padding(bottom = 16.dp)) { scannedData ->
            viewModel.joinQuiz(scannedData)
        }
    }
}

@Composable
fun QuestionSection(viewModel: StudentViewModel, state: StudentState) {
    val question = state.currentQuestion ?: return
    var selectedOption by remember(question.id) { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Question:", style = MaterialTheme.typography.headlineSmall, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(question.text, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
        Spacer(modifier = Modifier.height(32.dp))

        if (state.answered) {
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                Text("Answer submitted. Waiting for the next question...", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
            }
        } else {
            question.options.forEach { option ->
                val isSelected = selectedOption == option
                Button(
                    onClick = { selectedOption = option },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = if (isSelected) null else BorderStroke(1.dp, GlassBorder),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) VibrantPink else GlassContainer,
                        contentColor = TextPrimary
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(option, fontSize = 20.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { selectedOption?.let { viewModel.submitAnswer(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkMatcha,
                    disabledContainerColor = DarkMatcha.copy(alpha = 0.3f)
                ),
                enabled = selectedOption != null
            ) {
                Text("Submit Answer", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun QuizEndedSection(state: StudentState) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Quiz Ended!", style = MaterialTheme.typography.headlineLarge, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(32.dp))
        Text("Leaderboard", style = MaterialTheme.typography.titleLarge, color = TextPrimary, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(state.leaderboard) { student ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = GlassContainer),
                    border = BorderStroke(1.dp, GlassBorder),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(student.studentName, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text("${student.score} pts", color = MatchaGreen, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                    }
                }
            }
        }
    }
}
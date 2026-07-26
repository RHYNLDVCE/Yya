package com.muslima.yya.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.hazeEffect
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun LoginSection(viewModel: AdminViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val passwordFocusRequester = remember { FocusRequester() }
    val cardShape = RoundedCornerShape(8.dp)

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = VibrantPink,
        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
        focusedLabelColor = VibrantPink,
        unfocusedLabelColor = TextSecondary,
        cursorColor = VibrantPink,
        unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
        focusedContainerColor = Color.White.copy(alpha = 0.1f),
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 64.dp), // Keeps it away from absolute edges on smaller monitors
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Side: Welcome Message (Centered in its half of the screen)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.widthIn(max = 480.dp) // Prevents text from stretching too wide
                ) {
                    Text(
                        "Manage the \nQuiz App",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontSize = 56.sp, // Made slightly larger to pop
                            lineHeight = 64.sp
                        ),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Login to access the dashboard, manage students, create quizzes, and monitor live leaderboards.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary
                    )
                }
            }

            // Right Side: Glassy Login Card (Centered in its half of the screen)
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .width(420.dp)
                        .hazeEffect(
                            state = LocalHazeState.current,
                            style = HeavyBlurStyle
                        ),
                    colors = CardDefaults.cardColors(containerColor = GlassContainer),
                    border = BorderStroke(1.dp, GlassBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    shape = cardShape
                ) {
                    Column(
                        modifier = Modifier.padding(40.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Yya Admin",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = VibrantPink
                        )
                        Text(
                            "Welcome back",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it },
                            label = { Text("Username") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().onKeyEvent {
                                if (it.key == Key.Enter) {
                                    if (it.type == KeyEventType.KeyDown) {
                                        passwordFocusRequester.requestFocus()
                                    }
                                    true
                                } else false
                            },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(image, "Toggle password visibility", tint = VibrantPink)
                                }
                            },
                            modifier = Modifier.fillMaxWidth().focusRequester(passwordFocusRequester).onKeyEvent {
                                if (it.key == Key.Enter) {
                                    if (it.type == KeyEventType.KeyDown) {
                                        if (username.isNotBlank() && password.isNotBlank()) {
                                            viewModel.login(username, password)
                                        }
                                    }
                                    true
                                } else false
                            },
                            colors = textFieldColors,
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        Button(
                            onClick = { viewModel.login(username, password) },
                            enabled = username.isNotBlank() && password.isNotBlank(),
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = VibrantPink,
                                contentColor = Color.White,
                                disabledContainerColor = VibrantPink.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Login", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DashboardSection(viewModel: AdminViewModel, state: AdminState) {
    // Load all leaderboards automatically
    LaunchedEffect(state.quizzesList) {
        viewModel.loadAllDashboardLeaderboards()
    }

    val cardShape = RoundedCornerShape(8.dp)
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
        androidx.compose.foundation.lazy.LazyColumn(modifier = Modifier.padding(40.dp).fillMaxSize()) {
            item {
                Text("Dashboard Overview", style = MaterialTheme.typography.headlineLarge, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(24.dp))

                // Server Status Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                ) {
                    Row(modifier = Modifier.padding(24.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Server Status", color = TextSecondary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (state.serverRunning) {
                                Text("Running", color = DarkMatcha, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Address: ws://${state.serverIp}:8080/quiz", color = TextPrimary, style = MaterialTheme.typography.bodyLarge)
                            } else {
                                Text("Stopped", color = SoftCoral, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Total Quizzes", color = TextSecondary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${state.quizzesList.size}", color = VibrantPink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Total Students", color = TextSecondary, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${state.studentsList.size}", color = VibrantPink, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
                Text("Quizzes Leaderboards", style = MaterialTheme.typography.headlineMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
            }

            state.quizzesList.forEach { quiz ->
                item {
                    val leaderboard = state.dashboardLeaderboards[quiz.id] ?: emptyList()
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.02f)),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
                            Text(quiz.title, style = MaterialTheme.typography.titleMedium, color = DarkMatcha, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(16.dp))

                            if (leaderboard.isEmpty()) {
                                Box(modifier = Modifier.fillMaxWidth().height(60.dp), contentAlignment = Alignment.Center) {
                                    Text("No data for this quiz.", color = TextSecondary)
                                }
                            } else {
                                val maxScore = leaderboard.maxOfOrNull { it.score }?.coerceAtLeast(10L) ?: 100L
                                leaderboard.forEach { entry ->
                                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text(entry.studentName, modifier = Modifier.width(120.dp), color = TextPrimary, fontWeight = FontWeight.Bold, maxLines = 1)
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Box(modifier = Modifier.weight(1f).height(24.dp).background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))) {
                                            val fraction = (entry.score.toFloat() / maxScore.toFloat()).coerceIn(0f, 1f)
                                            if (fraction > 0f) {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxHeight()
                                                        .fillMaxWidth(fraction)
                                                        .background(VibrantPink, RoundedCornerShape(12.dp))
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text("${entry.score} pts", color = DarkMatcha, fontWeight = FontWeight.Bold, modifier = Modifier.width(60.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
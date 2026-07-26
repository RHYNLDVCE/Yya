package com.muslima.yya.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Modern Admin Light Theme (Tailwind inspired)
val DarkBackground = Color(0xFFF9FAFB) // App background (light grey)
val PrimaryAccent = Color(0xFF111827) // Dark for primary actions
val SecondaryAccent = Color(0xFFF3F4F6) // Active menu background
val ErrorAccent = Color(0xFFEF4444) // Red for logout/errors
val WarningAccent = Color(0xFFF59E0B) // Amber for stars/time
val SuccessAccent = Color(0xFF10B981) // Emerald for connected status
val SurfaceColor = Color(0xFFFFFFFF) // White for sidebar and cards
val BorderColor = Color(0xFFE5E7EB) // Subtle borders
val TextPrimary = Color(0xFF111827) // Dark text
val TextSecondary = Color(0xFF6B7280) // Gray text
val SurfaceTint = Color(0xFFF3F4F6)

// Define a professional typography configuration.
val ProfessionalTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        letterSpacing = (-0.5).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        letterSpacing = 0.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        letterSpacing = 0.15.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp
    )
)

@Composable
fun AdminScreen(viewModel: AdminViewModel) {
    val state by viewModel.state.collectAsState()
    var selectedMenu by remember { mutableStateOf("Dashboard") }
    var selectedSubMenu by remember { mutableStateOf<String?>(null) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            background = DarkBackground,
            surface = SurfaceColor,
            onSurface = TextPrimary,
            primary = PrimaryAccent,
            onPrimary = Color.White
        ),
        typography = ProfessionalTypography
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkBackground)
        ) {
            if (!state.isLoggedIn) {
                LoginSection(viewModel, state)
            } else {
                Row(modifier = Modifier.fillMaxSize()) {
                    LeftPanel(
                        selectedMenu = selectedMenu,
                        selectedSubMenu = selectedSubMenu,
                        onMenuSelected = { menu, subMenu ->
                            selectedMenu = menu
                            selectedSubMenu = subMenu
                            viewModel.selectQuizForDetail(null)
                        }
                    )

                    Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)) {
                        when (selectedMenu) {
                            "Dashboard" -> DashboardSection(viewModel, state)
                            "Students" -> StudentsSection(viewModel, state, selectedSubMenu)
                            "Quiz" -> {
                                if (state.selectedQuizDetail != null) {
                                    if (state.isQuizStarted) {
                                        ActiveQuizSection(viewModel, state)
                                    } else {
                                        QuizDetailSection(viewModel, state)
                                    }
                                } else {
                                    when (selectedSubMenu) {
                                        "Add Quiz" -> AddQuizSection(viewModel)
                                        "List Quiz" -> QuizListSection(viewModel, state)
                                        "Update Quiz" -> QuizUpdateSection(viewModel, state)
                                        "Delete Quiz" -> QuizDeleteSection(viewModel, state)
                                        else -> QuizListSection(viewModel, state)
                                    }
                                }
                            }
                            "Settings" -> SettingsSection(viewModel, state)
                            else -> DashboardSection(viewModel, state)
                        }
                    }
                }
            }
        }
    }
}
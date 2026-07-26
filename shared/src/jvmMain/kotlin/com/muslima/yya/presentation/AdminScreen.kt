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
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.haze

// Dark Theme & Glass Colors
val DarkBackground = Color(0xFF0B0D17)
val MatchaGreen = Color(0xFFE9EDC9)
val DarkMatcha = Color(0xFFA3B18A)
val VibrantPink = Color(0xFFFF007F)
val VibrantRed = Color(0xFFFF4D4D)
val VibrantPurple = Color(0xFF9D00FF)
val GlassContainer = Color(0xFF1E1E2E).copy(alpha = 0.4f)
val GlassBorder = Color.White.copy(alpha = 0.2f)
val TextPrimary = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFFA0A0B0)
val SoftCoral = Color(0xFFFFA69E)

// Global state and style for heavy blur glassmorphism
val LocalHazeState = compositionLocalOf { HazeState() }
val HeavyBlurStyle = HazeStyle(
    blurRadius = 100.dp,
    tint = HazeTint(Color.Black.copy(alpha = 0.3f))
)

// Define a professional typography configuration.
// Replace FontFamily.SansSerif with a loaded custom font (e.g., Inter) if needed.
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

    val hazeState = remember { HazeState() }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = DarkBackground,
            surface = Color.Transparent,
            onSurface = TextPrimary,
            primary = VibrantPink,
            onPrimary = Color.White
        ),
        typography = ProfessionalTypography
    ) {
        CompositionLocalProvider(LocalHazeState provides hazeState) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(DarkBackground)
                    .haze(state = hazeState)
            ) {
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

                if (!state.isLoggedIn) {
                    LoginSection(viewModel)
                } else {
                    Row(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        LeftPanel(
                            selectedMenu = selectedMenu,
                            selectedSubMenu = selectedSubMenu,
                            onMenuSelected = { menu, subMenu ->
                                selectedMenu = menu
                                selectedSubMenu = subMenu
                                viewModel.selectQuizForDetail(null)
                            }
                        )

                        Spacer(modifier = Modifier.width(32.dp))

                        Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(vertical = 16.dp, horizontal = 24.dp)) {
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
                                else -> DashboardSection(viewModel, state)
                            }
                        }
                    }
                }
            }
        }
    }
}
package com.muslima.yya.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.chrisbanes.haze.hazeEffect

@Composable
fun LeftPanel(
    selectedMenu: String,
    selectedSubMenu: String?,
    onMenuSelected: (String, String?) -> Unit
) {
    var studentsExpanded by remember { mutableStateOf(false) }
    var quizExpanded by remember { mutableStateOf(false) }
    val cardShape = RoundedCornerShape(8.dp)

    Card(
        modifier = Modifier
            .width(260.dp)
            .fillMaxHeight()
            .hazeEffect(
                state = LocalHazeState.current,
                style = HeavyBlurStyle
            ),
        colors = CardDefaults.cardColors(containerColor = GlassContainer),
        border = BorderStroke(1.dp, GlassBorder),
        shape = cardShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(vertical = 32.dp, horizontal = 16.dp)) {
            Text(
                "Yya Admin",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.ExtraBold),
                color = VibrantPink,
                modifier = Modifier.padding(start = 8.dp, bottom = 48.dp)
            )

            MenuButton(
                title = "Dashboard",
                isSelected = selectedMenu == "Dashboard",
                isExpandable = false,
                isExpanded = false,
                onClick = { onMenuSelected("Dashboard", null) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            MenuButton(
                title = "Students",
                isSelected = selectedMenu == "Students",
                isExpandable = true,
                isExpanded = studentsExpanded,
                onClick = { studentsExpanded = !studentsExpanded }
            )
            if (studentsExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    val subOptions = listOf("Add Students", "List All Students", "Update Students", "Delete Students")
                    subOptions.forEach { sub ->
                        SubMenuButton(
                            title = sub,
                            isSelected = selectedMenu == "Students" && selectedSubMenu == sub,
                            onClick = { onMenuSelected("Students", sub) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            MenuButton(
                title = "Quiz",
                isSelected = selectedMenu == "Quiz",
                isExpandable = true,
                isExpanded = quizExpanded,
                onClick = { quizExpanded = !quizExpanded }
            )
            if (quizExpanded) {
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    val subOptions = listOf("Add Quiz", "List Quiz", "Update Quiz", "Delete Quiz")
                    subOptions.forEach { sub ->
                        SubMenuButton(
                            title = sub,
                            isSelected = selectedMenu == "Quiz" && selectedSubMenu == sub,
                            onClick = { onMenuSelected("Quiz", sub) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    isSelected: Boolean,
    isExpandable: Boolean = false,
    isExpanded: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color.White.copy(alpha = 0.15f) else Color.Transparent
    val contentColor = if (isSelected) VibrantPink else TextPrimary

    val rotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "DropdownIconRotation"
    )

    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)

            if (isExpandable) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "Collapse menu" else "Expand menu",
                    modifier = Modifier.rotate(rotation),
                    tint = contentColor
                )
            }
        }
    }
}

@Composable
fun SubMenuButton(title: String, isSelected: Boolean, onClick: () -> Unit) {
    val contentColor = if (isSelected) DarkMatcha else TextSecondary
    val backgroundColor = if (isSelected) Color.White.copy(alpha = 0.08f) else Color.Transparent

    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.Start) {
            Text(title, fontSize = 14.sp, fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal)
        }
    }
}
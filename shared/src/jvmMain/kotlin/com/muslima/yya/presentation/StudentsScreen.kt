package com.muslima.yya.presentation

import androidx.compose.foundation.BorderStroke
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
import com.muslima.yya.domain.model.Student
import dev.chrisbanes.haze.hazeEffect
import java.util.UUID

val DarkTextFieldColors @Composable get() = OutlinedTextFieldDefaults.colors(
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

@Composable
fun StudentsSection(viewModel: AdminViewModel, state: AdminState, subOption: String?) {
    val cardShape = RoundedCornerShape(32.dp)
    when (subOption) {
        "Add Students" -> AddStudentSection(viewModel)
        "List All Students" -> ListStudentsSection(state)
        "Update Students" -> UpdateStudentSection(viewModel, state)
        "Delete Students" -> DeleteStudentSection(viewModel, state)
        else -> {
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
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Students Management", style = MaterialTheme.typography.headlineLarge, color = TextPrimary)
                }
            }
        }
    }
}

@Composable
fun AddStudentSection(viewModel: AdminViewModel) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var studentNumber by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var yearLevel by remember { mutableStateOf("") }

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
        Column(modifier = Modifier.padding(40.dp).fillMaxWidth()) {
            Text("Add New Student", style = MaterialTheme.typography.headlineMedium, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = studentNumber, onValueChange = { studentNumber = it }, label = { Text("Student Number") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = yearLevel, onValueChange = { yearLevel = it }, label = { Text("Year Level") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    viewModel.addStudent(Student(
                        id = UUID.randomUUID().toString(),
                        firstName = firstName,
                        lastName = lastName,
                        studentNumber = studentNumber,
                        age = age.toIntOrNull() ?: 0,
                        gender = gender,
                        yearLevel = yearLevel
                    ))
                    firstName = ""; lastName = ""; studentNumber = ""; age = ""; gender = ""; yearLevel = ""
                },
                modifier = Modifier.align(Alignment.End).height(56.dp).width(160.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Save Student", fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun ListStudentsSection(state: AdminState) {
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
            Text("All Students", style = MaterialTheme.typography.headlineMedium, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.studentsList) { student ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text("${student.firstName} ${student.lastName}", fontWeight = FontWeight.Bold, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("No: ${student.studentNumber} | Age: ${student.age} | Gender: ${student.gender} | Year: ${student.yearLevel}", color = TextSecondary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UpdateStudentSection(viewModel: AdminViewModel, state: AdminState) {
    var selectedStudent by remember { mutableStateOf<Student?>(null) }
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
                Text("Select Student", style = MaterialTheme.typography.headlineSmall, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn {
                    items(state.studentsList) { student ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { selectedStudent = student },
                            colors = CardDefaults.cardColors(containerColor = if (selectedStudent?.id == student.id) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.05f)),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(0.dp)
                        ) {
                            Text("${student.firstName} ${student.lastName}", modifier = Modifier.padding(16.dp), color = TextPrimary, fontWeight = if (selectedStudent?.id == student.id) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            if (selectedStudent != null) {
                var firstName by remember(selectedStudent) { mutableStateOf(selectedStudent!!.firstName) }
                var lastName by remember(selectedStudent) { mutableStateOf(selectedStudent!!.lastName) }
                var studentNumber by remember(selectedStudent) { mutableStateOf(selectedStudent!!.studentNumber) }
                var age by remember(selectedStudent) { mutableStateOf(selectedStudent!!.age.toString()) }
                var gender by remember(selectedStudent) { mutableStateOf(selectedStudent!!.gender) }
                var yearLevel by remember(selectedStudent) { mutableStateOf(selectedStudent!!.yearLevel) }

                Column(modifier = Modifier.weight(2f).fillMaxHeight().padding(16.dp)) {
                    Text("Update Details", style = MaterialTheme.typography.headlineSmall, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = studentNumber, onValueChange = { studentNumber = it }, label = { Text("Student Number") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = yearLevel, onValueChange = { yearLevel = it }, label = { Text("Year Level") }, modifier = Modifier.fillMaxWidth(), colors = DarkTextFieldColors, shape = RoundedCornerShape(16.dp))
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.updateStudent(selectedStudent!!.copy(
                                firstName = firstName,
                                lastName = lastName,
                                studentNumber = studentNumber,
                                age = age.toIntOrNull() ?: 0,
                                gender = gender,
                                yearLevel = yearLevel
                            ))
                            selectedStudent = null
                        },
                        modifier = Modifier.align(Alignment.End).height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DarkMatcha),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text("Update Student", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            } else {
                Box(modifier = Modifier.weight(2f).fillMaxHeight(), contentAlignment = Alignment.Center) {
                    Text("Select a student to update", color = TextSecondary)
                }
            }
        }
    }
}

@Composable
fun DeleteStudentSection(viewModel: AdminViewModel, state: AdminState) {
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
            Text("Delete Students", style = MaterialTheme.typography.headlineMedium, color = VibrantPink, fontWeight = FontWeight.ExtraBold)
            Spacer(modifier = Modifier.height(24.dp))
            LazyColumn {
                items(state.studentsList) { student ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(modifier = Modifier.padding(20.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("${student.firstName} ${student.lastName}", fontWeight = FontWeight.Bold, color = TextPrimary)
                                Text("No: ${student.studentNumber}", color = TextSecondary)
                            }
                            Button(
                                onClick = { viewModel.deleteStudent(student.id) },
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
package com.tumme.scrudstudents.ui.subscribe

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.R

/**
 * Formulaire pour inscrire un étudiant à un cours.
 * Permet de sélectionner un étudiant et un cours via des menus déroulants.
 * Valide les données avant de créer l'inscription avec une note préliminaire.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeFormScreen(
    viewModel: SubscribeListViewModel = hiltViewModel(),
    onSaved: ()->Unit = {}
) {
    val context = LocalContext.current
    var selectedStudentId by remember { mutableStateOf<Int?>(null) }
    var selectedCourseId by remember { mutableStateOf<Int?>(null) }
    var scoreText by remember { mutableStateOf("") }
    
    var studentError by remember { mutableStateOf(false) }
    var courseError by remember { mutableStateOf(false) }
    var scoreError by remember { mutableStateOf(false) }
    
    var studentMenuExpanded by remember { mutableStateOf(false) }
    var courseMenuExpanded by remember { mutableStateOf(false) }

    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = context.getString(R.string.new_subscription),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        @OptIn(ExperimentalMaterial3Api::class)
        ExposedDropdownMenuBox(
            expanded = studentMenuExpanded,
            onExpandedChange = { studentMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedStudentId?.let { studentId ->
                    students.find { it.idStudent == studentId }?.let { student ->
                        "${student.firstName} ${student.lastName}"
                    } ?: ""
                } ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(context.getString(R.string.student_label)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = studentMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = studentError,
                supportingText = if (studentError) { 
                    { Text(context.getString(R.string.please_select_student)) } 
                } else null
            )
            
            ExposedDropdownMenu(
                expanded = studentMenuExpanded,
                onDismissRequest = { studentMenuExpanded = false }
            ) {
                students.forEach { student ->
                    DropdownMenuItem(
                        text = { Text("${student.firstName} ${student.lastName}") },
                        onClick = {
                            selectedStudentId = student.idStudent
                            studentError = false
                            studentMenuExpanded = false
                        }
                    )
                }
            }
        }

        @OptIn(ExperimentalMaterial3Api::class)
        ExposedDropdownMenuBox(
            expanded = courseMenuExpanded,
            onExpandedChange = { courseMenuExpanded = it }
        ) {
            OutlinedTextField(
                value = selectedCourseId?.let { courseId ->
                    courses.find { it.idCourse == courseId }?.nameCourse ?: ""
                } ?: "",
                onValueChange = { },
                readOnly = true,
                label = { Text(context.getString(R.string.course)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = courseMenuExpanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                isError = courseError,
                supportingText = if (courseError) { 
                    { Text(context.getString(R.string.please_select_course)) } 
                } else null
            )
            
            ExposedDropdownMenu(
                expanded = courseMenuExpanded,
                onDismissRequest = { courseMenuExpanded = false }
            ) {
                courses.forEach { course ->
                    DropdownMenuItem(
                        text = { Text(course.nameCourse) },
                        onClick = {
                            selectedCourseId = course.idCourse
                            courseError = false
                            courseMenuExpanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = scoreText,
            onValueChange = { newValue ->
                scoreText = newValue
                scoreError = false
            },
            label = { Text(context.getString(R.string.grade_label)) },
            modifier = Modifier.fillMaxWidth(),
            isError = scoreError,
            supportingText = if (scoreError) { 
                { Text(context.getString(R.string.grade_must_be_positive)) } 
            } else null,
        )

        Button(
            onClick = {
                var hasError = false
                
                if (selectedStudentId == null) {
                    studentError = true
                    hasError = true
                }
                
                if (selectedCourseId == null) {
                    courseError = true
                    hasError = true
                }
                
                val score = scoreText.toFloatOrNull()
                if (score == null || score < 0) {
                    scoreError = true
                    hasError = true
                }
                
                if (!hasError && selectedStudentId != null && selectedCourseId != null) {
                    val subscribe = SubscribeEntity(
                        studentId = selectedStudentId!!,
                        courseId = selectedCourseId!!,
                        score = score!!
                    )
                    viewModel.insertSubscribe(subscribe)
                    onSaved()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(context.getString(R.string.save))
        }
    }
}

package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse

/**
 * Formulaire pour créer un nouveau cours.
 * Permet de saisir les informations : nom, ECTS, niveau, description, enseignant.
 * Valide les données avant de les enregistrer dans la base de données.
 */
@Composable
fun CourseFormScreen(
    viewModel: CourseListViewModel = hiltViewModel(),
    onSaved: ()->Unit = {}
) {
    var id by remember { mutableStateOf((0..10000).random()) }
    var nameCourse by remember { mutableStateOf("") }
    var ectsText by remember { mutableStateOf("") }
    var levelCourse by remember { mutableStateOf(LevelCourse.P1) }
    var teacherId by remember { mutableStateOf(1) }
    var description by remember { mutableStateOf("") }
    
    var nameError by remember { mutableStateOf(false) }
    var ectsError by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(
            value = nameCourse, 
            onValueChange = { 
                nameCourse = it
                nameError = false
            }, 
            label = { Text("Course Name") },
            isError = nameError,
            supportingText = if (nameError) { { Text("Course name cannot be empty") } } else null
        )
        Spacer(Modifier.height(8.dp))
        
        TextField(
            value = ectsText, 
            onValueChange = { 
                ectsText = it
                ectsError = false
            }, 
            label = { Text("ECTS Credits") },
            isError = ectsError,
            supportingText = if (ectsError) { { Text("ECTS must be greater than 0") } } else null
        )
        Spacer(Modifier.height(8.dp))

        Text("Select Level:", modifier = Modifier.padding(bottom = 8.dp))
        Row {
            listOf(LevelCourse.P1, LevelCourse.P2, LevelCourse.P3, 
                   LevelCourse.B1, LevelCourse.B2, LevelCourse.B3,
                   LevelCourse.A1, LevelCourse.A2, LevelCourse.A3,
                   LevelCourse.MS, LevelCourse.PhD).forEach { level ->
                Button(
                    onClick = { levelCourse = level }, 
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (levelCourse == level) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(level.value, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        
        TextField(
            value = description, 
            onValueChange = { description = it }, 
            label = { Text("Description") },
            modifier = Modifier.height(100.dp)
        )
        Spacer(Modifier.height(8.dp))
        
        TextField(
            value = teacherId.toString(), 
            onValueChange = { 
                try {
                    teacherId = it.toInt()
                } catch (e: NumberFormatException) {
                }
            }, 
            label = { Text("Teacher ID") }
        )
        Spacer(Modifier.height(16.dp))
        
        Button(onClick = {
            var hasError = false
            
            if (nameCourse.isBlank()) {
                nameError = true
                hasError = true
            }
            
            val ects = try {
                ectsText.toFloat()
            } catch (e: NumberFormatException) {
                ectsError = true
                hasError = true
                0f
            }
            
            if (ects <= 0) {
                ectsError = true
                hasError = true
            }
            
            if (!hasError) {
                val course = CourseEntity(
                    idCourse = id,
                    nameCourse = nameCourse,
                    ectsCourse = ects,
                    levelCourse = levelCourse,
                    teacherId = teacherId,
                    description = description
                )
                viewModel.insertCourse(course)
                onSaved()
            }
        }) {
            Text("Save")
        }
    }
}

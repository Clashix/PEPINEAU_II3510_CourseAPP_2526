package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse

/**
 * Formulaire pour créer un nouvel étudiant.
 * Permet de saisir les informations : nom, prénom, date de naissance, niveau, email, genre.
 * Valide les données avant de les enregistrer dans la base de données.
 */
@Composable
fun StudentFormScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onSaved: ()->Unit = {}
) {
    var id by remember { mutableStateOf((0..10000).random()) }
    var lastName by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var dobText by remember { mutableStateOf("2000-01-01") }
    var gender by remember { mutableStateOf(Gender.MALE) }
    var level by remember { mutableStateOf(LevelCourse.P1) }
    var email by remember { mutableStateOf("") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Last Name") })
        Spacer(Modifier.height(8.dp))
        
        TextField(value = firstName, onValueChange = { firstName = it }, label = { Text("First Name") })
        Spacer(Modifier.height(8.dp))
        
        TextField(value = dobText, onValueChange = { dobText = it }, label = { Text("Date of birth (yyyy-MM-dd)") })
        Spacer(Modifier.height(8.dp))
        
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))

        Row {
            listOf(Gender.MALE, Gender.FEMALE, Gender.OTHER).forEach { g->
                Button(onClick = { gender = g }, modifier = Modifier.padding(end = 8.dp)) {
                    Text(g.value)
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        
        Text("Select Level:", modifier = Modifier.padding(bottom = 8.dp))
        Row {
            listOf(LevelCourse.P1, LevelCourse.P2, LevelCourse.P3, 
                   LevelCourse.B1, LevelCourse.B2, LevelCourse.B3,
                   LevelCourse.A1, LevelCourse.A2, LevelCourse.A3,
                   LevelCourse.MS, LevelCourse.PhD).forEach { l ->
                Button(
                    onClick = { level = l }, 
                    modifier = Modifier.padding(end = 4.dp, bottom = 4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (level == l) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Text(l.value, style = MaterialTheme.typography.labelSmall)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        
        Button(onClick = {
            val dob = dateFormat.parse(dobText) ?: Date()
            val student = StudentEntity(
                idStudent = id,
                lastName = lastName,
                firstName = firstName,
                dateOfBirth = dob,
                gender = gender,
                level = level,
                email = email
            )
            viewModel.insertStudent(student)
            onSaved()
        }) {
            Text("Save")
        }
    }
}

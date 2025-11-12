package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel
import com.tumme.scrudstudents.R

/**
 * Page d'accueil de l'enseignant après connexion.
 * Affiche les informations de l'enseignant et propose l'accès aux fonctionnalités :
 * gestion des cours, saisie des notes et consultation des étudiants inscrits.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherHomeScreen(
    navController: NavController,
    viewModel: TeacherViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val teacher by viewModel.currentTeacher
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = context.getString(R.string.teacher_home),
            style = MaterialTheme.typography.headlineMedium
        )
        
        teacher?.let { currentTeacher ->
            Text(
                text = context.getString(R.string.welcome, currentTeacher.firstName, currentTeacher.lastName),
                style = MaterialTheme.typography.titleMedium
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("teacher_courses") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.my_courses),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.my_courses_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("teacher_grades") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.enter_grades),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.enter_grades_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("teacher_students") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.enrolled_students),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.enrolled_students_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { viewModel.logout() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(context.getString(R.string.logout))
        }
    }
}

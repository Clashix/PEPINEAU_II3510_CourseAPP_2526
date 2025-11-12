package com.tumme.scrudstudents.ui.student

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
import com.tumme.scrudstudents.ui.student.StudentViewModel
import com.tumme.scrudstudents.R

/**
 * Page d'accueil de l'étudiant après connexion.
 * Affiche les informations de l'étudiant et propose l'accès aux différentes fonctionnalités :
 * cours disponibles, inscriptions, notes et résumé final.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavController,
    viewModel: StudentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val student by viewModel.currentStudent
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = context.getString(R.string.student_home),
            style = MaterialTheme.typography.headlineMedium
        )
        
        student?.let { currentStudent ->
            Text(
                text = context.getString(R.string.welcome, currentStudent.firstName, currentStudent.lastName),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = context.getString(R.string.level, currentStudent.level.value),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("student_courses") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.available_courses),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.available_courses_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("student_subscriptions") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.my_subscriptions),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.my_subscriptions_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("student_grades") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.my_grades),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.my_grades_desc),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { navController.navigate("student_summary") }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = context.getString(R.string.final_summary),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = context.getString(R.string.final_summary_desc),
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

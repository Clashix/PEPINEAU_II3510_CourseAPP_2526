package com.tumme.scrudstudents.ui.teacher

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.ui.teacher.TeacherViewModel

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
    val teacher by viewModel.currentTeacher
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Accueil Enseignant",
            style = MaterialTheme.typography.headlineMedium
        )
        
        teacher?.let { currentTeacher ->
            Text(
                text = "Bienvenue, ${currentTeacher.firstName} ${currentTeacher.lastName}",
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
                            text = "Mes Cours",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Déclarer et gérer vos cours",
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
                            text = "Saisir des Notes",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Entrer les notes des étudiants",
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
                            text = "Étudiants Inscrits",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Voir les étudiants de vos cours",
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
            Text("Se déconnecter")
        }
    }
}

package com.tumme.scrudstudents.ui.student

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
import com.tumme.scrudstudents.ui.student.StudentViewModel

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
    val student by viewModel.currentStudent
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Accueil Étudiant",
            style = MaterialTheme.typography.headlineMedium
        )
        
        student?.let { currentStudent ->
            Text(
                text = "Bienvenue, ${currentStudent.firstName} ${currentStudent.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Niveau : ${currentStudent.level.value}",
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
                            text = "Cours Disponibles",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Voir et s'inscrire aux cours de votre niveau",
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
                            text = "Mes Inscriptions",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Voir vos cours inscrits",
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
                            text = "Mes Notes",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Consulter vos notes par cours",
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
                            text = "Résumé Final",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Voir votre moyenne pondérée",
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

package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import com.tumme.scrudstudents.data.local.model.LevelCourse
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Écran affichant la moyenne générale pondérée par ECTS de l'étudiant.
 * Calcule la moyenne pour le niveau de l'étudiant en utilisant les crédits ECTS.
 * Utilise StudentSummaryViewModel pour effectuer le calcul.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentSummaryScreen(
    navController: NavController,
    viewModel: StudentViewModel = hiltViewModel(),
    summaryViewModel: StudentSummaryViewModel = hiltViewModel()
) {
    val student by viewModel.currentStudent
    val average by summaryViewModel.average.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    
    LaunchedEffect(student) {
        if (student != null) {
            summaryViewModel.calculateAverage(student!!.idStudent, student!!.level)
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Résumé Final") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            student?.let { currentStudent ->
                Text(
                    text = "Résumé pour ${currentStudent.firstName} ${currentStudent.lastName}",
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Niveau: ${currentStudent.level.value}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(32.dp))
                
                if (isLoading) {
                    CircularProgressIndicator()
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Moyenne Pondérée",
                                style = MaterialTheme.typography.titleLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = String.format("%.2f", average),
                                style = MaterialTheme.typography.displayMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "sur 20",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    
                    if (average == 0.0f) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aucune note disponible pour ce niveau",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * ViewModel gérant le calcul de la moyenne pondérée de l'étudiant.
 * Calcule la moyenne en tenant compte des crédits ECTS de chaque cours.
 * Filtre les notes selon le niveau de l'étudiant.
 */
@HiltViewModel
class StudentSummaryViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {
    
    private val _average = MutableStateFlow(0.0f)
    val average: StateFlow<Float> = _average
    
    // Calcule la moyenne pondérée par ECTS pour un étudiant et un niveau
    fun calculateAverage(studentId: Int, level: LevelCourse) {
        viewModelScope.launch {
            val avg = repository.calculateStudentAverage(studentId, level)
            _average.value = avg
        }
    }
}


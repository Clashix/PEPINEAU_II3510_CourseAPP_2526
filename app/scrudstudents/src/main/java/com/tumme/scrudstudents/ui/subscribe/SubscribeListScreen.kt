package com.tumme.scrudstudents.ui.subscribe

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Écran affichant la liste de toutes les inscriptions (étudiant + cours).
 * Affiche les noms des étudiants et des cours au lieu des IDs.
 * Permet d'ajouter et supprimer des inscriptions.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscribeListScreen(
    viewModel: SubscribeListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToStudents: () -> Unit = {},
    onNavigateToCourses: () -> Unit = {}
) {
    val subscribes by viewModel.subscribes.collectAsState()
    val students by viewModel.students.collectAsState()
    val courses by viewModel.courses.collectAsState()
    
    val events by viewModel.events.collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    
    LaunchedEffect(events) {
        events?.let { message ->
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Inscriptions") },
                actions = {
                    TextButton(onClick = onNavigateToStudents) {
                        Text("Étudiants")
                    }
                    TextButton(onClick = onNavigateToCourses) {
                        Text("Cours")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToForm,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Ajouter une inscription"
                )
            }
        }
    ) { paddingValues ->
        if (subscribes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "Aucune inscription trouvée",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    TableHeader(
                        cells = listOf("Étudiant", "Cours", "Note", "Actions"),
                        weights = listOf(0.3f, 0.3f, 0.2f, 0.2f)
                    )
                }
                
                items(subscribes) { subscribe ->
                    SubscribeRow(
                        subscribe = subscribe,
                        studentName = viewModel.getStudentName(subscribe.studentId),
                        courseName = viewModel.getCourseName(subscribe.courseId),
                        onDelete = { viewModel.deleteSubscribe(subscribe) }
                    )
                }
            }
        }
    }
}

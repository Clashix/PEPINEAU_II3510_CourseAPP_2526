package com.tumme.scrudstudents.ui.course

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/**
 * Écran affichant la liste de tous les cours avec opérations CRUD.
 * Permet d'ajouter, supprimer et consulter les détails des cours.
 * Utilise CourseListViewModel pour gérer les données.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseListScreen(
    viewModel: CourseListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToStudents: () -> Unit = {},
    onNavigateToSubscribes: () -> Unit = {}
) {
    val courses by viewModel.courses.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Courses") },
                actions = {
                    TextButton(onClick = onNavigateToStudents) {
                        Text("Étudiants")
                    }
                    TextButton(onClick = onNavigateToSubscribes) {
                        Text("Inscriptions")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToForm) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {
            Button(
                onClick = onNavigateToStudents,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Go to Students")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TableHeader(cells = listOf("Name", "ECTS", "Level", "Actions"),
                weights = listOf(0.4f, 0.2f, 0.2f, 0.2f))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(courses) { course ->
                    CourseRow(
                        course = course,
                        onEdit = { },
                        onDelete = { viewModel.deleteCourse(course) },
                        onView = { onNavigateToDetail(course.idCourse) },
                        onShare = { }
                    )
                }
            }
        }
    }
}

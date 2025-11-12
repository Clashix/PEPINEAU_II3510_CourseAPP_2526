package com.tumme.scrudstudents.ui.course

import com.tumme.scrudstudents.ui.components.TableHeader
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.R

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
    val context = LocalContext.current
    val courses by viewModel.courses.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.courses)) },
                actions = {
                    TextButton(onClick = onNavigateToStudents) {
                        Text(context.getString(R.string.students))
                    }
                    TextButton(onClick = onNavigateToSubscribes) {
                        Text(context.getString(R.string.subscriptions))
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
                Text(context.getString(R.string.go_to_students))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TableHeader(cells = listOf(
                context.getString(R.string.name),
                context.getString(R.string.ects),
                context.getString(R.string.level_label),
                context.getString(R.string.actions)
            ),
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

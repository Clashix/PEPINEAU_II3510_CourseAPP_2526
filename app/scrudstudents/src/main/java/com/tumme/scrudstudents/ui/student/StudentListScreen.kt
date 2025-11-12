package com.tumme.scrudstudents.ui.student

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
 * Écran affichant la liste de tous les étudiants avec opérations CRUD.
 * Permet d'ajouter, supprimer et consulter les détails des étudiants.
 * Utilise StudentListViewModel pour gérer les données.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListScreen(
    viewModel: StudentListViewModel = hiltViewModel(),
    onNavigateToForm: () -> Unit = {},
    onNavigateToDetail: (Int) -> Unit = {},
    onNavigateToCourses: () -> Unit = {},
    onNavigateToSubscribes: () -> Unit = {}
) {
    val context = LocalContext.current
    val students by viewModel.students.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.students)) },
                actions = {
                    TextButton(onClick = onNavigateToCourses) {
                        Text(context.getString(R.string.courses))
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
                onClick = onNavigateToCourses,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(context.getString(R.string.go_to_courses))
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            TableHeader(cells = listOf(
                context.getString(R.string.dob),
                context.getString(R.string.last),
                context.getString(R.string.first),
                context.getString(R.string.gender),
                context.getString(R.string.actions)
            ),
                weights = listOf(0.25f, 0.25f, 0.25f, 0.15f, 0.10f))

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(students) { student ->
                    StudentRow(
                        student = student,
                        onEdit = { },
                        onDelete = { viewModel.deleteStudent(student) },
                        onView = { onNavigateToDetail(student.idStudent) },
                        onShare = { }
                    )
                }
            }
        }
    }
}

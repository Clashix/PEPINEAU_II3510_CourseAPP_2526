package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.R

/**
 * Écran affichant les détails d'un cours spécifique.
 * Récupère les informations du cours par son ID et les affiche.
 * Permet de retourner à la liste via le bouton retour.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailScreen(
    courseId: Int,
    viewModel: CourseListViewModel = hiltViewModel(),
    onBack: ()->Unit = {}
) {
    val context = LocalContext.current
    var course by remember { mutableStateOf<CourseEntity?>(null) }

    LaunchedEffect(courseId) {
        course = viewModel.findCourse(courseId)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(context.getString(R.string.course_details)) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = context.getString(R.string.back)) }
        })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (course == null) {
                Text(context.getString(R.string.loading))
            } else {
                Text(context.getString(R.string.id_label, course!!.idCourse))
                Text(context.getString(R.string.name_label, course!!.nameCourse))
                Text(context.getString(R.string.ects_label, course!!.ectsCourse))
                Text(context.getString(R.string.level_display, course!!.levelCourse.value))
            }
        }
    }
}

package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tumme.scrudstudents.data.local.model.StudentEntity
import java.text.SimpleDateFormat
import java.util.Locale
import com.tumme.scrudstudents.R

/**
 * Écran affichant les détails d'un étudiant spécifique.
 * Récupère les informations de l'étudiant par son ID et les affiche.
 * Permet de retourner à la liste via le bouton retour.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(
    studentId: Int,
    viewModel: StudentListViewModel = hiltViewModel(),
    onBack: ()->Unit = {}
) {
    val context = LocalContext.current
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var student by remember { mutableStateOf<StudentEntity?>(null) }

    LaunchedEffect(studentId) {
        student = viewModel.findStudent(studentId)
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(context.getString(R.string.student_details)) }, navigationIcon = {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, contentDescription = context.getString(R.string.back)) }
        })
    }) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            if (student == null) {
                Text(context.getString(R.string.loading))
            } else {
                Text(context.getString(R.string.id_label, student!!.idStudent))
                Text(context.getString(R.string.name_label, "${student!!.firstName} ${student!!.lastName}"))
                Text(context.getString(R.string.dob_label, sdf.format(student!!.dateOfBirth)))
                Text(context.getString(R.string.gender_label, student!!.gender.value))
            }
        }
    }
}

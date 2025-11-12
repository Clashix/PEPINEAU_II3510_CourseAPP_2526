package com.tumme.scrudstudents.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tumme.scrudstudents.data.local.model.GradeEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import com.tumme.scrudstudents.data.repository.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import com.tumme.scrudstudents.R

/**
 * Écran affichant toutes les notes de l'étudiant connecté.
 * Récupère les notes depuis le repository et affiche le nom du cours avec chaque note.
 * Utilise StudentGradesViewModel pour charger les données et les noms de cours.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    navController: NavController,
    viewModel: StudentViewModel = hiltViewModel(),
    gradesViewModel: StudentGradesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val student by viewModel.currentStudent
    val grades by gradesViewModel.grades.collectAsState()
    var isLoading by remember { mutableStateOf(true) }
    var courseNames by remember { mutableStateOf<Map<Int, String>>(emptyMap()) }
    
    LaunchedEffect(student) {
        if (student != null) {
            isLoading = false
            // Récupérer les noms des cours
            gradesViewModel.loadCourseNames()
        }
    }
    
    val courseNamesState by gradesViewModel.courseNames.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.my_grades)) },
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
                .padding(16.dp)
        ) {
            student?.let { currentStudent ->
                Text(
                    text = context.getString(R.string.grades_of, currentStudent.firstName, currentStudent.lastName),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.height(16.dp))
                
                if (isLoading) {
                    CircularProgressIndicator()
                } else if (grades.isEmpty()) {
                    Text(
                        text = context.getString(R.string.no_grades),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(grades) { grade ->
                            Card(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {
                                    Text(
                                        text = courseNamesState[grade.courseId] ?: context.getString(R.string.course_id, grade.courseId),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = context.getString(R.string.grade, grade.grade),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = context.getString(R.string.date, SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(java.util.Date(grade.dateGiven))),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * ViewModel gérant les notes de l'étudiant connecté.
 * Charge les notes depuis le repository et les noms des cours pour l'affichage.
 * Observe les changements dans les notes pour mettre à jour l'UI automatiquement.
 */
@HiltViewModel
class StudentGradesViewModel @Inject constructor(
    private val repository: SCRUDRepository,
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
    val grades: StateFlow<List<GradeEntity>> = _grades
    
    private val _courseNames = MutableStateFlow<Map<Int, String>>(emptyMap())
    val courseNames: StateFlow<Map<Int, String>> = _courseNames
    
    init {
        loadGrades()
    }
    
    // Charge les notes de l'étudiant connecté
    private fun loadGrades() {
        viewModelScope.launch {
            val student = authRepository.getCurrentStudent()
            student?.let {
                repository.getGradesByStudent(it.idStudent).collect { gradesList ->
                    _grades.value = gradesList
                }
            }
        }
    }
    
    // Charge les noms des cours pour les afficher avec les notes
    fun loadCourseNames() {
        viewModelScope.launch {
            val courses = repository.getAllCourses().first()
            val namesMap = courses.associate { it.idCourse to it.nameCourse }
            _courseNames.value = namesMap
        }
    }
}


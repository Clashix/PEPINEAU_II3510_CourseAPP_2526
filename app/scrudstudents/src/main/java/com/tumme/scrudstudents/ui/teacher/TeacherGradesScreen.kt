package com.tumme.scrudstudents.ui.teacher

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
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.GradeEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import com.tumme.scrudstudents.R

/**
 * Écran permettant à l'enseignant de saisir et modifier les notes des étudiants.
 * Affiche les cours de l'enseignant et les étudiants inscrits à chaque cours.
 * Permet d'entrer ou modifier les notes pour chaque étudiant.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherGradesScreen(
    navController: NavController,
    viewModel: TeacherViewModel = hiltViewModel(),
    gradesViewModel: TeacherGradesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val teacher by viewModel.currentTeacher
    val courses by gradesViewModel.teacherCourses.collectAsState()
    var selectedCourse by remember { mutableStateOf<CourseEntity?>(null) }
    val students by gradesViewModel.students.collectAsState()
    val grades by gradesViewModel.grades.collectAsState()
    
    LaunchedEffect(teacher) {
        if (teacher != null) {
            gradesViewModel.loadTeacherCourses(teacher!!.idTeacher)
        }
    }
    
    LaunchedEffect(selectedCourse) {
        selectedCourse?.let { course ->
            gradesViewModel.loadStudentsForCourse(course.idCourse)
            gradesViewModel.loadGradesForCourse(course.idCourse)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.enter_grades)) },
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
            Text(
                text = context.getString(R.string.select_course),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Liste des cours de l'enseignant
            LazyColumn(
                modifier = Modifier.height(150.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(courses) { course ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { selectedCourse = course },
                        colors = CardDefaults.cardColors(
                            containerColor = if (selectedCourse?.idCourse == course.idCourse) 
                                MaterialTheme.colorScheme.primaryContainer 
                            else 
                                MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = course.nameCourse,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Liste des étudiants inscrits au cours sélectionné
            selectedCourse?.let { course ->
                Text(
                    text = context.getString(R.string.students_enrolled_in, course.nameCourse),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                if (students.isEmpty()) {
                    Text(context.getString(R.string.no_students_in_course))
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(students) { student ->
                            val existingGrade = grades.find { 
                                it.studentId == student.idStudent && it.courseId == course.idCourse 
                            }
                            
                            GradeEntryCard(
                                student = student,
                                course = course,
                                teacherId = teacher?.idTeacher ?: 0,
                                existingGrade = existingGrade,
                                onGradeSave = { grade ->
                                    gradesViewModel.saveGrade(grade)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradeEntryCard(
    student: StudentEntity,
    course: CourseEntity,
    teacherId: Int,
    existingGrade: GradeEntity?,
    onGradeSave: (GradeEntity) -> Unit
) {
    val context = LocalContext.current
    var gradeText by remember { mutableStateOf(existingGrade?.grade?.toString() ?: "") }
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "${student.firstName} ${student.lastName}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            TextField(
                value = gradeText,
                onValueChange = { gradeText = it },
                label = { Text(context.getString(R.string.grade_out_of_20)) },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    val grade = gradeText.toFloatOrNull()
                    if (grade != null && grade >= 0 && grade <= 20) {
                        val idGrade = existingGrade?.idGrade ?: (0..100000).random()
                        val gradeEntity = GradeEntity(
                            idGrade = idGrade,
                            studentId = student.idStudent,
                            courseId = course.idCourse,
                            teacherId = teacherId,
                            grade = grade,
                            dateGiven = System.currentTimeMillis()
                        )
                        onGradeSave(gradeEntity)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    if (existingGrade != null) 
                        context.getString(R.string.update_grade) 
                    else 
                        context.getString(R.string.save_grade)
                )
            }
        }
    }
}

/**
 * ViewModel gérant la saisie des notes par l'enseignant.
 * Charge les cours de l'enseignant, les étudiants inscrits et les notes existantes.
 * Permet d'enregistrer ou modifier les notes des étudiants.
 */
@HiltViewModel
class TeacherGradesViewModel @Inject constructor(
    private val repository: SCRUDRepository
) : ViewModel() {
    
    private val _teacherCourses = MutableStateFlow<List<CourseEntity>>(emptyList())
    val teacherCourses: StateFlow<List<CourseEntity>> = _teacherCourses
    
    private val _students = MutableStateFlow<List<StudentEntity>>(emptyList())
    val students: StateFlow<List<StudentEntity>> = _students
    
    private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
    val grades: StateFlow<List<GradeEntity>> = _grades
    
    // Charge les cours enseignés par l'enseignant
    fun loadTeacherCourses(teacherId: Int) {
        viewModelScope.launch {
            repository.getAllCourses().collect { courses ->
                _teacherCourses.value = courses.filter { it.teacherId == teacherId }
            }
        }
    }
    
    // Charge les étudiants inscrits à un cours spécifique
    fun loadStudentsForCourse(courseId: Int) {
        viewModelScope.launch {
            repository.getSubscribesByCourse(courseId).collect { subscriptions ->
                val studentIds = subscriptions.map { it.studentId }
                val allStudents = repository.getAllStudents().first()
                _students.value = allStudents.filter { it.idStudent in studentIds }
            }
        }
    }
    
    // Charge les notes existantes pour un cours
    fun loadGradesForCourse(courseId: Int) {
        viewModelScope.launch {
            repository.getGradesByCourse(courseId).collect { gradesList ->
                _grades.value = gradesList
            }
        }
    }
    
    // Enregistre ou met à jour une note dans la base de données
    fun saveGrade(grade: GradeEntity) {
        viewModelScope.launch {
            repository.insertGrade(grade)
        }
    }
}


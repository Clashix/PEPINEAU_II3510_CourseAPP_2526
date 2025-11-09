package com.tumme.scrudstudents.ui.course

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel gérant la liste des cours et les opérations CRUD.
 * Observe les changements dans la base de données et expose la liste aux écrans.
 * Permet de filtrer les cours par niveau ou par enseignant.
 */
@HiltViewModel
class CourseListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val courses: StateFlow<List<CourseEntity>> = _courses
    
    // Filtre les cours par niveau (utilisé pour les étudiants)
    fun filterCoursesByLevel(level: com.tumme.scrudstudents.data.local.model.LevelCourse): Flow<List<CourseEntity>> =
        repo.getCoursesByLevel(level)
    
    // Filtre les cours par enseignant (utilisé pour les enseignants)
    fun filterCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>> =
        repo.getCoursesByTeacher(teacherId)

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    // Supprime un cours de la base de données
    fun deleteCourse(course: CourseEntity) = viewModelScope.launch {
        repo.deleteCourse(course)
        _events.emit("Course deleted")
    }

    // Ajoute un nouveau cours dans la base de données
    fun insertCourse(course: CourseEntity) = viewModelScope.launch {
        repo.insertCourse(course)
        _events.emit("Course inserted")
    }

    // Trouve un cours par son ID
    suspend fun findCourse(id: Int) = repo.getCourseById(id)

}

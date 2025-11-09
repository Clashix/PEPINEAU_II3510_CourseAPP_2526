package com.tumme.scrudstudents.ui.subscribe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel gérant la liste des inscriptions et les opérations CRUD.
 * Observe les inscriptions, étudiants et cours pour afficher les noms au lieu des IDs.
 * Fournit des méthodes pour récupérer les noms à partir des IDs.
 */
@HiltViewModel
class SubscribeListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _subscribes: StateFlow<List<SubscribeEntity>> =
        repo.getAllSubscribes().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val subscribes: StateFlow<List<SubscribeEntity>> = _subscribes

    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val students: StateFlow<List<StudentEntity>> = _students

    private val _courses: StateFlow<List<CourseEntity>> =
        repo.getAllCourses().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val courses: StateFlow<List<CourseEntity>> = _courses

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    // Supprime une inscription de la base de données
    fun deleteSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.deleteSubscribe(subscribe)
        _events.emit("Inscription supprimée")
    }

    // Ajoute une nouvelle inscription dans la base de données
    fun insertSubscribe(subscribe: SubscribeEntity) = viewModelScope.launch {
        repo.insertSubscribe(subscribe)
        _events.emit("Inscription ajoutée")
    }

    suspend fun findStudent(id: Int) = repo.getStudentById(id)

    suspend fun findCourse(id: Int) = repo.getCourseById(id)

    // Récupère le nom complet d'un étudiant à partir de son ID
    fun getStudentName(studentId: Int): String {
        return students.value.find { it.idStudent == studentId }?.let { 
            "${it.firstName} ${it.lastName}" 
        } ?: "Étudiant inconnu"
    }

    // Récupère le nom d'un cours à partir de son ID
    fun getCourseName(courseId: Int): String {
        return courses.value.find { it.idCourse == courseId }?.nameCourse 
            ?: "Cours inconnu"
    }
}

package com.tumme.scrudstudents.ui.student

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel gérant la liste des étudiants et les opérations CRUD.
 * Observe les changements dans la base de données et expose la liste aux écrans.
 * Gère les opérations d'ajout, suppression et recherche d'étudiants.
 */
@HiltViewModel
class StudentListViewModel @Inject constructor(
    private val repo: SCRUDRepository
) : ViewModel() {

    private val _students: StateFlow<List<StudentEntity>> =
        repo.getAllStudents().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
    val students: StateFlow<List<StudentEntity>> = _students

    private val _events = MutableSharedFlow<String>()
    val events = _events.asSharedFlow()

    // Supprime un étudiant de la base de données
    fun deleteStudent(student: StudentEntity) = viewModelScope.launch {
        repo.deleteStudent(student)
        _events.emit("Student deleted")
    }

    // Ajoute un nouvel étudiant dans la base de données
    fun insertStudent(student: StudentEntity) = viewModelScope.launch {
        repo.insertStudent(student)
        _events.emit("Student inserted")
    }

    // Trouve un étudiant par son ID
    suspend fun findStudent(id: Int) = repo.getStudentById(id)

}
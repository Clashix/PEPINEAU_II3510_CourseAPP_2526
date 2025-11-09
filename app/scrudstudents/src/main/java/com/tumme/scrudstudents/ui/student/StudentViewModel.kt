package com.tumme.scrudstudents.ui.student

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel gérant les informations de l'étudiant connecté.
 * Charge les données de l'étudiant depuis AuthRepository et les expose à l'UI.
 * Permet de gérer la déconnexion de l'étudiant.
 */
@HiltViewModel
class StudentViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _currentStudent = mutableStateOf<StudentEntity?>(null)
    val currentStudent: State<StudentEntity?> = _currentStudent
    
    init {
        // Charge les informations de l'étudiant au démarrage
        viewModelScope.launch {
            try {
                val student = authRepository.getCurrentStudent()
                _currentStudent.value = student
            } catch (e: Exception) {
                _currentStudent.value = null
            }
        }
    }
    
    // Déconnecte l'étudiant et réinitialise l'état
    fun logout() {
        authRepository.logout()
    }
}

package com.tumme.scrudstudents.ui.teacher

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel gérant les informations de l'enseignant connecté.
 * Charge les données de l'enseignant depuis AuthRepository et les expose à l'UI.
 * Permet de gérer la déconnexion de l'enseignant.
 */
@HiltViewModel
class TeacherViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _currentTeacher = mutableStateOf<TeacherEntity?>(null)
    val currentTeacher: State<TeacherEntity?> = _currentTeacher
    
    init {
        // Charge les informations de l'enseignant au démarrage
        viewModelScope.launch {
            try {
                val teacher = authRepository.getCurrentTeacher()
                _currentTeacher.value = teacher
            } catch (e: Exception) {
                _currentTeacher.value = null
            }
        }
    }
    
    // Déconnecte l'enseignant et réinitialise l'état
    fun logout() {
        authRepository.logout()
    }
}

package com.tumme.scrudstudents.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * ViewModel gérant l'authentification (connexion et inscription).
 * Fait le lien entre l'UI et le AuthRepository. Expose l'état d'authentification aux écrans.
 * Gère la création de comptes étudiants et enseignants avec leurs informations respectives.
 */
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    val isAuthenticated: StateFlow<Boolean> = authRepository.isAuthenticated
    val currentUser: StateFlow<com.tumme.scrudstudents.data.local.model.UserEntity?> = authRepository.currentUser
    val userRole: StateFlow<com.tumme.scrudstudents.data.local.model.UserRole?> = authRepository.userRole
    
    // Tente de connecter l'utilisateur avec les identifiants fournis
    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val success = authRepository.login(username, password)
            onResult(success)
        }
    }
    
    // Déconnecte l'utilisateur actuel
    fun logout() {
        authRepository.logout()
    }
    
    // Crée un nouveau compte étudiant avec les informations fournies
    fun registerStudent(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        level: LevelCourse,
        gender: Gender,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Générer un ID unique pour l'étudiant
            val studentId = System.currentTimeMillis().toInt()
            
            val student = StudentEntity(
                idStudent = studentId,
                lastName = lastName,
                firstName = firstName,
                dateOfBirth = Date(), // Date actuelle par défaut
                gender = gender,
                level = level,
                email = email
            )
            
            val success = authRepository.registerStudent(username, password, student)
            onResult(success)
        }
    }
    
    // Crée un nouveau compte enseignant avec les informations fournies
    fun registerTeacher(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
        email: String,
        department: String,
        gender: Gender,
        onResult: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            // Générer un ID unique pour l'enseignant
            val teacherId = System.currentTimeMillis().toInt()
            
            val teacher = TeacherEntity(
                idTeacher = teacherId,
                lastName = lastName,
                firstName = firstName,
                email = email,
                department = department,
                dateOfBirth = Date(), // Date actuelle par défaut
                gender = gender
            )
            
            val success = authRepository.registerTeacher(username, password, teacher)
            onResult(success)
        }
    }
}

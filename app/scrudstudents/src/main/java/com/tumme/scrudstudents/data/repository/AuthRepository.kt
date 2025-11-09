package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.UserDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.model.UserEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.UserRole
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository gérant l'authentification et les rôles des utilisateurs.
 * Gère la connexion, l'inscription et le suivi de l'utilisateur connecté.
 * Maintient l'état d'authentification dans des StateFlow pour que l'UI réagisse aux changements.
 */
@Singleton
class AuthRepository @Inject constructor(
    private val userDao: UserDao,
    private val studentDao: StudentDao,
    private val teacherDao: TeacherDao
) {
    
    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated
    
    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser
    
    private val _userRole = MutableStateFlow<UserRole?>(null)
    val userRole: StateFlow<UserRole?> = _userRole
    
    // Vérifie les identifiants et connecte l'utilisateur si valides
    suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUserByUsername(username)
        return if (user != null && user.password == password) {
            _isAuthenticated.value = true
            _currentUser.value = user
            _userRole.value = user.role
            true
        } else {
            false
        }
    }
    
    // Déconnecte l'utilisateur actuel
    fun logout() {
        _isAuthenticated.value = false
        _currentUser.value = null
        _userRole.value = null
    }
    
    // Crée un nouveau compte étudiant (vérifie d'abord que le username n'existe pas)
    suspend fun registerStudent(username: String, password: String, student: StudentEntity): Boolean {
        if (userDao.getUserByUsername(username) != null) {
            return false
        }
        
        studentDao.insert(student)
        
        val user = UserEntity(
            idUser = student.idStudent,
            username = username,
            password = password,
            role = UserRole.STUDENT,
            studentId = student.idStudent,
            teacherId = null
        )
        userDao.insert(user)
        
        return true
    }
    
    // Crée un nouveau compte enseignant (vérifie d'abord que le username n'existe pas)
    suspend fun registerTeacher(username: String, password: String, teacher: TeacherEntity): Boolean {
        if (userDao.getUserByUsername(username) != null) {
            return false
        }
        
        teacherDao.insert(teacher)
        
        val user = UserEntity(
            idUser = teacher.idTeacher,
            username = username,
            password = password,
            role = UserRole.TEACHER,
            studentId = null,
            teacherId = teacher.idTeacher
        )
        userDao.insert(user)
        
        return true
    }
    
    // Récupère l'étudiant actuellement connecté
    suspend fun getCurrentStudent(): StudentEntity? {
        val user = _currentUser.value
        return if (user?.studentId != null) {
            studentDao.getStudentById(user.studentId)
        } else {
            null
        }
    }
    
    // Récupère l'enseignant actuellement connecté
    suspend fun getCurrentTeacher(): TeacherEntity? {
        val user = _currentUser.value
        return if (user?.teacherId != null) {
            teacherDao.getTeacherById(user.teacherId)
        } else {
            null
        }
    }
}

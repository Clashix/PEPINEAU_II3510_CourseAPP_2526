package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Représente un utilisateur dans la base de données Room pour l'authentification.
 * Stocke les identifiants de connexion (username, password) et le rôle.
 * Lié soit à StudentEntity soit à TeacherEntity selon le rôle de l'utilisateur.
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val idUser: Int,           // ID unique de l'utilisateur
    val username: String,                   // Nom d'utilisateur pour la connexion
    val password: String,                   // Mot de passe (en production, utiliser un hash)
    val role: UserRole,                     // Rôle de l'utilisateur (STUDENT ou TEACHER)
    val studentId: Int? = null,             // ID de l'étudiant si c'est un étudiant
    val teacherId: Int? = null              // ID de l'enseignant si c'est un enseignant
)

/**
 * Enum définissant les rôles possibles pour les utilisateurs.
 * Utilisé pour différencier les étudiants et les enseignants dans l'application.
 */
enum class UserRole(val value: String) {
    STUDENT("STUDENT"),
    TEACHER("TEACHER");
    
    companion object {
        fun from(value: String) = UserRole.entries.firstOrNull { it.value == value } ?: STUDENT
    }
}

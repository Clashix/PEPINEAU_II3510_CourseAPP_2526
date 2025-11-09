package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Représente un enseignant dans la base de données Room.
 * Stocke les informations personnelles et professionnelles de l'enseignant.
 * Lié à UserEntity pour l'authentification et à CourseEntity pour les cours enseignés.
 */
@Entity(tableName = "teachers")
data class TeacherEntity(
    @PrimaryKey val idTeacher: Int,        // ID unique de l'enseignant
    val lastName: String,                  // Nom de famille
    val firstName: String,                 // Prénom
    val email: String,                     // Email de l'enseignant
    val department: String,                 // Département
    val dateOfBirth: Date,                 // Date de naissance
    val gender: Gender                     // Genre (enum converti en String par Room)
)

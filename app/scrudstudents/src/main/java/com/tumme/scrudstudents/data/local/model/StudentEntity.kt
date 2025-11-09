package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Représente un étudiant dans la base de données Room.
 * Stocke les informations personnelles de l'étudiant : nom, prénom, date de naissance.
 * Contient aussi le niveau d'étude et l'email pour l'inscription aux cours.
 */
@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey val idStudent: Int,        // ID unique de l'étudiant
    val lastName: String,                  // Nom de famille
    val firstName: String,                 // Prénom
    val dateOfBirth: Date,                 // Date de naissance (Room la convertit en Long)
    val gender: Gender,                    // Genre (enum converti en String par Room)
    val level: LevelCourse,                // Niveau d'étude de l'étudiant
    val email: String                      // Email de l'étudiant
)
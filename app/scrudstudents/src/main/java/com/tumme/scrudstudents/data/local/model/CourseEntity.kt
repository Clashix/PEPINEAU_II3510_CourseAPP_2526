package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Représente un cours dans la base de données Room.
 * Stocke les informations d'un cours : nom, ECTS, niveau, enseignant.
 * Utilisé pour gérer les cours disponibles et les inscriptions des étudiants.
 */
@Entity(tableName = "courses")
data class CourseEntity(
    @PrimaryKey val idCourse: Int,        // ID unique du cours
    val nameCourse: String,               // Nom du cours
    val ectsCourse: Float,                // Nombre de crédits ECTS
    val levelCourse: LevelCourse,         // Niveau du cours (enum converti en String par Room)
    val teacherId: Int,                   // ID de l'enseignant qui enseigne ce cours
    val description: String               // Description du cours
)
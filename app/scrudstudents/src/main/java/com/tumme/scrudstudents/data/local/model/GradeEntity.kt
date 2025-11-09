package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

/**
 * Représente une note dans la base de données Room.
 * Lie un étudiant, un cours et une note donnée par un enseignant.
 * Permet de calculer les moyennes pondérées par ECTS pour chaque étudiant.
 */
@Entity(
    tableName = "grades",
    foreignKeys = [
        ForeignKey(
            entity = StudentEntity::class, 
            parentColumns = ["idStudent"], 
            childColumns = ["studentId"], 
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CourseEntity::class, 
            parentColumns = ["idCourse"], 
            childColumns = ["courseId"], 
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("studentId"), Index("courseId")]
)
data class GradeEntity(
    @PrimaryKey val idGrade: Int,          // ID unique de la note
    val studentId: Int,                   // ID de l'étudiant
    val courseId: Int,                     // ID du cours
    val grade: Float,                      // Note (0-20)
    val teacherId: Int,                    // ID de l'enseignant qui a donné la note
    val dateGiven: Long                    // Date de la note (timestamp)
)

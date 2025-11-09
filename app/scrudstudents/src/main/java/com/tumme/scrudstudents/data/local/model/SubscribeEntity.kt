package com.tumme.scrudstudents.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.ColumnInfo

/**
 * Représente l'inscription d'un étudiant à un cours dans la base de données Room.
 * Crée une relation many-to-many entre les étudiants et les cours.
 * Contient aussi un score préliminaire pour l'inscription.
 */
@Entity(
    tableName = "subscribes",
    primaryKeys = ["studentId", "courseId"],
    foreignKeys = [
        ForeignKey(entity = StudentEntity::class, parentColumns = ["idStudent"], childColumns = ["studentId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CourseEntity::class, parentColumns = ["idCourse"], childColumns = ["courseId"], onDelete = ForeignKey.CASCADE)
    ],
    indices = [Index("studentId"), Index("courseId")]
)
data class SubscribeEntity(
    val studentId: Int,
    val courseId: Int,
    val score: Float
)
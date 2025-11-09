package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.CourseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des cours.
 * Définit toutes les requêtes SQL pour accéder aux données des cours.
 * Room génère automatiquement l'implémentation de cette interface.
 */
@Dao
interface CourseDao {
    
    // Récupère tous les cours triés par nom
    @Query("SELECT * FROM courses ORDER BY nameCourse")
    fun getAllCourses(): Flow<List<CourseEntity>>

    // Ajoute un nouveau cours (remplace si existe déjà)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(course: CourseEntity)

    // Supprime un cours
    @Delete
    suspend fun delete(course: CourseEntity)

    // Trouve un cours par son ID
    @Query("SELECT * FROM courses WHERE idCourse = :id LIMIT 1")
    suspend fun getCourseById(id: Int): CourseEntity?
    
    // Récupère les cours d'un niveau spécifique
    @Query("SELECT * FROM courses WHERE levelCourse = :level ORDER BY nameCourse")
    fun getCoursesByLevel(level: String): Flow<List<CourseEntity>>
    
    // Récupère les cours enseignés par un enseignant
    @Query("SELECT * FROM courses WHERE teacherId = :teacherId ORDER BY nameCourse")
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>>
}
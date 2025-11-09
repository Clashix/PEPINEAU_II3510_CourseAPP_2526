package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des enseignants.
 * Définit toutes les requêtes SQL pour accéder aux données des enseignants.
 * Room génère automatiquement l'implémentation de cette interface.
 */
@Dao
interface TeacherDao {
    
    // Récupère tous les enseignants
    @Query("SELECT * FROM teachers")
    fun getAllTeachers(): Flow<List<TeacherEntity>>
    
    // Trouve un enseignant par son ID
    @Query("SELECT * FROM teachers WHERE idTeacher = :id")
    suspend fun getTeacherById(id: Int): TeacherEntity?
    
    // Ajoute un nouvel enseignant
    @Insert
    suspend fun insert(teacher: TeacherEntity)
    
    // Met à jour un enseignant existant
    @Update
    suspend fun update(teacher: TeacherEntity)
    
    // Supprime un enseignant
    @Delete
    suspend fun delete(teacher: TeacherEntity)
}

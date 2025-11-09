package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.StudentEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des étudiants.
 * Définit toutes les requêtes SQL pour accéder aux données des étudiants.
 * Room génère automatiquement l'implémentation de cette interface.
 */
@Dao
interface StudentDao {
    
    // Récupère tous les étudiants triés par nom puis prénom
    @Query("SELECT * FROM students ORDER BY lastName, firstName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    // Ajoute un nouvel étudiant (remplace si existe déjà)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(student: StudentEntity)

    // Supprime un étudiant
    @Delete
    suspend fun delete(student: StudentEntity)

    // Trouve un étudiant par son ID
    @Query("SELECT * FROM students WHERE idStudent = :id LIMIT 1")
    suspend fun getStudentById(id: Int): StudentEntity?
}
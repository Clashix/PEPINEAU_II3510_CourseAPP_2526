package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.GradeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des notes.
 * Définit toutes les requêtes SQL pour gérer les notes des étudiants.
 * Permet de filtrer les notes par étudiant, cours ou enseignant.
 */
@Dao
interface GradeDao {
    
    // Récupère toutes les notes
    @Query("SELECT * FROM grades")
    fun getAllGrades(): Flow<List<GradeEntity>>
    
    // Récupère toutes les notes d'un étudiant
    @Query("SELECT * FROM grades WHERE studentId = :studentId")
    fun getGradesByStudent(studentId: Int): Flow<List<GradeEntity>>
    
    // Récupère toutes les notes d'un cours
    @Query("SELECT * FROM grades WHERE courseId = :courseId")
    fun getGradesByCourse(courseId: Int): Flow<List<GradeEntity>>
    
    // Récupère toutes les notes données par un enseignant
    @Query("SELECT * FROM grades WHERE teacherId = :teacherId")
    fun getGradesByTeacher(teacherId: Int): Flow<List<GradeEntity>>
    
    // Trouve une note par son ID
    @Query("SELECT * FROM grades WHERE idGrade = :id")
    suspend fun getGradeById(id: Int): GradeEntity?
    
    // Ajoute une nouvelle note
    @Insert
    suspend fun insert(grade: GradeEntity)
    
    // Met à jour une note existante
    @Update
    suspend fun update(grade: GradeEntity)
    
    // Supprime une note
    @Delete
    suspend fun delete(grade: GradeEntity)
}

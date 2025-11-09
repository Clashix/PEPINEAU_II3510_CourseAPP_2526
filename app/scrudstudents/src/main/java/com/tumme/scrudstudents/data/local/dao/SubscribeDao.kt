package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des inscriptions.
 * Définit toutes les requêtes SQL pour gérer les inscriptions étudiants-cours.
 * Permet de lier les étudiants aux cours et de récupérer les inscriptions.
 */
@Dao
interface SubscribeDao {
    // Récupère toutes les inscriptions
    @Query("SELECT * FROM subscribes")
    fun getAllSubscribes(): Flow<List<SubscribeEntity>>

    // Ajoute une nouvelle inscription (remplace si existe déjà)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(subscribe: SubscribeEntity)

    // Supprime une inscription
    @Delete
    suspend fun delete(subscribe: SubscribeEntity)

    // Récupère toutes les inscriptions d'un étudiant
    @Query("SELECT * FROM subscribes WHERE studentId = :sId")
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>>

    // Récupère toutes les inscriptions d'un cours
    @Query("SELECT * FROM subscribes WHERE courseId = :cId")
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>>
}
package com.tumme.scrudstudents.data.local.dao

import androidx.room.*
import com.tumme.scrudstudents.data.local.model.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO pour les opérations sur la table des utilisateurs (authentification).
 * Définit toutes les requêtes SQL pour gérer les comptes utilisateurs.
 * Utilisé pour la connexion et la gestion des rôles.
 */
@Dao
interface UserDao {
    
    // Récupère tous les utilisateurs
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>
    
    // Trouve un utilisateur par son nom d'utilisateur (pour la connexion)
    @Query("SELECT * FROM users WHERE username = :username")
    suspend fun getUserByUsername(username: String): UserEntity?
    
    // Trouve un utilisateur par son ID
    @Query("SELECT * FROM users WHERE idUser = :id")
    suspend fun getUserById(id: Int): UserEntity?
    
    // Ajoute un nouvel utilisateur
    @Insert
    suspend fun insert(user: UserEntity)
    
    // Met à jour un utilisateur existant
    @Update
    suspend fun update(user: UserEntity)
    
    // Supprime un utilisateur
    @Delete
    suspend fun delete(user: UserEntity)
}

package com.tumme.scrudstudents.data.local.model

/**
 * Enum définissant les genres possibles pour les utilisateurs.
 * Utilisé dans StudentEntity et TeacherEntity pour stocker le genre.
 */
enum class Gender(val value: String) {
    MALE("MALE"),
    FEMALE("FEMALE"),
    OTHER("OTHER");
    
    companion object {
        fun from(value: String) = Gender.entries.firstOrNull { it.value == value } ?: MALE
    }
}
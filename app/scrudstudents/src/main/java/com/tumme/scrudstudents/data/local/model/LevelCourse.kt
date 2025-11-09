package com.tumme.scrudstudents.data.local.model

/**
 * Enum définissant tous les niveaux de cours possibles (P1, P2, B1, etc.).
 * Utilisé dans CourseEntity pour spécifier le niveau d'un cours
 * et dans StudentEntity pour indiquer le niveau d'étude de l'étudiant.
 */
enum class LevelCourse(val value: String) {
    P1("P1"),
    P2("P2"),
    P3("P3"),
    B1("B1"),
    B2("B2"),
    B3("B3"),
    A1("A1"),
    A2("A2"),
    A3("A3"),
    MS("MS"),
    PhD("PhD");

    companion object {
        fun from(value: String) = LevelCourse.entries.firstOrNull { it.value == value } ?: P1
    }
}
package com.tumme.scrudstudents.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

import com.tumme.scrudstudents.ui.student.StudentListScreen
import com.tumme.scrudstudents.ui.student.StudentFormScreen
import com.tumme.scrudstudents.ui.student.StudentDetailScreen
import com.tumme.scrudstudents.ui.student.StudentHomeScreen
import com.tumme.scrudstudents.ui.student.StudentGradesScreen
import com.tumme.scrudstudents.ui.student.StudentSummaryScreen
import com.tumme.scrudstudents.ui.course.CourseListScreen
import com.tumme.scrudstudents.ui.course.CourseFormScreen
import com.tumme.scrudstudents.ui.course.CourseDetailScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeListScreen
import com.tumme.scrudstudents.ui.subscribe.SubscribeFormScreen
import com.tumme.scrudstudents.ui.auth.LoginScreen
import com.tumme.scrudstudents.ui.auth.RegisterScreen
import com.tumme.scrudstudents.ui.teacher.TeacherHomeScreen
import com.tumme.scrudstudents.ui.teacher.TeacherGradesScreen

/**
 * Gestion de la navigation entre les écrans de l'application.
 * Utilise Jetpack Navigation Compose pour définir les routes et les destinations.
 * Définit toutes les routes possibles et orchestre la navigation selon le rôle de l'utilisateur.
 */
object Routes {
    // Authentification
    const val LOGIN = "login"                                  // Route vers la connexion
    const val REGISTER = "register"                            // Route vers l'inscription
    
    // Étudiant
    const val STUDENT_HOME = "student_home"                    // Route vers l'accueil étudiant
    const val STUDENT_COURSES = "student_courses"              // Route vers les cours disponibles
    const val STUDENT_SUBSCRIPTIONS = "student_subscriptions" // Route vers les inscriptions
    const val STUDENT_GRADES = "student_grades"                // Route vers les notes
    const val STUDENT_SUMMARY = "student_summary"              // Route vers le résumé final
    
    // Enseignant
    const val TEACHER_HOME = "teacher_home"                    // Route vers l'accueil enseignant
    const val TEACHER_COURSES = "teacher_courses"              // Route vers les cours de l'enseignant
    const val TEACHER_GRADES = "teacher_grades"                // Route vers la saisie des notes
    const val TEACHER_STUDENTS = "teacher_students"           // Route vers les étudiants inscrits
    
    // Administration (ancien système)
    const val STUDENT_LIST = "student_list"                    // Route vers la liste des étudiants
    const val STUDENT_FORM = "student_form"                    // Route vers le formulaire d'ajout
    const val STUDENT_DETAIL = "student_detail/{studentId}"    // Route vers les détails (avec paramètre)
    const val COURSE_LIST = "course_list"                      // Route vers la liste des cours
    const val COURSE_FORM = "course_form"                      // Route vers le formulaire d'ajout de cours
    const val COURSE_DETAIL = "course_detail/{courseId}"       // Route vers les détails de cours (avec paramètre)
    const val SUBSCRIBE_LIST = "subscribe_list"                // Route vers la liste des inscriptions
    const val SUBSCRIBE_FORM = "subscribe_form"                // Route vers le formulaire d'ajout d'inscription
}

/**
 * Host de navigation principal définissant toutes les routes de l'application.
 * Gère la navigation entre les écrans selon le rôle de l'utilisateur (étudiant ou enseignant).
 * Définit les paramètres de route et les callbacks de navigation pour chaque écran.
 */
@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    
    NavHost(navController, startDestination = Routes.LOGIN) {
        
        // Écran de connexion (point d'entrée de l'application)
        composable(Routes.LOGIN) {
            LoginScreen(navController = navController)
        }
        
        // Écran d'inscription (création de compte étudiant ou enseignant)
        composable(Routes.REGISTER) {
            RegisterScreen(navController = navController)
        }
        
        // Accueil étudiant (après connexion)
        composable(Routes.STUDENT_HOME) {
            StudentHomeScreen(navController = navController)
        }
        
        // Accueil enseignant (après connexion)
        composable(Routes.TEACHER_HOME) {
            TeacherHomeScreen(navController = navController)
        }
        
        // ==================== ÉCRANS ÉTUDIANT ====================
        
        // Liste des cours disponibles pour l'étudiant
        composable(Routes.STUDENT_COURSES) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") },
                onNavigateToStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onNavigateToSubscribes = { navController.navigate(Routes.SUBSCRIBE_LIST) }
            )
        }
        
        // Liste des inscriptions de l'étudiant
        composable(Routes.STUDENT_SUBSCRIPTIONS) {
            SubscribeListScreen(
                onNavigateToForm = { navController.navigate(Routes.SUBSCRIBE_FORM) },
                onNavigateToStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onNavigateToCourses = { navController.navigate(Routes.COURSE_LIST) }
            )
        }
        
        // Notes de l'étudiant
        composable(Routes.STUDENT_GRADES) {
            StudentGradesScreen(navController = navController)
        }
        
        // Résumé final avec moyenne pondérée de l'étudiant
        composable(Routes.STUDENT_SUMMARY) {
            StudentSummaryScreen(navController = navController)
        }
        
        // ==================== ÉCRANS ENSEIGNANT ====================
        
        // Liste des cours enseignés par l'enseignant
        composable(Routes.TEACHER_COURSES) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") },
                onNavigateToStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onNavigateToSubscribes = { navController.navigate(Routes.SUBSCRIBE_LIST) }
            )
        }
        
        // Saisie des notes par l'enseignant
        composable(Routes.TEACHER_GRADES) {
            TeacherGradesScreen(navController = navController)
        }
        
        // Liste des étudiants inscrits aux cours de l'enseignant
        composable(Routes.TEACHER_STUDENTS) {
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") },
                onNavigateToCourses = { navController.navigate(Routes.COURSE_LIST) },
                onNavigateToSubscribes = { navController.navigate(Routes.SUBSCRIBE_LIST) }
            )
        }
        
        // ==================== ÉCRANS ADMINISTRATION (ANCIEN SYSTÈME) ====================
        
        // Liste de tous les étudiants (administration)
        composable(Routes.STUDENT_LIST) {
            StudentListScreen(
                onNavigateToForm = { navController.navigate(Routes.STUDENT_FORM) },
                onNavigateToDetail = { id -> navController.navigate("student_detail/$id") },
                onNavigateToCourses = { navController.navigate(Routes.COURSE_LIST) },
                onNavigateToSubscribes = { navController.navigate(Routes.SUBSCRIBE_LIST) }
            )
        }
        
        // Formulaire d'ajout d'étudiant
        composable(Routes.STUDENT_FORM) {
            StudentFormScreen(onSaved = { navController.popBackStack() })
        }
        
        // Détails d'un étudiant (avec paramètre studentId)
        composable(
            "student_detail/{studentId}", 
            arguments = listOf(navArgument("studentId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("studentId") ?: 0
            StudentDetailScreen(
                studentId = id, 
                onBack = { navController.popBackStack() }
            )
        }
        
        // Liste de tous les cours (administration)
        composable(Routes.COURSE_LIST) {
            CourseListScreen(
                onNavigateToForm = { navController.navigate(Routes.COURSE_FORM) },
                onNavigateToDetail = { id -> navController.navigate("course_detail/$id") },
                onNavigateToStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onNavigateToSubscribes = { navController.navigate(Routes.SUBSCRIBE_LIST) }
            )
        }
        
        // Formulaire d'ajout de cours
        composable(Routes.COURSE_FORM) {
            CourseFormScreen(onSaved = { navController.popBackStack() })
        }
        
        // Détails d'un cours (avec paramètre courseId)
        composable(
            "course_detail/{courseId}", 
            arguments = listOf(navArgument("courseId"){ type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("courseId") ?: 0
            CourseDetailScreen(
                courseId = id, 
                onBack = { navController.popBackStack() }
            )
        }
        
        // Liste de toutes les inscriptions (administration)
        composable(Routes.SUBSCRIBE_LIST) {
            SubscribeListScreen(
                onNavigateToForm = { navController.navigate(Routes.SUBSCRIBE_FORM) },
                onNavigateToStudents = { navController.navigate(Routes.STUDENT_LIST) },
                onNavigateToCourses = { navController.navigate(Routes.COURSE_LIST) }
            )
        }
        
        // Formulaire d'ajout d'inscription
        composable(Routes.SUBSCRIBE_FORM) {
            SubscribeFormScreen(onSaved = { navController.popBackStack() })
        }
    }
}
package com.tumme.scrudstudents.data.repository

import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.dao.GradeDao
import com.tumme.scrudstudents.data.local.model.CourseEntity
import com.tumme.scrudstudents.data.local.model.StudentEntity
import com.tumme.scrudstudents.data.local.model.SubscribeEntity
import com.tumme.scrudstudents.data.local.model.TeacherEntity
import com.tumme.scrudstudents.data.local.model.GradeEntity
import com.tumme.scrudstudents.data.local.model.LevelCourse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Repository centralisant toutes les opérations sur les données.
 * Fait le lien entre les ViewModel et les DAO. Délègue les opérations CRUD aux DAO correspondants.
 * Permet de centraliser la logique métier et facilite les tests en pouvant mocker le repository.
 */
class SCRUDRepository(
    private val studentDao: StudentDao,
    private val courseDao: CourseDao,
    private val subscribeDao: SubscribeDao,
    private val teacherDao: TeacherDao,
    private val gradeDao: GradeDao
) {
    
    // Opérations sur les étudiants
    fun getAllStudents(): Flow<List<StudentEntity>> = studentDao.getAllStudents()
    suspend fun insertStudent(student: StudentEntity) = studentDao.insert(student)
    suspend fun deleteStudent(student: StudentEntity) = studentDao.delete(student)
    suspend fun getStudentById(id: Int) = studentDao.getStudentById(id)

    // Opérations sur les cours
    fun getAllCourses(): Flow<List<CourseEntity>> = courseDao.getAllCourses()
    // Récupère les cours d'un niveau spécifique (utilisé pour filtrer par niveau d'étude)
    fun getCoursesByLevel(level: com.tumme.scrudstudents.data.local.model.LevelCourse): Flow<List<CourseEntity>> = 
        courseDao.getCoursesByLevel(level.value)
    // Récupère les cours enseignés par un enseignant
    fun getCoursesByTeacher(teacherId: Int): Flow<List<CourseEntity>> = 
        courseDao.getCoursesByTeacher(teacherId)
    suspend fun insertCourse(course: CourseEntity) = courseDao.insert(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.delete(course)
    suspend fun getCourseById(id: Int) = courseDao.getCourseById(id)

    // Opérations sur les inscriptions
    fun getAllSubscribes(): Flow<List<SubscribeEntity>> = subscribeDao.getAllSubscribes()
    // Récupère toutes les inscriptions d'un étudiant
    fun getSubscribesByStudent(sId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByStudent(sId)
    // Récupère toutes les inscriptions d'un cours
    fun getSubscribesByCourse(cId: Int): Flow<List<SubscribeEntity>> = subscribeDao.getSubscribesByCourse(cId)
    suspend fun insertSubscribe(subscribe: SubscribeEntity) = subscribeDao.insert(subscribe)
    suspend fun deleteSubscribe(subscribe: SubscribeEntity) = subscribeDao.delete(subscribe)

    // Opérations sur les enseignants
    fun getAllTeachers(): Flow<List<TeacherEntity>> = teacherDao.getAllTeachers()
    suspend fun insertTeacher(teacher: TeacherEntity) = teacherDao.insert(teacher)
    suspend fun deleteTeacher(teacher: TeacherEntity) = teacherDao.delete(teacher)
    suspend fun getTeacherById(id: Int) = teacherDao.getTeacherById(id)

    // Opérations sur les notes
    fun getAllGrades(): Flow<List<GradeEntity>> = gradeDao.getAllGrades()
    // Récupère toutes les notes d'un étudiant
    fun getGradesByStudent(studentId: Int): Flow<List<GradeEntity>> = gradeDao.getGradesByStudent(studentId)
    // Récupère toutes les notes d'un cours
    fun getGradesByCourse(courseId: Int): Flow<List<GradeEntity>> = gradeDao.getGradesByCourse(courseId)
    // Récupère toutes les notes données par un enseignant
    fun getGradesByTeacher(teacherId: Int): Flow<List<GradeEntity>> = gradeDao.getGradesByTeacher(teacherId)
    suspend fun insertGrade(grade: GradeEntity) = gradeDao.insert(grade)
    suspend fun updateGrade(grade: GradeEntity) = gradeDao.update(grade)
    suspend fun deleteGrade(grade: GradeEntity) = gradeDao.delete(grade)
    
    // Calcule la moyenne pondérée par ECTS d'un étudiant pour un niveau donné
    suspend fun calculateStudentAverage(studentId: Int, level: LevelCourse): Float {
        val gradesList = gradeDao.getGradesByStudent(studentId).first()
        
        if (gradesList.isEmpty()) return 0.0f
        
        var totalWeighted = 0.0f
        var totalEcts = 0.0f
        
        for (grade in gradesList) {
            val course = courseDao.getCourseById(grade.courseId)
            if (course != null && course.levelCourse == level) {
                totalWeighted += grade.grade * course.ectsCourse
                totalEcts += course.ectsCourse
            }
        }
        
        return if (totalEcts > 0) totalWeighted / totalEcts else 0.0f
    }
}
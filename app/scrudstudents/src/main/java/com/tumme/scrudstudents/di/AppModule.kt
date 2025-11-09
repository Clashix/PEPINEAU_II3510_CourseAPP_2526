package com.tumme.scrudstudents.di

import android.content.Context
import androidx.room.Room
import com.tumme.scrudstudents.data.local.AppDatabase
import com.tumme.scrudstudents.data.local.dao.CourseDao
import com.tumme.scrudstudents.data.local.dao.StudentDao
import com.tumme.scrudstudents.data.local.dao.SubscribeDao
import com.tumme.scrudstudents.data.local.dao.UserDao
import com.tumme.scrudstudents.data.local.dao.TeacherDao
import com.tumme.scrudstudents.data.local.dao.GradeDao
import com.tumme.scrudstudents.data.repository.SCRUDRepository
import com.tumme.scrudstudents.data.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext

/**
 * Module Hilt pour l'injection de dépendances.
 * Définit comment créer et injecter les dépendances de l'application (base de données, DAO, repositories).
 * Utilise Hilt pour l'injection automatique : les ViewModel reçoivent les dépendances sans avoir à les créer.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // Crée la base de données Room (une seule instance pour toute l'application)
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "scrud-db")
            .fallbackToDestructiveMigration()
            .build()

    // Fournit les DAO (injectés depuis la base de données)
    @Provides fun provideStudentDao(db: AppDatabase): StudentDao = db.studentDao()
    @Provides fun provideCourseDao(db: AppDatabase): CourseDao = db.courseDao()
    @Provides fun provideSubscribeDao(db: AppDatabase): SubscribeDao = db.subscribeDao()
    @Provides fun provideUserDao(db: AppDatabase): UserDao = db.userDao()
    @Provides fun provideTeacherDao(db: AppDatabase): TeacherDao = db.teacherDao()
    @Provides fun provideGradeDao(db: AppDatabase): GradeDao = db.gradeDao()

    // Crée le repository principal (une seule instance partagée)
    @Provides
    @Singleton
    fun provideRepository(
        studentDao: StudentDao, 
        courseDao: CourseDao,
        subscribeDao: SubscribeDao,
        teacherDao: TeacherDao,
        gradeDao: GradeDao
    ): SCRUDRepository =
        SCRUDRepository(studentDao, courseDao, subscribeDao, teacherDao, gradeDao)
    
    // Crée le repository d'authentification (une seule instance partagée)
    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        studentDao: StudentDao,
        teacherDao: TeacherDao
    ): AuthRepository =
        AuthRepository(userDao, studentDao, teacherDao)
}
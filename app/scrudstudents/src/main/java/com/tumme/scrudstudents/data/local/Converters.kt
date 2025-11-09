package com.tumme.scrudstudents.data.local

import androidx.room.TypeConverter
import com.tumme.scrudstudents.data.local.model.Gender
import com.tumme.scrudstudents.data.local.model.LevelCourse
import com.tumme.scrudstudents.data.local.model.UserRole
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromGender(value: String?): Gender? = value?.let { Gender.from(it) }

    @TypeConverter
    fun genderToString(gender: Gender?): String? = gender?.value

    @TypeConverter
    fun fromLevel(value: String?): LevelCourse? = value?.let { LevelCourse.from(it) }

    @TypeConverter
    fun levelToString(level: LevelCourse?): String? = level?.value
    
    @TypeConverter
    fun fromUserRole(value: String?): UserRole? = value?.let { UserRole.from(it) }
    
    @TypeConverter
    fun userRoleToString(role: UserRole?): String? = role?.value
}
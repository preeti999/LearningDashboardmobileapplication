package com.example.learningdashboardmobileapplication.domain.repository

import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity

/**
 * Domain contract used by use cases. The data layer implements this interface.
 */
interface CourseRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun loadCourses(): List<CourseEntity>
    suspend fun getCourse(courseId: Int): CourseEntity?
    suspend fun toggleLesson(courseId: Int, lessonId: Int): CourseEntity?
}


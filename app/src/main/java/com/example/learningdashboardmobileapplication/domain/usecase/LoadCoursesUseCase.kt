package com.example.learningdashboardmobileapplication.domain.usecase

import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository

/**
 * Loads the dashboard data through one small domain entry point.
 */
class LoadCoursesUseCase(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(): List<CourseEntity> {
        return repository.loadCourses()
    }
}


package com.example.learningdashboardmobileapplication.domain.usecase

import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository

/**
 * Fetches one course for the detail screen.
 */
class GetCourseUseCase(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: Int): CourseEntity? {
        return repository.getCourse(courseId)
    }
}


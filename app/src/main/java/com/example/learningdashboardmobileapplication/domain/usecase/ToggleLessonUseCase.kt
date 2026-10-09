package com.example.learningdashboardmobileapplication.domain.usecase

import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository

/**
 * Updates lesson completion and returns the refreshed course state.
 */
class ToggleLessonUseCase(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(courseId: Int, lessonId: Int): CourseEntity? {
        return repository.toggleLesson(courseId, lessonId)
    }
}


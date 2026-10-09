package com.example.learningdashboardmobileapplication.di

import android.content.Context
import com.example.learningdashboardmobileapplication.data.FileCourseRepository
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository
import com.example.learningdashboardmobileapplication.domain.usecase.GetCourseUseCase
import com.example.learningdashboardmobileapplication.domain.usecase.LoadCoursesUseCase
import com.example.learningdashboardmobileapplication.domain.usecase.LoginUseCase
import com.example.learningdashboardmobileapplication.domain.usecase.ToggleLessonUseCase

/**
 * Simple composition root.
 * This keeps object creation in one place without introducing a DI framework.
 */
class AppContainer private constructor(context: Context) {

    private val repository: CourseRepository = FileCourseRepository(context.applicationContext)

    val loginUseCase = LoginUseCase(repository)
    val loadCoursesUseCase = LoadCoursesUseCase(repository)
    val getCourseUseCase = GetCourseUseCase(repository)
    val toggleLessonUseCase = ToggleLessonUseCase(repository)

    companion object {
        @Volatile
        private var instance: AppContainer? = null

        fun get(context: Context): AppContainer {
            return instance ?: synchronized(this) {
                instance ?: AppContainer(context.applicationContext).also { instance = it }
            }
        }
    }
}


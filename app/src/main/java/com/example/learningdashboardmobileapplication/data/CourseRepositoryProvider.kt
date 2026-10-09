package com.example.learningdashboardmobileapplication.data

import android.content.Context
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository

object CourseRepositoryProvider {
    @Volatile
    private var repository: CourseRepository? = null

    fun get(context: Context): CourseRepository {
        return repository ?: synchronized(this) {
            repository ?: FileCourseRepository(context.applicationContext).also { repository = it }
        }
    }
}


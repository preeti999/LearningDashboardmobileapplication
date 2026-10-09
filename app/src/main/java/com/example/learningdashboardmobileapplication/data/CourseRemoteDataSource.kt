package com.example.learningdashboardmobileapplication.data

import android.content.Context
import kotlinx.coroutines.delay

class CourseRemoteDataSource(
    private val context: Context
) {
    suspend fun fetchCourses(): List<Course> {
        delay(500)
        val json = context.assets.open("courses.json").bufferedReader().use { it.readText() }
        return CourseJsonCodec.parseCourses(json)
    }
}


package com.example.learningdashboardmobileapplication.data

import java.io.File

/**
 * Keeps the offline path separate from the remote API path.
 * The app reads from this file first, so previously loaded data still works without internet.
 */
class CourseLocalDataSource(
    private val cacheFile: File
) {
    fun loadCachedCourses(): List<Course>? {
        if (!cacheFile.exists()) return null
        val text = cacheFile.takeIf { it.length() > 0 }?.readText().orEmpty()
        if (text.isBlank()) return null
        return runCatching { CourseJsonCodec.parseCourses(text) }.getOrNull()
    }

    fun saveCourses(courses: List<Course>) {
        cacheFile.writeText(CourseJsonCodec.toJson(courses))
    }
}


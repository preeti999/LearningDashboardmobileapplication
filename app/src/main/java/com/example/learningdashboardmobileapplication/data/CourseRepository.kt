package com.example.learningdashboardmobileapplication.data

import android.content.Context
import com.example.learningdashboardmobileapplication.data.mapper.toDomain
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.time.Duration.Companion.milliseconds


class FileCourseRepository(
    context: Context,
    private val remoteDataSource: CourseRemoteDataSource = CourseRemoteDataSource(context.applicationContext),
    private val localDataSource: CourseLocalDataSource = CourseLocalDataSource(
        File(context.applicationContext.filesDir, CACHE_FILE_NAME)
    )
 ) : com.example.learningdashboardmobileapplication.domain.repository.CourseRepository {

    private val mutex = Mutex()

    override suspend fun login(email: String, password: String): Boolean = withContext(Dispatchers.IO) {
        // Mock login only checks simple format rules for this assignment.
        kotlinx.coroutines.delay(400.milliseconds)
        email.contains("@") && password.length >= 4
    }

    override suspend fun loadCourses(): List<CourseEntity> = mutex.withLock {
        withContext(Dispatchers.IO) {
            // Offline first: return cached data when it already exists.
            localDataSource.loadCachedCourses()?.takeIf { it.isNotEmpty() }?.toDomain() ?: run {
                // If nothing is cached, read the bundled JSON once and store it locally.
                val remoteCourses = remoteDataSource.fetchCourses()
                localDataSource.saveCourses(remoteCourses)
                remoteCourses.toDomain()
            }
        }
    }

    override suspend fun getCourse(courseId: Int): CourseEntity? {
        return loadCourses().firstOrNull { it.id == courseId }
    }

    override suspend fun toggleLesson(courseId: Int, lessonId: Int): CourseEntity? = mutex.withLock {
        withContext(Dispatchers.IO) {
            // Update the saved course progress locally so the app works after going offline.
            val courses = localDataSource.loadCachedCourses() ?: remoteDataSource.fetchCourses()
            val updatedCourses = courses.map { course ->
                if (course.id != courseId) {
                    course
                } else {
                    val completedLessons = ((course.progress / 100.0) * course.lessons).toInt()
                    val nextCompletedLessons = if (lessonId <= completedLessons) {
                        (completedLessons - 1).coerceAtLeast(0)
                    } else {
                        (completedLessons + 1).coerceAtMost(course.lessons)
                    }
                    val updatedProgress = ((nextCompletedLessons.toDouble() / course.lessons) * 100).toInt()
                    course.copy(progress = updatedProgress)
                }
            }
            val updatedCourse = updatedCourses.firstOrNull { it.id == courseId }
            if (updatedCourse != null) {
                localDataSource.saveCourses(updatedCourses)
            }
            updatedCourse?.toDomain()
        }
    }


    companion object {
        private const val CACHE_FILE_NAME = "courses_cache.json"
    }
}


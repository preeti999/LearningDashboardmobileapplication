package com.example.learningdashboardmobileapplication.data.mapper

import com.example.learningdashboardmobileapplication.data.Course
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity

/**
 * Converts the storage model into the domain model.
 * Keeping this in a separate file keeps the data layer easy to explain.
 */
fun Course.toDomain(): CourseEntity {
	return CourseEntity(
		id = id,
		title = title,
		instructor = instructor,
		progress = progress,
		lessons = lessons
	)
}

fun List<Course>.toDomain(): List<CourseEntity> = map { it.toDomain() }


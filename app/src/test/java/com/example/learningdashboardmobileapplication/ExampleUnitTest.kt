package com.example.learningdashboardmobileapplication

import com.example.learningdashboardmobileapplication.data.CourseProgressCalculator
import com.example.learningdashboardmobileapplication.data.Lesson
import org.junit.Test
import org.junit.Assert.assertEquals

class ExampleUnitTest {
    @Test
    fun calculates_progress_from_completed_lessons() {
        val lessons = listOf(
            Lesson(id = 1, title = "Introduction", isCompleted = true),
            Lesson(id = 2, title = "Variables", isCompleted = true),
            Lesson(id = 3, title = "Functions", isCompleted = false),
            Lesson(id = 4, title = "OOP", isCompleted = false)
        )

        assertEquals(50, CourseProgressCalculator.calculateProgress(lessons))
        assertEquals(0, CourseProgressCalculator.calculateProgress(emptyList()))
    }
}
package com.example.learningdashboardmobileapplication.data

data class Lesson(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)

data class Course(
    val id: Int,
    val title: String,
    val instructor: String,
    val progress: Int,
    val lessons: Int
) {
    val lessonCount: Int
        get() = lessons

    val lessonItems: List<Lesson>
        get() {
            val completedLessons = ((progress / 100.0) * lessons).toInt()
            return (1..lessons).map { index ->
                Lesson(
                    id = index,
                    title = "Lesson $index",
                    isCompleted = index <= completedLessons
                )
            }
        }
}

object CourseProgressCalculator {
    fun calculateProgress(lessons: List<Lesson>): Int {
        if (lessons.isEmpty()) return 0
        val completedCount = lessons.count { it.isCompleted }
        return ((completedCount.toDouble() / lessons.size.toDouble()) * 100).toInt()
    }
}


package com.example.learningdashboardmobileapplication.domain.entity

/**
 * Domain course model.
 *
 * The UI works with this type, while the data layer keeps its own storage model.
 */
data class CourseEntity(
    val id: Int,
    val title: String,
    val instructor: String,
    val progress: Int,
    val lessons: Int
) {
    val lessonCount: Int
        get() = lessons

    val lessonItems: List<LessonEntity>
        get() {
            val completedLessons = ((progress / 100.0) * lessons).toInt()
            return (1..lessons).map { index ->
                LessonEntity(
                    id = index,
                    title = "Lesson $index",
                    isCompleted = index <= completedLessons
                )
            }
        }
}


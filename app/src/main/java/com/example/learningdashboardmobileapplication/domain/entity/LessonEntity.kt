package com.example.learningdashboardmobileapplication.domain.entity

/**
 * Domain lesson model used by the UI. Keeping it separate from the data model makes the
 * architecture easier to explain in interviews.
 */
data class LessonEntity(
    val id: Int,
    val title: String,
    val isCompleted: Boolean
)


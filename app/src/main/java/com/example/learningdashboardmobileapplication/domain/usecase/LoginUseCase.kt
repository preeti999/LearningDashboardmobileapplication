package com.example.learningdashboardmobileapplication.domain.usecase

import com.example.learningdashboardmobileapplication.domain.repository.CourseRepository

/**
 * Keeps login logic simple and isolated from the ViewModel.
 */
class LoginUseCase(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(email: String, password: String): Boolean {
        return repository.login(email, password)
    }
}


package com.example.learningdashboardmobileapplication.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.usecase.LoadCoursesUseCase
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val loadCoursesUseCase: LoadCoursesUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<DashboardUiState>()
    val uiState: LiveData<DashboardUiState>
        get() = _uiState

    init {
        _uiState.value = DashboardUiState.Loading
    }

    fun loadCourses() {
        viewModelScope.launch {
            _uiState.value = DashboardUiState.Loading
            try {
                val courses = loadCoursesUseCase()
                _uiState.value = if (courses.isEmpty()) {
                    DashboardUiState.Empty
                } else {
                    DashboardUiState.Success(courses)
                }
            } catch (throwable: Throwable) {
                _uiState.value = DashboardUiState.Error(
                    throwable.message ?: "Failed to load courses."
                )
            }
        }
    }

    sealed class DashboardUiState {
        data object Loading : DashboardUiState()
        data object Empty : DashboardUiState()
        data class Success(val courses: List<CourseEntity>) : DashboardUiState()
        data class Error(val message: String) : DashboardUiState()
    }

    class Factory(
        private val loadCoursesUseCase: LoadCoursesUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
                return DashboardViewModel(loadCoursesUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}


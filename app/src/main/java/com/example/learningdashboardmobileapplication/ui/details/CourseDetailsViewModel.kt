package com.example.learningdashboardmobileapplication.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.domain.usecase.GetCourseUseCase
import com.example.learningdashboardmobileapplication.domain.usecase.ToggleLessonUseCase
import kotlinx.coroutines.launch

class CourseDetailsViewModel(
    private val getCourseUseCase: GetCourseUseCase,
    private val toggleLessonUseCase: ToggleLessonUseCase
) : ViewModel() {

    private val _uiState = MutableLiveData<CourseDetailsUiState>()
    val uiState: LiveData<CourseDetailsUiState>
        get() = _uiState

    init {
        _uiState.value = CourseDetailsUiState.Loading
    }

    private var currentCourseId: Int = -1

    fun loadCourse(courseId: Int) {
        currentCourseId = courseId
        viewModelScope.launch {
            _uiState.value = CourseDetailsUiState.Loading
            try {
                val course = getCourseUseCase(courseId)
                _uiState.value = if (course == null) {
                    CourseDetailsUiState.Error("Course not found.")
                } else {
                    CourseDetailsUiState.Success(course)
                }
            } catch (throwable: Throwable) {
                _uiState.value = CourseDetailsUiState.Error(
                    throwable.message ?: "Failed to load course details."
                )
            }
        }
    }

    fun toggleLesson(lessonId: Int) {
        if (currentCourseId == -1) return
        viewModelScope.launch {
            try {
                val updatedCourse = toggleLessonUseCase(currentCourseId, lessonId)
                if (updatedCourse != null) {
                    _uiState.value = CourseDetailsUiState.Success(updatedCourse)
                } else {
                    _uiState.value = CourseDetailsUiState.Error("Unable to update the lesson.")
                }
            } catch (throwable: Throwable) {
                _uiState.value = CourseDetailsUiState.Error(
                    throwable.message ?: "Unable to update the lesson."
                )
            }
        }
    }

    sealed class CourseDetailsUiState {
        data object Loading : CourseDetailsUiState()
        data class Success(val course: CourseEntity) : CourseDetailsUiState()
        data class Error(val message: String) : CourseDetailsUiState()
    }

    class Factory(
        private val getCourseUseCase: GetCourseUseCase,
        private val toggleLessonUseCase: ToggleLessonUseCase
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CourseDetailsViewModel::class.java)) {
                return CourseDetailsViewModel(getCourseUseCase, toggleLessonUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}


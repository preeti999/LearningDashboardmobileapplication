package com.example.learningdashboardmobileapplication.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.learningdashboardmobileapplication.R
import com.example.learningdashboardmobileapplication.di.AppContainer
import com.example.learningdashboardmobileapplication.databinding.FragmentCourseDetailsBinding
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.ui.common.dp
import com.example.learningdashboardmobileapplication.ui.common.showIf
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class CourseDetailsFragment : Fragment() {

    private var _binding: FragmentCourseDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        val container = AppContainer.get(requireContext())
        ViewModelProvider(
            this,
            CourseDetailsViewModel.Factory(container.getCourseUseCase, container.toggleLessonUseCase)
        )[CourseDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCourseDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val courseId = requireArguments().getInt(ARG_COURSE_ID)

        binding.retryButton.setOnClickListener { viewModel.loadCourse(courseId) }
        viewModel.uiState.observe(viewLifecycleOwner) { renderState(it) }
        viewModel.loadCourse(courseId)
    }

    private fun renderState(state: CourseDetailsViewModel.CourseDetailsUiState) {
        binding.loadingProgressBar.showIf(state is CourseDetailsViewModel.CourseDetailsUiState.Loading)
        binding.errorTextView.showIf(state is CourseDetailsViewModel.CourseDetailsUiState.Error)
        binding.retryButton.showIf(state is CourseDetailsViewModel.CourseDetailsUiState.Error)

        when (state) {
            is CourseDetailsViewModel.CourseDetailsUiState.Success -> renderCourse(state.course)
            is CourseDetailsViewModel.CourseDetailsUiState.Error -> binding.errorTextView.text = state.message
            else -> Unit
        }
    }

    private fun renderCourse(course: CourseEntity) {
        // The header/progress stay fixed; only the lesson list updates and scrolls below.
        binding.courseTitleTextView.text = course.title
        binding.courseProgressTextView.text = getString(R.string.course_progress_format, course.progress)
        binding.courseProgressBar.progress = course.progress
        binding.lessonsContainer.removeAllViews()

        course.lessonItems.forEach { lesson ->
            binding.lessonsContainer.addView(createLessonRow(lesson.title, lesson.id, lesson.isCompleted))
        }
    }

    private fun createLessonRow(title: String, lessonId: Int, isCompleted: Boolean): View {
        val cardView = MaterialCardView(requireContext()).apply {
            radius = requireContext().dp(12).toFloat()
            cardElevation = requireContext().dp(1).toFloat()
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = requireContext().dp(10)
            }
        }

        val row = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(
                requireContext().dp(16),
                requireContext().dp(16),
                requireContext().dp(16),
                requireContext().dp(16)
            )
        }

        val statusText = if (isCompleted) "✓ Completed" else "○ Pending"

        row.addView(LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            addView(TextView(requireContext()).apply {
                text = title
                textSize = 16f
            })
            addView(TextView(requireContext()).apply {
                text = statusText
                layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                    topMargin = requireContext().dp(4)
                }
            })
        })

        row.addView(MaterialButton(requireContext()).apply {
            text = if (isCompleted) "Completed" else "Complete"
            isEnabled = !isCompleted
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setOnClickListener { viewModel.toggleLesson(lessonId) }
        })

        cardView.addView(row)
        return cardView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_COURSE_ID = "course_id"
    }
}



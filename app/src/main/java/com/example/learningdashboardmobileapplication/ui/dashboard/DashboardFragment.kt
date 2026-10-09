package com.example.learningdashboardmobileapplication.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.learningdashboardmobileapplication.R
import com.example.learningdashboardmobileapplication.di.AppContainer
import com.example.learningdashboardmobileapplication.domain.entity.CourseEntity
import com.example.learningdashboardmobileapplication.databinding.FragmentDashboardBinding
import com.example.learningdashboardmobileapplication.ui.common.dp
import com.example.learningdashboardmobileapplication.ui.common.showIf
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        val container = AppContainer.get(requireContext())
        ViewModelProvider(this, DashboardViewModel.Factory(container.loadCoursesUseCase))[DashboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.retryButton.setOnClickListener { viewModel.loadCourses() }
        viewModel.uiState.observe(viewLifecycleOwner) { renderState(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCourses()
    }

    private fun renderState(state: DashboardViewModel.DashboardUiState) {
        binding.loadingProgressBar.showIf(state is DashboardViewModel.DashboardUiState.Loading)
        binding.errorTextView.showIf(state is DashboardViewModel.DashboardUiState.Error)
        binding.retryButton.showIf(state is DashboardViewModel.DashboardUiState.Error)
        binding.emptyStateTextView.showIf(state is DashboardViewModel.DashboardUiState.Empty)
        binding.coursesContainer.showIf(state is DashboardViewModel.DashboardUiState.Success)

        when (state) {
            is DashboardViewModel.DashboardUiState.Success -> renderCourses(state.courses)
            is DashboardViewModel.DashboardUiState.Error -> binding.errorTextView.text = state.message
            else -> Unit
        }
    }

    private fun renderCourses(courses: List<CourseEntity>) {
        // Keep the existing dashboard card design intact; only the data source is changing.
        binding.coursesContainer.removeAllViews()
        if (courses.isEmpty()) return

        courses.forEach { course ->
            binding.coursesContainer.addView(createCourseCard(course))
        }
    }

    private fun createCourseCard(course: CourseEntity): View {
        val cardView = MaterialCardView(requireContext()).apply {
            radius = requireContext().dp(16).toFloat()
            cardElevation = requireContext().dp(2).toFloat()
            isClickable = true
            isFocusable = true
            setOnClickListener { openCourseDetails(course.id) }
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = requireContext().dp(12)
            }
        }

        val content = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(
                requireContext().dp(16),
                requireContext().dp(16),
                requireContext().dp(16),
                requireContext().dp(16)
            )
        }

        content.addView(TextView(requireContext()).apply {
            text = course.title
            textSize = 18f
        })

        content.addView(TextView(requireContext()).apply {
            text = getString(R.string.dashboard_instructor_format, course.instructor)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = requireContext().dp(6)
            }
        })

        content.addView(TextView(requireContext()).apply {
            text = getString(R.string.dashboard_progress_format, course.progress)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = requireContext().dp(6)
            }
        })

        content.addView(TextView(requireContext()).apply {
            text = getString(R.string.dashboard_lessons_format, course.lessonCount)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = requireContext().dp(6)
            }
        })

        content.addView(MaterialButton(requireContext()).apply {
            text = getString(R.string.dashboard_continue)
            layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply {
                topMargin = requireContext().dp(12)
            }
            setOnClickListener { openCourseDetails(course.id) }
        })

        cardView.addView(content)
        return cardView
    }

    private fun openCourseDetails(courseId: Int) {
        findNavController().navigate(
            R.id.action_dashboardFragment_to_courseDetailsFragment,
            Bundle().apply { putInt("course_id", courseId) }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





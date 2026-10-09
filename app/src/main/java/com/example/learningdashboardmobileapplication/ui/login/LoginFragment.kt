package com.example.learningdashboardmobileapplication.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.learningdashboardmobileapplication.R
import com.example.learningdashboardmobileapplication.di.AppContainer
import com.example.learningdashboardmobileapplication.databinding.FragmentLoginBinding
import com.example.learningdashboardmobileapplication.ui.common.showIf

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy {
        val container = AppContainer.get(requireContext())
        ViewModelProvider(this, LoginViewModel.Factory(container.loginUseCase))[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.emailEditText.setText(getString(R.string.default_login_email))
        binding.passwordEditText.setText(getString(R.string.default_login_password))

        binding.loginButton.setOnClickListener {
            binding.errorTextView.showIf(false)
            viewModel.login(
                binding.emailEditText.text?.toString().orEmpty(),
                binding.passwordEditText.text?.toString().orEmpty()
            )
        }

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            binding.loadingProgressBar.showIf(state is LoginViewModel.LoginUiState.Loading)
            binding.loginButton.isEnabled = state !is LoginViewModel.LoginUiState.Loading

            when (state) {
                is LoginViewModel.LoginUiState.Error -> {
                    binding.errorTextView.text = state.message
                    binding.errorTextView.showIf(true)
                }

                is LoginViewModel.LoginUiState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
                }

                else -> binding.errorTextView.showIf(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


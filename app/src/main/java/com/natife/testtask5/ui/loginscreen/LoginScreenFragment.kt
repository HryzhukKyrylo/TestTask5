package com.natife.testtask5.ui.loginscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentLoginScreenBinding
import com.natife.testtask5.ui.base.BaseFragment
import com.natife.testtask5.ui.loginscreen.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenFragment : BaseFragment<FragmentLoginScreenBinding>() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSettings()
        initListeners()
    }

    private fun initSettings() {
        loginViewModel.initSettings()
    }

    private fun initListeners() {

        loginViewModel.observeRememberNickname.observe(viewLifecycleOwner) { remember ->
            if (remember) {
                binding.rememberPasswordSwitch.isChecked = true
                enabledButton(true)
            }

        }

        loginViewModel.observeNickname.observe(viewLifecycleOwner) { name ->
            if (!name.isNullOrEmpty()) {
                binding.nicknameEditText.setText(name)
            }
        }

        loginViewModel.observeNavigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate != null && navigate) {
                binding.loginProgressBar.visibility = View.GONE
                loginViewModel.saveNickname(
                    binding.nicknameEditText.text.toString(),
                    binding.rememberPasswordSwitch.isChecked
                )

                with(findNavController()) {
                    popBackStack(R.id.loginScreenFragment, true)
                    navigate(R.id.listUsersScreenFragment)
                }
            } else {
                binding.loginProgressBar.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.connect_to_server),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.nicknameEditText.addTextChangedListener(afterTextChanged = {
            if (!it.isNullOrEmpty()) {
                enabledButton(true)
            } else {
                enabledButton(false)
            }
        })

        binding.loginButton.setOnClickListener {
            loginViewModel.connect(binding.nicknameEditText.text.toString())
        }
    }

    private fun enabledButton(enabled: Boolean) {
        binding.loginButton.isEnabled = (enabled)
    }
}

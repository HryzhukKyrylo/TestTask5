package com.natife.testtask5.ui.loginscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentLoginScreenBinding
import com.natife.testtask5.ui.loginscreen.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {
    private var binding: FragmentLoginScreenBinding? = null
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSettings()
        initListeners()
    }

    private fun initSettings() {
        viewModel.initSettings()
    }

    private fun initListeners() {

        viewModel.rememberNickname.observe(viewLifecycleOwner) { remember ->
            if (remember) {
                binding?.rememberPassword?.isChecked = true
                enabledButton(true)
            }

        }

        viewModel.savedNickname.observe(viewLifecycleOwner) { name ->
            if (!name.isNullOrEmpty()) {
                binding?.nickNameEditText?.setText(name)
            }
        }

        viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
            if (navigate) {
                binding?.progressBar?.visibility = View.GONE
                viewModel.forget()
                viewModel.saveNickname(
                    binding?.nickNameEditText?.text.toString(),
                    binding?.rememberPassword?.isChecked ?: false
                )
                findNavController().navigate(

                    R.id.action_loginScreenFragment_to_listUsersScreenFragment
                )
            } else {
                binding?.progressBar?.visibility = View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    resources.getString(R.string.connect_to_server),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding?.nickNameEditText?.addTextChangedListener(afterTextChanged = {
            if (!it.isNullOrEmpty()) {
                enabledButton(true)
            } else {
                enabledButton(false)
            }
        })

        binding?.loginButton?.setOnClickListener {
            viewModel.connect(binding?.nickNameEditText?.text.toString())
        }
    }

    private fun enabledButton(enabled: Boolean) {
        binding?.loginButton?.isEnabled = (enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

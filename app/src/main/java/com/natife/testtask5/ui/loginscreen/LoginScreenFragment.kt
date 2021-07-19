package com.natife.testtask5.ui.loginscreen

import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentLoginScreenBinding
import com.natife.testtask5.ui.loginscreen.viewmodel.LoginViewModel
import com.natife.testtask5.util.PreferenceHelper.checkLogin
import com.natife.testtask5.util.PreferenceHelper.customPreference
import com.natife.testtask5.util.PreferenceHelper.savedNickname
import dagger.hilt.android.AndroidEntryPoint

private const val CUSTOM_PREF_NAME = "my_preferences"

@AndroidEntryPoint
class LoginScreenFragment : Fragment() {
    private var binding: FragmentLoginScreenBinding? = null
    private val viewModel: LoginViewModel by viewModels()
    private var preferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPreference()
        initSettings()
        initListeners()
    }

    private fun initSettings() {
        if(preferences?.checkLogin == true){
            binding?.rememberPassword?.isChecked = true
            binding?.nickNameTextView?.setText(preferences?.savedNickname?:"")
            enabledButton(true)
        }
    }

    private fun initPreference() {
        preferences = customPreference(requireContext(), CUSTOM_PREF_NAME)
    }

    private fun initListeners() {
        binding?.nickNameTextView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable) {
                if (p0.isNotEmpty()) {
                    enabledButton(true)
                } else {
                    enabledButton(false)
                }
            }
        })

        binding?.loginButton?.setOnClickListener {
            val nickname = binding?.nickNameTextView?.text.toString()

            viewModel.navigate.observe(viewLifecycleOwner) { navigate ->
                if (navigate) {
                    binding?.progressBar?.visibility = View.GONE
                    viewModel.forget()
                    saveNickname(nickname)
                    findNavController().navigate(
                        R.id.action_loginScreenFragment_to_listUsersScreenFragment,
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
            viewModel.connect(nickname)
        }
    }

    private fun saveNickname(nickname : String) {
        if (binding?.rememberPassword?.isChecked == true) {
            preferences?.savedNickname = nickname
            preferences?.checkLogin = true
        }else{
            preferences?.savedNickname = ""
            preferences?.checkLogin = false
        }
    }

    private fun enabledButton(enabled: Boolean) {
        binding?.loginButton?.isEnabled = (enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        preferences = null
    }
}

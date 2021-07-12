package com.natife.testtask5.ui.loginscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentLoginScreenBinding

class LoginScreenFragment : Fragment() {
    private var binding: FragmentLoginScreenBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }


    private fun initListeners() {
        binding?.email?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enabledButton()
            }
        })

        binding?.password?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                enabledButton()
            }
        })

        binding?.loginButton?.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreenFragment_to_listUsersScreenFragment)
        }
    }

    private fun enabledButton() {
        val mUsername: String = binding?.email?.text.toString().trim()
        val mPassword: String = binding?.password?.text.toString().trim()
        binding?.loginButton?.isEnabled = (mUsername.isNotEmpty() && mPassword.isNotBlank())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}

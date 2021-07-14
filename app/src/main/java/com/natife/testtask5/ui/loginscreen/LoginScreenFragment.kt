package com.natife.testtask5.ui.loginscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
        initListeners()
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

            viewModel.navigate.observe(viewLifecycleOwner){ navigate ->
                if(navigate){
                    binding?.progressBar?.visibility = View.GONE
                    viewModel.forget()
                    val id = viewModel.id.value
                    val bundle = bundleOf(NICK_ARG to nickname, ID_ARG to id)

                    findNavController().navigate(
                        R.id.action_loginScreenFragment_to_listUsersScreenFragment,
                        bundle
                    )
                } else {
                    binding?.progressBar?.visibility = View.VISIBLE
                }
            }
            viewModel.connect(nickname)
        }
    }

    private fun enabledButton(enabled: Boolean) {
        binding?.loginButton?.isEnabled = (enabled)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val NICK_ARG = "nickname"
        const val ID_ARG = "id"
    }
}

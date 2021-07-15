package com.natife.testtask5.ui.listusersscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.natife.testtask5.databinding.FragmentListUsersScreenBinding
import com.natife.testtask5.ui.listusersscreen.adapter.CustomRecyclerAdapter
import com.natife.testtask5.ui.listusersscreen.viewmodel.UserViewModel
import com.natife.testtask5.ui.loginscreen.LoginScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersScreenFragment : Fragment() {

    private var binding: FragmentListUsersScreenBinding? = null
    private val viewModel: UserViewModel by viewModels()
    private val adapter by lazy { CustomRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListUsersScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initListener()
    }

    private fun initListener() {
        viewModel.users.observe(viewLifecycleOwner){

            if(!it.isNullOrEmpty()) adapter.updateLiseUsers(it)
        }
        viewModel.fetchUsers()

    }

    private fun initAdapter() {
        binding?.recyclerView?.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}

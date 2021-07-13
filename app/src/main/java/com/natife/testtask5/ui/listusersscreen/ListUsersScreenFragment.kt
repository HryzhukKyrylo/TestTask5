package com.natife.testtask5.ui.listusersscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.natife.testtask5.databinding.FragmentListUsersScreenBinding
import com.natife.testtask5.ui.listusersscreen.adapter.CustomRecyclerAdapter
import com.natife.testtask5.ui.listusersscreen.viewmodel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersScreenFragment : Fragment() {

    private var binding : FragmentListUsersScreenBinding? = null
    private val adapter  by lazy { CustomRecyclerAdapter() }
    private val viewModel: ListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View ?{
        binding =  FragmentListUsersScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initUsers()
    }

    private fun initUsers() {
        viewModel.ip.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
        viewModel.send()
    }

    private fun initRecycler() {
        binding?.recyclerView?.adapter = adapter
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}

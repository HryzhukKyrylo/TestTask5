package com.natife.testtask5.ui.listusersscreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentListUsersScreenBinding
import com.natife.testtask5.ui.listusersscreen.adapter.CustomRecyclerAdapter
import com.natife.testtask5.ui.listusersscreen.viewmodel.UserViewModel
import com.natife.testtask5.ui.loginscreen.LoginScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersScreenFragment : Fragment() {

    private var binding: FragmentListUsersScreenBinding? = null
    private val viewModel: UserViewModel by viewModels()
    private lateinit var adapter: CustomRecyclerAdapter

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
        binding?.usersProgressBar?.visibility = View.VISIBLE
        Toast.makeText(requireContext(), "Load Users", Toast.LENGTH_SHORT).show()

        viewModel.fetchUsers()

        viewModel.users.observe(viewLifecycleOwner) {


            if (!it.isNullOrEmpty()) {
                binding?.noUsersTextView?.visibility = View.GONE
                binding?.usersProgressBar?.visibility = View.GONE
                binding?.recyclerView?.visibility = View.VISIBLE
                adapter.updateLiseUsers(it)
            } else {
                binding?.noUsersTextView?.visibility = View.VISIBLE
                binding?.usersProgressBar?.visibility = View.GONE
                binding?.recyclerView?.visibility = View.GONE
            }
        }
    }

    private fun initAdapter() {
        adapter = CustomRecyclerAdapter { user ->
            var bundle = bundleOf(USER_ARG to user)
            findNavController().navigate(
                R.id.action_listUsersScreenFragment_to_chatScreenFragment,
                bundle
            )
        }
        binding?.recyclerView?.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object{
        const val USER_ARG = "user_id_465738299763"
    }

}

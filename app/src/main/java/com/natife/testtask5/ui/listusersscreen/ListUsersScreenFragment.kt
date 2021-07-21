package com.natife.testtask5.ui.listusersscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentListUsersScreenBinding
import com.natife.testtask5.ui.listusersscreen.adapter.ListUsersAdapter
import com.natife.testtask5.ui.listusersscreen.viewmodel.ListUsersViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersScreenFragment : Fragment() {

    private var binding: FragmentListUsersScreenBinding? = null
    private val viewModel: ListUsersViewModel by viewModels()
    private var adapter: ListUsersAdapter? = null

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
        viewModel.fetchUsers()
        binding?.usersProgressBar?.visibility = View.VISIBLE

        viewModel.users.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                binding?.apply {
                    noUsersTextView.visibility = View.GONE
                    usersProgressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }

                adapter?.submitList(it)
//                adapter?.updateLiseUsers(it)
            } else {
                binding?.apply {
                    noUsersTextView.visibility = View.VISIBLE
                    usersProgressBar.visibility = View.GONE
                    recyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun initAdapter() {
        adapter = ListUsersAdapter { user ->
            val bundle = bundleOf(USER_ARG to user)
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

    companion object {
        const val USER_ARG = "user_id_465738299763"
    }
}

package com.natife.testtask5.ui.listusersscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentListUsersScreenBinding
import com.natife.testtask5.ui.listusersscreen.adapter.ListUsersAdapter
import com.natife.testtask5.ui.listusersscreen.viewmodel.ListUsersViewModel
import com.natife.testtask5.util.showSnack
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ListUsersScreenFragment : Fragment() {

    private var binding: FragmentListUsersScreenBinding? = null
    private val usersViewModel: ListUsersViewModel by viewModels()
    private val usersAdapter: ListUsersAdapter by lazy {
        ListUsersAdapter { user ->
            val bundle = bundleOf(USER_ARG to user)
            findNavController().navigate(
                R.id.action_listUsersScreenFragment_to_chatScreenFragment,
                bundle
            )
        }
    }
    private var timeBackPressed = 0L


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListUsersScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListenerBackPressed()
        initAdapter()
        initListener()
    }

    private fun initListener() {
        usersViewModel.startRequestingUsers()
        usersViewModel.observeConnection.observe(viewLifecycleOwner) { connection ->
            if (!connection) {
                binding?.root?.showSnack(
                    resources.getString(R.string.disconnect),
                    resources.getString(R.string.retry)
                ) {
                    usersViewModel.reconnectToServer()
                }
            }
        }

        binding?.usersProgressBar?.visibility = View.VISIBLE

        usersViewModel.observeUsers.observe(viewLifecycleOwner) { listUsers ->
            if (!listUsers.isNullOrEmpty()) {
                binding?.apply {
                    noListUsersTextView.visibility = View.GONE
                    usersProgressBar.visibility = View.GONE
                    listUsersRecyclerView.visibility = View.VISIBLE
                }

                usersAdapter.submitList(listUsers)
            } else {
                binding?.apply {
                    noListUsersTextView.visibility = View.VISIBLE
                    usersProgressBar.visibility = View.GONE
                    listUsersRecyclerView.visibility = View.GONE
                }
            }
        }
    }

    private fun initAdapter() {
        binding?.listUsersRecyclerView?.adapter = usersAdapter
    }

    private fun initListenerBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (timeBackPressed + 2000 > System.currentTimeMillis()) {
                        isEnabled = false
                        usersViewModel.disconnectToServer()
                        activity?.onBackPressed()
                    } else
                        Toast.makeText(
                            requireContext(),
                            resources.getString(R.string.back_press),
                            Toast.LENGTH_SHORT
                        ).show()
                    timeBackPressed = System.currentTimeMillis()
                }
            })
    }

    override fun onPause() {
        super.onPause()
        usersViewModel.stopRequestingUsers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        const val USER_ARG = "user_id_465738299763"
    }
}

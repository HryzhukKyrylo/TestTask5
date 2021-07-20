package com.natife.testtask5.ui.chatscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.testtask5.R
import com.natife.testtask5.databinding.FragmentChatScreenBinding
import com.natife.testtask5.ui.chatscreen.adapter.ChatAdapter
import com.natife.testtask5.ui.chatscreen.viewmodel.ChatViewModel
import com.natife.testtask5.ui.listusersscreen.ListUsersScreenFragment
import com.natife.testtask5.util.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import com.natife.testtask5.data.model.User

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var binding: FragmentChatScreenBinding? = null
    private var adapter: ChatAdapter? = null
    private val chatViewModel: ChatViewModel by viewModels()
    private var user: User? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>(ListUsersScreenFragment.USER_ARG)
        if (user == null) {
            findNavController().popBackStack()
            return
        }
        initToolbar()
        initAdapter()
        initListener()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()

    }

    private fun initToolbar() {
        binding?.toolbar?.setNavigationIcon(R.drawable.ic_baseline_back)
        binding?.toolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding?.nameUserView?.text = user?.name
    }

    private fun initAdapter() {
        adapter = ChatAdapter(chatViewModel.getId())
        binding?.chatRecyclerAdapter?.layoutManager = LinearLayoutManager(requireContext())
        binding?.chatRecyclerAdapter?.adapter = adapter
    }

    private fun initListener() {
        binding?.messageText?.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEND){
                user?.id?.let { idUser ->
                    chatViewModel.sendMessage(idUser, binding?.messageText?.text.toString())
                }
                activity?.hideSoftKeyboard()
                binding?.messageText?.setText("")
                binding?.messageText?.clearFocus()
                binding?.messageText?.isCursorVisible = false
            }
             true
        }

        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            if (messages != null) {
                adapter?.add(messages)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter = null
        user = null
        binding = null
    }
}

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
import javax.inject.Inject

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var binding: FragmentChatScreenBinding? = null
    private val adapter: ChatAdapter by lazy {
        ChatAdapter(chatViewModel.getId())
    }

    @Inject
    lateinit var chatProfileFactory: ChatViewModel.AssistedFactory

    private val chatViewModel by viewModels<ChatViewModel> {
        ChatViewModel.provideFactory(
            chatProfileFactory,
            arguments?.getParcelable<User>(ListUsersScreenFragment.USER_ARG)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkUser()
        initToolbar()
        initAdapter()
        initListener()
    }

    private fun checkUser() {
        chatViewModel.user.observe(viewLifecycleOwner) {
            if (it == null) {
                findNavController().popBackStack()
                return@observe
            } else {
                binding?.nameUserView?.text = it.name
            }
        }
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
    }

    private fun initAdapter() {
        binding?.chatRecyclerAdapter?.layoutManager = LinearLayoutManager(requireContext())
        binding?.chatRecyclerAdapter?.adapter = adapter
    }

    private fun initListener() {
        binding?.messageText?.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEND) {
                chatViewModel.sendMessage(binding?.messageText?.text.toString())
                activity?.hideSoftKeyboard()
                binding?.apply {
                    messageText.setText("")
                    messageText.clearFocus()
                    messageText.isCursorVisible = false
                }
            }
            true
        }

        chatViewModel.messages.observe(viewLifecycleOwner) { messages ->
            if (messages != null) {
                adapter.add(messages)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

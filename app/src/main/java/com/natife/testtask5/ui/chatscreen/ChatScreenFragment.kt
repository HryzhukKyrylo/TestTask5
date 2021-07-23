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
import com.natife.testtask5.util.showSnack
import javax.inject.Inject

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var binding: FragmentChatScreenBinding? = null
    private val chatAdapter: ChatAdapter by lazy {
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)?.supportActionBar?.hide()
    }

    private fun checkUser() {
        chatViewModel.observeUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                findNavController().popBackStack()
                return@observe
            } else {
                binding?.userNameTextView?.text = user.name
            }
        }
    }

    private fun initToolbar() {
        binding?.chatToolbar?.setNavigationIcon(R.drawable.ic_baseline_back)
        binding?.chatToolbar?.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initAdapter() {
        binding?.chatRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.chatRecyclerView?.adapter = chatAdapter
    }

    private fun initListener() {
        chatViewModel.observeConnection.observe(viewLifecycleOwner) { connection ->
            if (!connection) {
                binding?.root?.showSnack(
                    resources.getString(R.string.disconnect),
                    resources.getString(R.string.retry)
                ) {
                    chatViewModel.reconnectToServer()
                }
            }
        }

        binding?.messageEditText?.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEND) {
                chatViewModel.sendMessage(binding?.messageEditText?.text.toString())
                activity?.hideSoftKeyboard()
                binding?.apply {
                    messageEditText.setText("")
                    messageEditText.clearFocus()
                    messageEditText.isCursorVisible = false
                }
            }
            true
        }

        chatViewModel.observeMessage.observe(viewLifecycleOwner) { messages ->
            if (messages != null) {
                chatAdapter.add(messages)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)?.supportActionBar?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

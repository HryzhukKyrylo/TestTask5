package com.natife.testtask5.ui.chatscreen


import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.natife.testtask5.databinding.FragmentChatScreenBinding
import com.natife.testtask5.ui.chatscreen.adapter.ChatAdapter
import com.natife.testtask5.ui.chatscreen.viewmodel.ChatViewModel
import com.natife.testtask5.ui.listusersscreen.ListUsersScreenFragment
import com.natife.testtask5.util.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.User

@AndroidEntryPoint
class ChatScreenFragment : Fragment() {
    private var binding: FragmentChatScreenBinding? = null
    private var adapter : ChatAdapter? = null
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

        initAdapter()
        initListener()
    }

    private fun initAdapter() {
        adapter = ChatAdapter()
        binding?.chatRecyclerAdapter?.layoutManager = LinearLayoutManager(requireContext())
        binding?.chatRecyclerAdapter?.adapter = adapter
    }

    private fun initListener() {
        binding?.messageText?.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(p0: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN &&
                    keyCode == KeyEvent.KEYCODE_ENTER
                ) {
                    user?.id?.let { idUser ->
                        chatViewModel.sendMessage(idUser, binding?.messageText?.text.toString())
                    }

                    // hide soft keyboard programmatically
                    activity?.hideSoftKeyboard()

                    // clear focus and hide cursor from edit text
                    binding?.messageText?.setText("")
                    binding?.messageText?.clearFocus()
                    binding?.messageText?.isCursorVisible = false

                    return true
                }
                return false
            }
        })

        chatViewModel.messages.observe(viewLifecycleOwner){ messages ->
            if(messages != null){
                adapter?.updateListRecycler(messages)
            }

        }
//        chatViewModel.messages2.observe(viewLifecycleOwner){ messages ->
//            if(messages != null){
//                Toast.makeText(requireContext(), messages, Toast.LENGTH_SHORT).show()
//            }
//
//        }



    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapter?.clearListRecycler()
        adapter = null
        user = null
        binding = null
    }
}

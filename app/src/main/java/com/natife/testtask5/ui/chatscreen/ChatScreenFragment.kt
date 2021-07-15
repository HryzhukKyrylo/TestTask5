package com.natife.testtask5.ui.chatscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.natife.testtask5.databinding.FragmentChatScreenBinding
import com.natife.testtask5.ui.chatscreen.viewmodel.ChatViewModel

class ChatScreenFragment : Fragment() {
    private var binding: FragmentChatScreenBinding? = null
    private val chatViewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View ?{
        binding = FragmentChatScreenBinding.inflate(inflater, container, false)
        return binding?.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}

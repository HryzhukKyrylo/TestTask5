package com.natife.testtask5.ui.chatscreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natife.testtask5.data.model.MessageDto
import com.natife.testtask5.databinding.MyMessageBinding
import com.natife.testtask5.databinding.UserMessageBinding
import model.Payload
import model.SendMessageDto

class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messageList = mutableListOf<Payload>()

    class UserViewHolder(private val binding: UserMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: MessageDto) {
            binding.userMessageText.text = message.message
        }
    }

    class MyViewHolder(private val binding: MyMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: SendMessageDto) {
            binding.myMessageText.text = message.message
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            MY_MESSAGE -> MyViewHolder(
                MyMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            USER_MESSAGE -> UserViewHolder(
                UserMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> {
                throw IllegalArgumentException("Invalid type of data")
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (holder) {

            is MyViewHolder -> holder.bind(messageList[position] as SendMessageDto)
//            is UserViewHolder -> holder.bind(messageList[position] as String)
            is UserViewHolder -> holder.bind(messageList[position] as MessageDto)
            else -> {
                throw IllegalArgumentException("Invalid type in ChatAdapter/onBindViewHolder -> ")
            }
        }

    override fun getItemCount() = messageList.size

    override fun getItemViewType(position: Int): Int =
        when (messageList[position]) {
            is SendMessageDto -> MY_MESSAGE
            is MessageDto -> USER_MESSAGE
            else -> throw IllegalArgumentException("Invalid type of data -> $position")

        }

    fun updateListRecycler(item: Payload) {
        messageList.add(item)
        notifyDataSetChanged()
        Log.i("TAG", "ChatAdapter/ updateListRecycler: ")
    }

    fun clearListRecycler() {
        messageList.clear()
    }

    companion object {
        const val MY_MESSAGE = 1
        const val USER_MESSAGE = 2
    }
}

package com.natife.testtask5.ui.chatscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.natife.core.data.model.MessageDto
import com.natife.testtask5.databinding.MyMessageBinding
import com.natife.testtask5.databinding.UserMessageBinding

class ChatAdapter(private val myId: String) :
    ListAdapter<MessageDto, ChatAdapter.BaseMessageViewHolder<ViewBinding>>(
        MessageDifferenceCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseMessageViewHolder<ViewBinding> {
        return if (viewType == MY_MESSAGE) {
            MyViewHolder(
                MyMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } else {
            UserViewHolder(
                UserMessageBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        } as BaseMessageViewHolder<ViewBinding>
    }

    override fun onBindViewHolder(holder: BaseMessageViewHolder<ViewBinding>, position: Int) =
        holder.bind(getItem(position))

    override fun getItemViewType(position: Int): Int =
        if (getItem(position).from.id == myId) {
            MY_MESSAGE
        } else {
            USER_MESSAGE
        }

    fun add(message: MessageDto) {
        submitList(currentList + message)
    }

    class UserViewHolder(binding: UserMessageBinding) :
        BaseMessageViewHolder<UserMessageBinding>(binding) {
        override fun bind(message: MessageDto) {
            binding.userMessageTextView.text = message.message
            binding.userDateTextView.text = message.time
        }
    }

    class MyViewHolder(binding: MyMessageBinding) :
        BaseMessageViewHolder<MyMessageBinding>(binding) {
        override fun bind(message: MessageDto) {
            binding.myMessageTextView.text = message.message
            binding.myDateTextView.text = message.time
        }
    }

    abstract class BaseMessageViewHolder<VB : ViewBinding>(protected val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(message: MessageDto)
    }

    class MessageDifferenceCallback : DiffUtil.ItemCallback<MessageDto>() {
        override fun areContentsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.from.id == newItem.from.id && oldItem.message == newItem.message
        }

        override fun areItemsTheSame(oldItem: MessageDto, newItem: MessageDto): Boolean {
            return oldItem.from.id == newItem.from.id && oldItem.message == newItem.message
        }
    }

    companion object {
        const val MY_MESSAGE = 1
        const val USER_MESSAGE = 2
    }
}


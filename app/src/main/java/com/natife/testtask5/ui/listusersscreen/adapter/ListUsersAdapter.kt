package com.natife.testtask5.ui.listusersscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.natife.testtask5.data.model.User
import com.natife.testtask5.databinding.ItemListBinding

class ListUsersAdapter(private val itemClick: (User) -> Unit) :
    ListAdapter<User, ListUsersAdapter.BaseUserViewHolder<ViewBinding>>
        (ListUsersDifferenceCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseUserViewHolder<ViewBinding> {
        return ListUserViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ) as BaseUserViewHolder<ViewBinding>
    }

    override fun onBindViewHolder(holder: BaseUserViewHolder<ViewBinding>, position: Int) {
        holder.bind(getItem(position), itemClick)
    }

    class ListUserViewHolder(binding: ItemListBinding) :
        BaseUserViewHolder<ItemListBinding>(binding) {
        override fun bind(user: User, itemClick: (User) -> Unit) {
            binding.nameTextView.text = user.name
            binding.cardItem.setOnClickListener {
                itemClick(user)
            }
        }

    }

    abstract class BaseUserViewHolder<VB : ViewBinding>(protected val binding: VB) :
        RecyclerView.ViewHolder(binding.root) {
        abstract fun bind(user: User, itemClick: (User) -> Unit)
    }

    class ListUsersDifferenceCallback : DiffUtil.ItemCallback<User>() {
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }
    }
}

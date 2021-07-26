package com.natife.testtask5.ui.listusersscreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.natife.core.data.model.User
import com.natife.testtask5.databinding.ItemListBinding

class ListUsersAdapter(private val itemClick: (User) -> Unit) :
    ListAdapter<User, ListUsersAdapter.ListUserViewHolder>
        (ListUsersDifferenceCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListUserViewHolder {
        return ListUserViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListUserViewHolder, position: Int) {
        holder.bind(getItem(position), itemClick)
    }

    class ListUserViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
         fun bind(user: User, itemClick: (User) -> Unit) {
            binding.userNameTextView.text = user.name
            binding.userCardView.setOnClickListener {
                itemClick(user)
            }
        }

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

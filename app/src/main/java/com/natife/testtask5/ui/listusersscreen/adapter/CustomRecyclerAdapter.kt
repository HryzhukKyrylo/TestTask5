package com.natife.testtask5.ui.listusersscreen.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natife.testtask5.R
import com.natife.testtask5.databinding.ItemListBinding
import kotlinx.android.synthetic.main.item_list.view.*
import model.User

class CustomRecyclerAdapter(val itemClick: (User) -> Unit): RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> (){
    private var listUsers : List<User> = listOf()


    inner class CustomViewHolder(private val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(user: User){
          with(binding){
              nameTextView.text = user.name
              cardItem.setOnClickListener {
                  itemClick(user)
                  Log.i("TAG", "setOnClickListener: user ")
              }
          }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount() = listUsers.size

    fun updateLiseUsers(list: List<User>){
        listUsers = list
        notifyDataSetChanged()
    }
}

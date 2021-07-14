package com.natife.testtask5.ui.listusersscreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.natife.testtask5.R
import kotlinx.android.synthetic.main.item_list.view.*
import model.User

class CustomRecyclerAdapter: RecyclerView.Adapter<CustomRecyclerAdapter.CustomViewHolder> (){
    private var listUsers : List<User> = listOf()

    class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val nameText = view.nameTextView

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
        return CustomViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.nameText.text = listUsers[position].name
    }

    override fun getItemCount() = listUsers.size

    fun updateLiseUsers(list: List<User>){
        listUsers = list
        notifyDataSetChanged()
    }
}

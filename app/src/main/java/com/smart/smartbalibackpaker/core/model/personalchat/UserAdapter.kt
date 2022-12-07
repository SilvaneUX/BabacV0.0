package com.smart.smartbalibackpaker.core.model.personalchat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.chat.ChatActivity
import com.smart.smartbalibackpaker.databinding.ItemRowUserBinding

class UserAdapter(val context: Context?, private val listUser: ArrayList<DataUser>) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: DataUser) {
            with(binding){
                Glide.with(itemView.context)
                    .load(user.image)
                    .placeholder(R.drawable.account)
                    .into(ivChatUserPhoto)
                tvChatUserName.text = user.username
                tvChatUserText.text = user.email

                itemView.setOnClickListener {
                    val intent = Intent(it.context, ChatActivity::class.java)
                    intent.putExtra(ChatActivity.UID_VALUE, user.id)
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

    }

    override fun getItemCount(): Int = listUser.size
}
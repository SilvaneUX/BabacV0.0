package com.smart.smartbalibackpaker.core.model.personalchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.smart.smartbalibackpaker.R
import java.util.*


class ChatAdapter(val context: Context, private val listChat: ArrayList<ModelChat>, private val imageUri: String) : RecyclerView.Adapter<ChatAdapter.ListViewHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1
    private var user: FirebaseUser? = null

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivProfile: ImageView = itemView.findViewById(R.id.iv_chatbox_photo)
        var tvMessage: TextView = itemView.findViewById(R.id.tv_chatbox_message)
        var tvMessageTime: TextView = itemView.findViewById(R.id.tv_chatbox_timestamp)
        var tvMessageStatus: TextView = itemView.findViewById(R.id.tv_chatbox_message_status)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return if (viewType == MSG_TYPE_LEFT) {
            val binding = LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent, false)
            ListViewHolder(binding)
        } else {
            val binding = LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent, false)
            ListViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val message = listChat[position].message
        val timestamp = listChat[position].timestamp
        val status = listChat[position].statusBaca

        val date = Calendar.getInstance(Locale.getDefault())
        date.timeInMillis = timestamp.toLong()
        val dateTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", date).toString()

        holder.tvMessage.text = message
        holder.tvMessageTime.text = dateTime
        Glide.with(context)
            .load(imageUri)
            .into(holder.ivProfile)

        if (position == listChat.size-1) {
            if (status == "terbaca"){
                holder.tvMessageStatus.text = "Seen"
            } else{
                holder.tvMessageStatus.text = "Delivered"
            }
        } else {
            holder.tvMessageStatus.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = listChat.size

    override fun getItemViewType(position: Int): Int {
        user = FirebaseAuth.getInstance().currentUser
        return if (listChat[position].sender == user?.uid){
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }
}
package com.smart.smartbalibackpaker.core.model.groupchat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.R
import java.util.*
import kotlin.collections.ArrayList

class GroupChatAdapter(
    val context: Context, private val listChat: ArrayList<MessageData>
    ) : RecyclerView.Adapter<GroupChatAdapter.ListViewHolder>() {

    companion object{
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
    }

    private var user: FirebaseUser? = null

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val usernameUser: TextView = itemView.findViewById(R.id.tv_group_username)
        val tvMessage: TextView = itemView.findViewById(R.id.tv_message_group_chat)
        val tvTime: TextView = itemView.findViewById(R.id.tv_timestamp_group_chat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            val view = LayoutInflater.from(context).inflate(R.layout.row_group_chat_right, parent, false)
            ListViewHolder(view)
        } else {
            val view = LayoutInflater.from(context).inflate(R.layout.row_group_chat_left, parent, false)
            ListViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val groupChat = listChat[position]
        val message = groupChat.message
        val time = groupChat.time

        val date = Calendar.getInstance(Locale.getDefault())
        date.timeInMillis = time.toLong()
        val dateTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", date).toString()

        setUsername(groupChat, holder)
        holder.tvMessage.text = message
        holder.tvTime.text = dateTime
    }

    private fun setUsername(groupChat: MessageData, holder: ListViewHolder) {
        val ref = FirebaseDatabase.getInstance().getReference("users")
        ref.orderByChild("id").equalTo(groupChat.sender).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val username = "${ds.child("username").value}"

//                    holder.usernameUser.text = username
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun getItemViewType(position: Int): Int {
        user = FirebaseAuth.getInstance().currentUser
        return if(listChat[position].sender == user?.uid){
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    override fun getItemCount(): Int = listChat.size
}
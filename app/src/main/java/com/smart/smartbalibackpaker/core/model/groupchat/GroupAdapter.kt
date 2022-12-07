package com.smart.smartbalibackpaker.core.model.groupchat

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.chat.GroupChatActivity
import com.smart.smartbalibackpaker.core.utils.Constant.ALL_GROUP
import com.smart.smartbalibackpaker.databinding.ItemRowGroupBinding
import java.util.*

class GroupAdapter(val context: Context, private val listChat: ArrayList<GroupData>) :
    RecyclerView.Adapter<GroupAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemRowGroupBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: GroupData) {
            val groupId = data.groupId
            if(groupId == ALL_GROUP){
                itemView.visibility = View.GONE
            }
            with(binding) {
                tvGroupTitle.text = data.groupTitle
                Glide.with(itemView.context)
                    .load(data.groupImage)
                    .apply(RequestOptions().override(55, 55).centerCrop())
                    .placeholder(R.drawable.account)
                    .into(imgGroupImage)
            }

            itemView.setOnClickListener {
                val intent = Intent(it.context, GroupChatActivity::class.java)
                intent.putExtra(GroupChatActivity.GROUP_ID, groupId)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listChat[position])

        loadLastMessage(listChat[position], holder)
    }

    private fun loadLastMessage(groupData: GroupData, holder: GroupAdapter.ListViewHolder) {
        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupData.groupId).child("messages").limitToLast(1)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){
                        val message = ds.child("message").value
                        val time = ds.child("time").value
                        val sender = ds.child("sender").value

                        val date = Calendar.getInstance(Locale.getDefault())
                        date.timeInMillis = time.toString().toLong()
                        val dateTime = android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", date).toString()

                        holder.binding.tvMessage.text = message.toString()
                        holder.binding.tvTime.text = dateTime

                        //get info sender of last message
                        val senderRef = FirebaseDatabase.getInstance().getReference("users")
                        senderRef.orderByChild("id").equalTo(sender.toString())
                            .addValueEventListener(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for(ds in snapshot.children){
                                        val username = ds.child("username").value

                                        holder.binding.tvSender.text = "${username.toString()}: "
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    override fun getItemCount(): Int = listChat.size
}
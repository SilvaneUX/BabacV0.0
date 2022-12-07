package com.smart.smartbalibackpaker.core.model.groupchat

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.ItemRowGroupMemberBinding
import com.smart.smartbalibackpaker.core.model.personalchat.DataUser
import java.util.ArrayList

class AddGroupMemberAdapter(val context: Context, private val listUser: ArrayList<DataUser>, private val groupId: String, private val myGroupRole: String) : RecyclerView.Adapter<AddGroupMemberAdapter.ListViewHolder>() {

    class ListViewHolder(val binding: ItemRowGroupMemberBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: DataUser){
            with(binding){
                tvGroupMemberUsername.text = user.username
                Glide.with(itemView.context)
                    .load(user.image)
                    .placeholder(R.drawable.account)
                    .into(ivChatUserPhoto)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = ItemRowGroupMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])

        val uid  = listUser[position].id

        checkIfAlreadyExist(listUser[position], holder)

        holder.itemView.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("groups")
            ref.child(groupId).child("member").child(uid)
                .addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            val previousRole = snapshot.child("role").value.toString()
                            val options: Array<String>

                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Choose Option")
                            if (myGroupRole == "creator") {
                                if (previousRole == "admin") {
                                    options = arrayOf("Remove Admin", "Remove User")
                                    builder.setItems(
                                        options
                                    ) { dialog, which ->
                                        if (which == 0) {
                                            //Remove Admin Clicked
                                            removeAdmin(listUser[position])
                                        } else {
                                            //Remove User Clicked
                                            removeMember(listUser[position])
                                        }
                                    }.show()
                                }
                                else if (previousRole == "member") {
                                    options = arrayOf("Make Admin", "Remove User")
                                    builder.setItems(
                                        options
                                    ) { dialog, which ->
                                        if (which == 0) {
                                            //Remove Admin Clicked
                                            makeAdmin(listUser[position])
                                        } else {
                                            //Remove User Clicked
                                            removeMember(listUser[position])
                                        }
                                    }.show()
                                }
                            } else if (myGroupRole == "admin") {
                                when (previousRole) {
                                    "creator" -> {
                                        Toast.makeText(context, "Creator Of Group", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    "admin" -> {
                                        options = arrayOf("Remove Admin", "Remove User")
                                        builder.setItems(
                                            options
                                        ) { dialog, which ->
                                            if (which == 0) {
                                                //Remove Admin Clicked
                                                removeAdmin(listUser[position])
                                            } else {
                                                //Remove User Clicked
                                                removeMember(listUser[position])
                                            }
                                        }.show()
                                    }
                                    "member" -> {
                                        options = arrayOf("Make Admin", "Remove User")
                                        builder.setItems(
                                            options
                                        ) { dialog, which ->
                                            if (which == 0) {
                                                //Remove Admin Clicked
                                                makeAdmin(listUser[position])
                                            } else {
                                                //Remove User Clicked
                                                removeMember(listUser[position])
                                            }
                                        }.show()
                                    }
                                }
                            }
                        } else {
                            val builder = AlertDialog.Builder(context)
                            builder.setTitle("Add Member")
                                .setMessage("Add this user in this group?")
                                .setPositiveButton("ADD"
                                ) { dialog, which ->
                                    addMember(listUser[position])
                                }
                                .setNegativeButton("CANCEL"
                                ) {dialog, which ->
                                    dialog.dismiss()
                                }
                                .show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }

    private fun addMember(dataUser: DataUser) {
        val time = System.currentTimeMillis()
        val hashMap : HashMap<String, String> = HashMap()
        hashMap["uid"] = dataUser.id
        hashMap["role"] = "member"
        hashMap["time"] = time.toString()

        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupId).child("member").child(dataUser.id).setValue(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun makeAdmin(dataUser: DataUser) {
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["role"] = "admin"

        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupId).child("member").child(dataUser.id).updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "This User Is Now Admin", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeMember(dataUser: DataUser) {
        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupId).child("member").child(dataUser.id).removeValue()
            .addOnSuccessListener {
                Toast.makeText(context, "Successfully Remove User", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeAdmin(dataUser: DataUser) {
        val hashMap : HashMap<String, Any> = HashMap()
        hashMap["role"] = "member"

        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupId).child("member").child(dataUser.id).updateChildren(hashMap)
            .addOnSuccessListener {
                Toast.makeText(context, "This User Is No Longer Admin", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkIfAlreadyExist(dataUser: DataUser, holder: ListViewHolder) {
        val ref = FirebaseDatabase.getInstance().getReference("groups")
        ref.child(groupId).child("member").child(dataUser.id)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val role = snapshot.child("role").value.toString()
                        holder.binding.tvGroupMemberStatus.text = role
                    } else {
                        holder.binding.tvGroupMemberStatus.text = ""
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    override fun getItemCount(): Int = listUser.size

}
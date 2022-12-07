package com.smart.smartbalibackpaker.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.chat.GroupInfoActivity.Companion.GROUP_ID_INFO
import com.smart.smartbalibackpaker.databinding.ActivityGroupChatBinding
import com.smart.smartbalibackpaker.core.model.groupchat.GroupChatAdapter
import com.smart.smartbalibackpaker.core.model.groupchat.MessageData

class GroupChatActivity : AppCompatActivity() {

    private val binding by lazy{ActivityGroupChatBinding.inflate(layoutInflater)}
    private var groupId : String? = ""
    private var myGroupRole = ""
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var user : FirebaseUser
    private lateinit var adapter : GroupChatAdapter

    companion object{
        const val GROUP_ID = "groupId"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        groupId = intent.getStringExtra(GROUP_ID)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        user = auth.currentUser!!
        loadGroupInfo()
        loadGroupMessage()
        loadMyGroupRole()
        setupMenu()

        binding.btnSendGroupChat.setOnClickListener {
            val message = binding.etGroupChatBox.text.toString()
            validateMessage(message)
        }
    }

    private fun setupMenu() {
        binding.toolbarGroupChat.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.action_group_info -> {
                    val intent = Intent(this, GroupInfoActivity::class.java)
                    intent.putExtra(GROUP_ID_INFO, groupId)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun loadMyGroupRole() {
        val ref = db.getReference("groups")
        ref.child(groupId ?: "").child("member")
            .orderByChild("uid").equalTo(user.uid)
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){
                        myGroupRole = ds.child("role").value.toString()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }

    private fun loadGroupMessage() {
        val ref = db.getReference("groups")
        ref.child(groupId ?: "").child("messages").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupChatList = ArrayList<MessageData>()
                groupChatList.clear()

                for(ds in snapshot.children){
                    val model = ds.getValue(MessageData::class.java)
                    if (model != null) {
                        val groupChat = MessageData(
                            model.message,
                            model.sender,
                            model.time,
                            model.type
                        )
                        groupChatList.add(groupChat)
                    }
                }
                adapter = GroupChatAdapter(this@GroupChatActivity, groupChatList)
                setRecyclerView(adapter)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun setRecyclerView(groupChatAdapter: GroupChatAdapter) {
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding.rvChatAct.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = groupChatAdapter
        }
        adapter.notifyDataSetChanged()
    }

    private fun validateMessage(message: String) {
        if(TextUtils.isEmpty(message)){
            Toast.makeText(this, "Can't send empty message", Toast.LENGTH_SHORT).show()
        } else {
            sendMessage(message)
        }
    }

    private fun sendMessage(message: String) {
        val timeStamp = System.currentTimeMillis().toString()

        val hashMap : HashMap<String, Any> = HashMap()
        hashMap.apply {
            put("sender", "${auth.currentUser?.uid}")
            put("message", message)
            put("time", timeStamp)
            put("type", "" + "text")
        }

        val ref = db.getReference("groups")
        ref.child(groupId!!).child("messages").child(timeStamp)
            .setValue(hashMap)
            .addOnSuccessListener {
                binding.etGroupChatBox.setText("")
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun loadGroupInfo() {
        val ref = db.getReference("groups")
        ref.orderByChild("groupId").equalTo(groupId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){
                        val groupTitle = ds.child("groupTitle").value
                        val groupImage = ds.child("groupImage").value

                        binding.tvGroupChatActToolbarTitle.text = groupTitle.toString()

                        Glide.with(applicationContext)
                            .load(groupImage)
                            .apply(RequestOptions().override(55, 55).centerCrop())
                            .placeholder(R.drawable.account)
                            .into(binding.ivGroupChatActToolbarPhoto)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}
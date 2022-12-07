package com.smart.smartbalibackpaker.chat

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.databinding.ActivityChatBinding
import com.smart.smartbalibackpaker.core.model.personalchat.ChatAdapter
import com.smart.smartbalibackpaker.core.model.personalchat.ModelChat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private var user: FirebaseUser? = null
    private lateinit var dbReference: DatabaseReference
    private var myUid: String? = null

    // Seen Status
    private lateinit var dbReferenceSeenStatus: DatabaseReference
    private lateinit var seenListener: ValueEventListener
    private var listChat = ArrayList<ModelChat>()
    private lateinit var chatAdapter: ChatAdapter


    private var uidOtherUser: String? = null
    private var imageUri: String? = null

    private var hashMap: HashMap<String, Any> = HashMap()

    companion object {
        const val UID_VALUE = "uid"
    }

    override fun onStart() {
        checkUserStatus()
        checkOnlineState("online")
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
        dbReferenceSeenStatus.removeEventListener(seenListener)
        checkOnlineState(System.currentTimeMillis().toString())
        checkOnTypingState("noOne")

    }

    override fun onResume() {
        checkOnlineState("online")
        super.onResume()
    }


    private fun checkOnlineState(onlineState: String) {
        myUid = FirebaseAuth.getInstance().currentUser?.uid
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(myUid.toString())

        hashMap.put("onlineState", onlineState)
        dbReference.updateChildren(hashMap)

    }

    private fun checkOnTypingState(onTypingState: String) {
        myUid = FirebaseAuth.getInstance().currentUser?.uid
        dbReference = FirebaseDatabase.getInstance().getReference("users").child(myUid.toString())

        hashMap.put("onTypingState", onTypingState)

        dbReference.updateChildren(hashMap)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar((binding.toolbarChat))
        binding.toolbarChat.title = ""

        user = FirebaseAuth.getInstance().currentUser
        myUid = user?.uid

        uidOtherUser = intent.getStringExtra(UID_VALUE)

        //Chat List
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.stackFromEnd = true
        binding.rvChatAct.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
        }


        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        val query = dbReference.orderByChild("id").equalTo(uidOtherUser)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val username = "" + ds.child("username").value
                    imageUri = ds.child("image").value.toString()
                    val onlineState = "" + ds.child("onlineState").value
                    val onTypingState = "" + ds.child("onTypingState").value

                    if (onTypingState.equals(myUid)){
                        binding.tvChatActToolbarStatus.apply {
                            text = "typing..."
                            setTextColor(Color.parseColor("#A9A9A9"))
                        }
                    } else {
                        if (onlineState.equals("online")) {
                            binding.tvChatActToolbarStatus.apply {
                                text = "online"
                                setTextColor(Color.parseColor("#00FF00"))
                            }
                        } else {
                            val date = Calendar.getInstance(Locale.getDefault())
                            date.timeInMillis = onlineState.toLong()
                            val dateTime =
                                android.text.format.DateFormat.format("dd/MM/yyyy hh:mm:aa", date)
                                    .toString()

                            binding.tvChatActToolbarStatus.apply {
                                text = "last seen $dateTime"
                                setTextColor(Color.parseColor("#A9A9A9"))
                            }
                        }
                    }



                    binding.tvChatActToolbarUsername.text = username
                    Glide.with(applicationContext)
                        .load(imageUri)
                        .into(binding.ivChatActToolbarPhoto)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.btnSendChat.setOnClickListener {
            val messageData = binding.etChatBox.text.toString().trim()
            if (TextUtils.isEmpty(messageData)) {
                Toast.makeText(this, "No Message", Toast.LENGTH_SHORT).show()
            } else {
                sendMessage(messageData)
            }
        }

        binding.etChatBox.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().trim().length == 0){
                    checkOnTypingState("noOne")
                } else {
                    uidOtherUser?.let { checkOnTypingState(it) }
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        readMessages()

        seenMessage()
    }

    private fun seenMessage() {
        dbReferenceSeenStatus = FirebaseDatabase.getInstance().reference.child("chats")
        seenListener = dbReferenceSeenStatus.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children) {
                    val chat = ds.getValue(ModelChat::class.java)
                    if (chat?.receiver.equals(myUid) && chat?.sender.equals(uidOtherUser)) {
                        val hasSeen: HashMap<String, Any> = HashMap()
                        hasSeen.put("statusBaca", "terbaca")
                        ds.ref.updateChildren(hasSeen)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun readMessages() {

        val dbChatReference = FirebaseDatabase.getInstance().reference.child("chats")
        user = FirebaseAuth.getInstance().currentUser

        dbChatReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listChat.clear()
                for (ds in snapshot.children) {
                    val chat = ds.getValue(ModelChat::class.java)
                    if (chat?.receiver.equals(myUid.toString()) && chat?.sender.equals(uidOtherUser)
                        || chat?.receiver.equals(uidOtherUser) && chat?.sender.equals(myUid.toString())
                    ) {
                        if (chat != null) {
                            val chatData = ModelChat(
                                chat.message,
                                chat.receiver,
                                chat.sender,
                                chat.timestamp,
                                chat.statusBaca
                            )
                            listChat.add(chatData)
                        }
                        chatAdapter = ChatAdapter(this@ChatActivity, listChat, imageUri!!)
                        chatAdapter.notifyDataSetChanged()
                        binding.rvChatAct.adapter = chatAdapter
                    }
                }
                Log.d("STATUS", listChat.toString())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    private fun sendMessage(messageData: String) {
        dbReference = FirebaseDatabase.getInstance().reference

        auth.currentUser?.uid?.let { hashMap.put("sender", it) }
        hashMap["receiver"] = uidOtherUser.toString()
        hashMap["message"] = messageData
        hashMap["timestamp"] = System.currentTimeMillis().toString()
        hashMap["statusBaca"] = "terkirim"
        dbReference.child("chats").push().setValue(hashMap)

        binding.etChatBox.text = null
    }

    private fun checkUserStatus() {
        user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            myUid = user?.uid
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
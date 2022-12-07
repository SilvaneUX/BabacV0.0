package com.smart.smartbalibackpaker.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.chat.GroupAddMemberActivity.Companion.GROUP_ID_ADD
import com.smart.smartbalibackpaker.core.model.groupchat.AddGroupMemberAdapter
import com.smart.smartbalibackpaker.core.model.personalchat.DataUser
import com.smart.smartbalibackpaker.databinding.ActivityGroupInfoBinding

class GroupInfoActivity : AppCompatActivity() {

    private val binding by lazy{ActivityGroupInfoBinding.inflate(layoutInflater)}
    private var groupId : String? = ""
    private var groupRole = ""
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var user : FirebaseUser
    private var userList = ArrayList<DataUser>()

    companion object{
        const val GROUP_ID_INFO = "groupIdInfo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        groupId = intent.getStringExtra(GROUP_ID_INFO)
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        user = auth.currentUser!!

        loadGroupInfo()
        loadMemberGroupRole()
        addParticipant()
        binding.btnLeaveGroup.setOnClickListener {
            leaveGroup()
        }
    }

    private fun leaveGroup() {
            val dialogTitle = "Leave Group"
            val dialogDescription = "Are you sure want to leave group?"

            val builder = AlertDialog.Builder(this)
            builder.setTitle(dialogTitle)
                .setMessage(dialogDescription)
                .setPositiveButton("LEAVE"
                ) { dialog, which ->
                    val deleteRef = db.getReference("groups").child(groupId?: "").child("member").child(user.uid)
                    deleteRef.removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(applicationContext, "Group left successful", Toast.LENGTH_SHORT).show()
                            val deleteGroup = db.getReference("groups").child(groupId ?: "").child("member")
                            deleteGroup.addListenerForSingleValueEvent(object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if(!snapshot.exists()){
                                        val delete = db.getReference("groups").child(groupId?: "")
                                        delete.removeValue()
                                            .addOnSuccessListener {
                                            }
                                            .addOnFailureListener{
                                            }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                            startActivity(Intent(this@GroupInfoActivity, MainActivity::class.java))
                            finishAffinity()
                        }
                        .addOnFailureListener {

                        }
                }
                .setNegativeButton("CANCEL"
                ) {dialog, which -> dialog.dismiss()}
                .show()
    }

    private fun addParticipant() {
        binding.btnAddMember.setOnClickListener {
            val intent = Intent(this@GroupInfoActivity, GroupAddMemberActivity::class.java)
            intent.putExtra(GROUP_ID_ADD, groupId)
            startActivity(intent)
        }
    }

    private fun loadMemberGroupRole() {
        val ref = db.getReference("groups").child(groupId?: "").child("member")
        ref.orderByChild("uid").equalTo(user.uid)
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(ds in snapshot.children){
                        groupRole = ds.child("role").value.toString()

                        when(groupRole){
                            "member" -> {
                                binding.apply {
                                    btnAddMember.visibility = View.GONE
                                }
                            }
                            "admin" -> {
                                binding.apply {
                                    btnAddMember.visibility = View.VISIBLE
                                }
                            }
                            else -> {
                                binding.apply {
                                    btnAddMember.visibility = View.VISIBLE
                                }
                            }
                        }
                        loadMemberGroup()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun loadMemberGroup(){
        val ref = db.getReference("groups").child(groupId?: "").child("member")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for(ds in snapshot.children){
                    val uid = ds.child("uid").value.toString()

                    val memberRef = db.getReference("users")
                    memberRef.orderByChild("id").equalTo(uid)
                        .addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(data in snapshot.children){
                                    val model = data.getValue(DataUser::class.java)

                                    if (model != null) {
                                        userList.add(model)
                                    }
                                    val adapterMember = AddGroupMemberAdapter(
                                        this@GroupInfoActivity, userList, groupId ?: "" , groupRole
                                    )
                                    binding.tvGroupInfoMember.text = "Member (${userList.size})"
                                    binding.rvGroupInfoMember.apply {
                                        setHasFixedSize(true)
                                        layoutManager = LinearLayoutManager(this@GroupInfoActivity)
                                        adapter = adapterMember
                                    }
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

    private fun loadGroupInfo() {
        val ref = db.getReference("groups")
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(object:
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val groupId = ds.child("groupId").value.toString()
                    val groupTitle = ds.child("groupTitle").value.toString()
                    val groupImage = ds.child("groupImage").value.toString()
                    val createdBy = ds.child("createdBy").value.toString()
                    val time = ds.child("time").value.toString()

                    binding.collapsingTbGroupInfo.title = groupTitle
                    Glide.with(applicationContext)
                        .load(groupImage)
                        .into(binding.imgGroupInfo)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}
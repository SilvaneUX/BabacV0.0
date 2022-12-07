package com.smart.smartbalibackpaker.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.ActivityGroupAddMemberBinding
import com.smart.smartbalibackpaker.core.model.groupchat.AddGroupMemberAdapter
import com.smart.smartbalibackpaker.core.model.personalchat.DataUser

class GroupAddMemberActivity : AppCompatActivity() {

    companion object{
        const val GROUP_ID_ADD = "groupIdAdd"
    }

    private val binding by lazy { ActivityGroupAddMemberBinding.inflate(layoutInflater) }
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseDatabase
    private lateinit var user : FirebaseUser
    private var groupId : String? = ""
    private var myGroupRole = ""
    private var userList = ArrayList<DataUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        user = auth.currentUser!!
        groupId = intent.getStringExtra(GROUP_ID_ADD)
        loadGroupInfo()
    }

    private fun getAllUsers() {
        val ref = db.getReference("users")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (ds in snapshot.children){
                    val modelUser = ds.getValue(DataUser::class.java)

                    if(!auth.uid.equals(modelUser?.id)){
                        if (modelUser != null) {
                            userList.add(modelUser)
                        }
                    }
                }

                val adapterAddMember = AddGroupMemberAdapter(
                    this@GroupAddMemberActivity, userList, groupId ?: "" , myGroupRole
                )
                binding.rvAddMember.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(this@GroupAddMemberActivity)
                    adapter = adapterAddMember
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun loadGroupInfo() {
        val ref1 = db.getReference("groups")

        val ref = db.getReference("groups")
        ref.orderByChild("groupId").equalTo(groupId).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val groupId = ds.child("groupId").value.toString()
                    val groupTitle = ds.child("groupTitle").value.toString()
                    val groupImage = ds.child("groupImage").value.toString()
                    val createdBy = ds.child("createdBy").value.toString()
                    val time = ds.child("time").value.toString()

                    ref1.child(groupId).child("member").child(auth.uid?: "")
                        .addValueEventListener(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()){
                                    myGroupRole = snapshot.child("role").value.toString()
                                    setSupportActionBar(binding.tbAddMember)
                                    val title = resources.getString(R.string.add_member)
                                    supportActionBar?.title = "$title ($myGroupRole)"

                                    getAllUsers()
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
}
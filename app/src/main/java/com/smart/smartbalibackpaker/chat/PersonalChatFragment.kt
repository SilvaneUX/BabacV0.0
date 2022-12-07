package com.smart.smartbalibackpaker.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.databinding.FragmentPersonalChatBinding
import com.smart.smartbalibackpaker.core.model.personalchat.DataUser
import com.smart.smartbalibackpaker.core.model.personalchat.UserAdapter

class PersonalChatFragment : Fragment() {
//
//    private var _binding: FragmentPersonalChatBinding? = null
//    private val binding get() = _binding
//    private lateinit var adapter: UserAdapter
//
//    private lateinit var auth: FirebaseAuth
//    private lateinit var db: FirebaseDatabase
//    private var user: FirebaseUser? = null
//    private lateinit var dbReference: DatabaseReference
//
//    private var listUser = ArrayList<DataUser>()
//
//    private var myUid: String? = null
//    private var hashMap: HashMap<String, Any> = HashMap()
//
//
//    override fun onStart() {
//        checkOnlineState("online")
//        super.onStart()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        checkOnlineState(System.currentTimeMillis().toString())
//
//    }
//
//    override fun onResume() {
//        checkOnlineState("online")
//        super.onResume()
//    }
//
//    override fun onDestroy() {
//        checkOnlineState(System.currentTimeMillis().toString())
//        super.onDestroy()
//    }
//
//    private fun checkOnlineState(onlineState: String) {
//        myUid = FirebaseAuth.getInstance().currentUser?.uid
//        dbReference = FirebaseDatabase.getInstance().getReference("users").child(myUid.toString())
//
//        hashMap.put("onlineState", onlineState)
//        dbReference.updateChildren(hashMap)
//
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        _binding = FragmentPersonalChatBinding.inflate(layoutInflater, container, false)
//
//        binding?.rvChatPersonal?.layoutManager = LinearLayoutManager(context)
//        binding?.rvChatPersonal?.setHasFixedSize(true)
//
//        getUser()
//
//        return binding?.root
//    }
//
//    private fun getUser() {
//        user = FirebaseAuth.getInstance().currentUser
//        dbReference = FirebaseDatabase.getInstance().reference.child("users")
//
//        dbReference.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                listUser.clear()
//                for (ds in snapshot.children) {
//                    val dataUser = ds.getValue(DataUser::class.java)
//
//                    if (!dataUser?.id.equals(user?.uid)) {
//                        if (dataUser != null) {
//                            val data = DataUser(
//                                dataUser.id,
//                                dataUser.username,
//                                dataUser.email,
//                                dataUser.image
//                            )
//                            listUser.add(data)
//                        }
//                    }
//                    adapter = context?.let { UserAdapter(it, listUser) } ?: UserAdapter(context, ArrayList<DataUser>())
//                    binding?.rvChatPersonal?.adapter = adapter
//                }
//
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//            }
//
//        })
//    }

}
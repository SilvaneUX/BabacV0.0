package com.smart.smartbalibackpaker

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.smart.smartbalibackpaker.auth.LoginActivity
import com.smart.smartbalibackpaker.core.preferences.PreferencesSettings
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.databinding.FragmentSettingBinding
import com.smart.smartbalibackpaker.guide.DetailGuideViewModel

class SettingFragment : Fragment() {

    private lateinit var db: FirebaseDatabase
    private lateinit var user: FirebaseUser
    private lateinit var dbReference: DatabaseReference
    private var userId: String? = null
    private var binding: FragmentSettingBinding? = null
    private lateinit var auth: FirebaseAuth
    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())
        ).get(DetailGuideViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.layout_settings_preferences, PreferencesSettings())
            .commit()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        user = auth.currentUser!!
        userId = auth.currentUser?.uid

        binding!!.txtViewEmail.text = user.email
        val query = dbReference.orderByChild("email").equalTo(user.email)
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(ds in snapshot.children){
                    val username = ""+ ds.child("username").value
                    val email = ds.child("email").value
                    val image = ds.child("image").value

                    if (image == ""){
                        context?.let {
                            Glide.with(it)
                                .load(R.drawable.account)
                                .into(binding!!.imgViewProfile)
                        }
                    } else {
                        context?.let {
                            Glide.with(it)
                                .load(image)
                                .into(binding!!.imgViewProfile)
                        }
                    }

                    binding?.txtViewEmail?.text = email.toString().trim()
                    binding?.txtViewUsername?.text = username

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding?.txtLogOut?.setOnClickListener {
            auth.signOut()
            detailGuideViewModel.deleteNodes()
            Intent(context, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity?.startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

}
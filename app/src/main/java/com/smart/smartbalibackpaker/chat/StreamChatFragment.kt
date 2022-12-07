package com.smart.smartbalibackpaker.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.FragmentStreamChatBinding

class StreamChatFragment : Fragment() {

    private var _binding: FragmentStreamChatBinding? = null
    private val binding get() = _binding

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
//            R.string.tab_personalchat,
//            R.string.tab_publicgroupchat,
            R.string.tab_grupchat
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val streamChatTabAdapter = activity?.let { StreamChatTabAdapter(it) }

        binding?.vpChat?.adapter = streamChatTabAdapter
        TabLayoutMediator(
            binding?.layoutTabLayout!!,
            binding?.vpChat!!
        ) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStreamChatBinding.inflate(layoutInflater, container, false)
        // Inflate the layout for this fragment
        return binding?.root
    }

}
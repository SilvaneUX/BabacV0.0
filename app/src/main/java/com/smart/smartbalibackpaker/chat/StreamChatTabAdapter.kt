package com.smart.smartbalibackpaker.chat

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class StreamChatTabAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragment = arrayOf(
//        PersonalChatFragment(),
//        PublicGroupFragment(),
        GroupChatFragment()
    )

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}
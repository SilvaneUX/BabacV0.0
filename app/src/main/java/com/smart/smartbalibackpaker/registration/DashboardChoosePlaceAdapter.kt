package com.smart.smartbalibackpaker.registration

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class DashboardChoosePlaceAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragment = arrayOf(
//        AllPlaceFragment(),
        ChooseTouristPlaceFragment(),
        ChooseWorshipPlaceFragment()
    )

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}
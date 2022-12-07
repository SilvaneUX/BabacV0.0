package com.smart.smartbalibackpaker.dashboard

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter

class DashboardPlaceAdapter(fragment: FragmentActivity) : FragmentStateAdapter(fragment) {

    private val fragment = arrayOf(
//        AllPlaceFragment(),
        TouristPlaceFragment(),
        HotelVillaFragment(),
        WorshipPlaceFragment()
    )

    override fun getItemCount(): Int = fragment.size

    override fun createFragment(position: Int): Fragment = fragment[position]
}
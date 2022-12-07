package com.smart.smartbalibackpaker.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class OnboardingAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager){

    private val fragment = listOf(
        OnboardingOneFragment(),
        OnboardingTwoFragment(),
        OnboardingThreeFragment()
    )

    override fun getCount(): Int = fragment.size

    override fun getItem(position: Int): Fragment = fragment[position]
}
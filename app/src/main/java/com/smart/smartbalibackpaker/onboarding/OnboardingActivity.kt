package com.smart.smartbalibackpaker.onboarding

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.Preference
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.auth.LoginActivity
import com.smart.smartbalibackpaker.core.preferences.Preferences
import com.smart.smartbalibackpaker.databinding.ActivityOnboardingBinding
import com.smart.smartbalibackpaker.guide.DetailGuideActivity

class OnboardingActivity : AppCompatActivity() {

    private val binding by lazy {ActivityOnboardingBinding.inflate(layoutInflater)}
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        preferences = Preferences(this)

        if (preferences.getState("onboarding").equals("1")){
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finishAffinity()
        }

        binding.vpOnboarding.adapter = OnboardingAdapter(supportFragmentManager)
        binding.btnIn.setOnClickListener {
            preferences.setState("onboarding", "1")
            startActivity(Intent(this@OnboardingActivity, LoginActivity::class.java))
            finish()
        }
    }
}
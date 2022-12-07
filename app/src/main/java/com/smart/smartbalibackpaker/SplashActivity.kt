package com.smart.smartbalibackpaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.smart.smartbalibackpaker.databinding.ActivitySplashBinding
import com.smart.smartbalibackpaker.onboarding.OnboardingActivity
import com.smart.smartbalibackpaker.onboarding.OnboardingOneFragment

class SplashActivity : AppCompatActivity() {

    private val binding by lazy{ActivitySplashBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        Handler(mainLooper).postDelayed({
            startActivity(Intent(this@SplashActivity, OnboardingActivity::class.java))
            finishAffinity()
        }, 2000)
    }
}
package com.smart.smartbalibackpaker.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.smart.smartbalibackpaker.databinding.ActivityListVacationBinding

class ListVacationActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListVacationBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}
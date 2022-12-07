package com.smart.smartbalibackpaker.guide

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.databinding.ActivityTimerGuideBinding
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimerGuideActivity : AppCompatActivity() {

    private val binding by lazy {ActivityTimerGuideBinding.inflate(layoutInflater)}
    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailGuideViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        backToDetailGuide()
        setTimer()
        back()
        populateData()
    }

    private fun populateData() {
        val idPlace = intent.getIntExtra(EXTRA_PLACE, 0)

        detailGuideViewModel.getDetailPlace(idPlace).observe(this){
            binding.apply {
                textView17.text = it.title
            }

            Glide
                .with(this)
                .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${it.thumbnail}")
                .into(binding.imgTimer)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setTimer() {
        val localTime = LocalDateTime.now()
        val duration = localTime.plusHours(2)

        binding.tvTime.text = DateTimeFormatter.ofPattern("HH:mm").format(duration).toString()
    }

    private fun back() {
        binding.imageView2.setOnClickListener {
            finish()
        }
    }

    private fun backToDetailGuide() {
        val idPerjalanan = intent.getIntExtra(EXTRA_ID, 0)

            detailGuideViewModel.getNodes().observe(this){list ->
                Log.d("listtimer", list.toString())
                binding.btnSkipVacation.setOnClickListener{
                    if (list.size < 3){
                        detailGuideViewModel.deleteNodes()
                        finishAffinity()
                        startActivity(
                            Intent(
                                this@TimerGuideActivity,
                                MainActivity::class.java
                            )
                        )
                    } else {
                        detailGuideViewModel.deleteNode()
                        val intent = Intent(this@TimerGuideActivity, DetailGuideActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.putExtra(DetailGuideActivity.EXTRA_ID_TOUR, idPerjalanan)
                        startActivity(intent)
                        finish()
                    }
                }
            }
    }

    companion object{
        const val EXTRA_ID = "extra_id"
        const val EXTRA_PLACE = "extra_place"
    }
}
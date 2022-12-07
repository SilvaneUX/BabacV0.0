package com.smart.smartbalibackpaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.model.favorite.FavoritePlacesAdapter
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.dashboard.FavoriteViewModel
import com.smart.smartbalibackpaker.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private val binding by lazy {ActivityFavoriteBinding.inflate(layoutInflater)}
    private val favoriteViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(FavoriteViewModel::class.java)
    }
    private var adapter = FavoritePlacesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        favoriteViewModel.getFavoritePlace().observe(this, {
            Log.d("JEMBS", it.toString())
            emptyInformation(it)
            adapter.setFavoriteList(it)
            adapter.notifyDataSetChanged()
//            adapter.submitList(it)
        })

        recyclerView()
        backButton()
    }

    private fun backButton() {
        binding.btnFavoriteExit.setOnClickListener {
            finish()
        }
    }

    private fun emptyInformation(listData : List<TourismDataEntity>) {
        if (listData.isNotEmpty()){
            binding.tvFavoriteEmpty.visibility = View.GONE
        } else {
            binding.tvFavoriteEmpty.visibility = View.VISIBLE
        }
    }

    private fun recyclerView() {
        binding.apply {
            rvFavoritePlace.layoutManager = GridLayoutManager(this@FavoriteActivity, 2)
            rvFavoritePlace.setHasFixedSize(true)
            rvFavoritePlace.isNestedScrollingEnabled = false
            rvFavoritePlace.adapter = adapter
        }
    }
}
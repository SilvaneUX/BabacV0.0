package com.smart.smartbalibackpaker.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity

class FavoriteViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    fun getFavoritePlace() : LiveData<List<TourismDataEntity>>{
        return tourismRepository.getPlacesFavorite()
    }

}
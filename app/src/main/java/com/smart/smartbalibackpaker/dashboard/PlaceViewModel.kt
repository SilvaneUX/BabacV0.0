package com.smart.smartbalibackpaker.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.vo.Resource

class PlaceViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    fun getPlaces(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>> =
        tourismRepository.getAllTourism(placeType)

    fun getChoosePlaces(placeType: String, lat: String, long: String, sort: String, priceTarget: Int): LiveData<Resource<PagedList<TourismDataEntity>>> =
        tourismRepository.getChooseAllTourism(placeType, lat, long, sort, priceTarget)

    fun getAccom(): LiveData<Resource<PagedList<AccomDataEntity>>> =
        tourismRepository.getAccom()
}
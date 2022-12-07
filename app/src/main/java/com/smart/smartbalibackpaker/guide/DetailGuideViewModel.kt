package com.smart.smartbalibackpaker.guide

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.vo.Resource

class DetailGuideViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    private val _listNodeDistance = MutableLiveData<ArrayList<Int?>>()
    val listNodeDistance : LiveData<ArrayList<Int?>> = _listNodeDistance

    fun insertNode(guide: GuideMapsEntity){
        tourismRepository.insertNode(guide)
    }

    fun getNodes() : LiveData<List<GuideMapsEntity>> {
        return tourismRepository.getNodes()
    }

    fun deleteNode() {
        tourismRepository.deleteNode()
    }

    fun deleteNodes() {
        tourismRepository.deleteNodes()
    }


//    private var placeId = MutableLiveData<Int>()
//
//    fun setSelectedPlace(placeId: Int) {
//        this.placeId.value = placeId
//    }
//
//    var detailPlace: LiveData<Resource<TourismDataEntity>> = Transformations.switchMap(placeId) {
//        tourismRepository.getDetailTourism(it)
//    }

    fun getDetailPlace(placeId: Int) : LiveData<TourismDataEntity> =
        tourismRepository.getDetailGuideTourism(placeId)
}
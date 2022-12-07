package com.smart.smartbalibackpaker.core.data

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity
import com.smart.smartbalibackpaker.core.vo.Resource

interface TourismDataSource {

    fun getAllTourism(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>>

    fun getChooseAllTourism(placeType: String, lat1: String, long1: String, sort: String, priceTarget: Int): LiveData<Resource<PagedList<TourismDataEntity>>>

    fun getDetailTourism(placeId: Int): LiveData<Resource<TourismDataEntity>>

    fun getDetailGuideTourism(placeId: Int): LiveData<TourismDataEntity>

    fun deleteTourism(id: Int)

    fun setPlaceFavorite(place: TourismDataEntity, state: Boolean)

    fun getPlacesFavorite() : LiveData<List<TourismDataEntity>>

    fun uploadRecord(email: String?,
                     nama: String?,
                     noHp: String?,
                     tglDatang: String?,
                     tglpergi: String?,
                     tmpDatang: String?,
                     akomodasi: String?,
                     hotel: String?,
                     tmpWisata: String?): LiveData<Resource<UploadResultEntity>>

    fun getAccom(): LiveData<Resource<PagedList<AccomDataEntity>>>

    fun getDetailAccom(id: Int) : LiveData<AccomDataEntity>

//    fun getDetailAccom(accomId: Int): LiveData<Resource<AccomDataEntity>>
}
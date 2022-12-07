package com.smart.smartbalibackpaker.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.vo.Resource

class RecordGuideViewModel(private val tourismRepository: TourismRepository): ViewModel() {

    fun getAllRecordGuide(backpackerId: String): LiveData<Resource<RecordGuideEntity>> =
        tourismRepository.getAllRecordGuide(backpackerId)

    fun getRecordVacation(backpackerId: String): LiveData<List<RecordVacationListEntity>> =
        tourismRepository.getRecordVacation(backpackerId)

    fun getDetailRecordVacation(idPerjalanan: Int): LiveData<RecordVacationListEntity> =
        tourismRepository.getDetailRecordVacation(idPerjalanan)

    fun getAccom() : LiveData<Resource<PagedList<AccomDataEntity>>> =
        tourismRepository.getAccom()

    fun getDetailAccom(id: Int) : LiveData<AccomDataEntity> {
        return tourismRepository.getDetailAccom(id)
    }
}
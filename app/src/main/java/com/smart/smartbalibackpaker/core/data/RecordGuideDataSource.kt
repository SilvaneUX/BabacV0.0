package com.smart.smartbalibackpaker.core.data

import androidx.lifecycle.LiveData
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.vo.Resource

interface RecordGuideDataSource {

    fun getAllRecordGuide(backpackerId : String): LiveData<Resource<RecordGuideEntity>>

    fun getRecordVacation(backpackerId : String) : LiveData<List<RecordVacationListEntity>>

    fun getDetailRecordVacation(idPerjalanan: Int) : LiveData<RecordVacationListEntity>

}
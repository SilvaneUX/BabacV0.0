package com.smart.smartbalibackpaker.registration

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smart.smartbalibackpaker.core.data.TourismRepository
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity
import com.smart.smartbalibackpaker.core.vo.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistSecondFormViewModel(private val tourismRepository: TourismRepository) : ViewModel() {

    fun uploadResult(email : String,
                     nama : String,
                     noHp : String,
                     tglDatang : String,
                     tglPergi : String,
                     tmpDatang : String,
                     akomodasi : String,
                     choosePlace : String) : LiveData<Resource<UploadResultEntity>> {
        return tourismRepository.uploadRecord(email, nama, noHp, tglDatang, tglPergi, tmpDatang, akomodasi, "", choosePlace)
    }

    private var placeId = MutableLiveData<Int>()

    fun setSelectedPlace(placeId: Int) {
        this.placeId.value = placeId
    }

    var detailPlace: LiveData<Resource<TourismDataEntity>> = Transformations.switchMap(placeId) {
        tourismRepository.getDetailTourism(it)
    }

    fun deleteTourism(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            tourismRepository.deleteTourism(id)
        }
    }

}
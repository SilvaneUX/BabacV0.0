package com.smart.smartbalibackpaker.core.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.smart.smartbalibackpaker.core.data.source.local.entity.*
import com.smart.smartbalibackpaker.core.data.source.local.room.*

class LocalDataSource private constructor(
    private val movieDao: TourismDao,
    private val guideDao: GuideDao,
    private val accomDataDao: AccomDataDao,
    private val uploadResultDao: UploadResultDao,
    private val recordGuideDao: RecordGuideDao,
    private val recordVacationDao: RecordVacationDao,
    ) {

    fun insertPlace(place: List<TourismDataEntity>) {
        movieDao.insertPlace(place)
    }

    fun updatePlace(place: TourismDataEntity) {
        movieDao.updatePlace(place)
    }

    fun deletePlace(id: Int){
        movieDao.deleteTourism(id)
    }

    fun getPlaces(placeType: String): DataSource.Factory<Int, TourismDataEntity> =
        movieDao.getPlace(placeType)

    fun getChoosePlaces(placeType: String, sortItem: String, priceTarget: Int): DataSource.Factory<Int, TourismDataEntity> {
        var arrayRandomPlace : DataSource.Factory<Int, TourismDataEntity>
        when (sortItem) {
            "price" -> {
                return movieDao.getChoosePlaceByPrice(placeType)
            }
            "priceTarget" -> {
                return movieDao.getRandomByPrice(priceTarget)
            }
            else -> (
                    return movieDao.getChoosePlace(placeType)
                    )
        }
    }

    fun getDetailPlace(placeId: Int): LiveData<TourismDataEntity> =
        movieDao.getDetailPlace(placeId)

    fun setPlaceFavorite(place: TourismDataEntity, newState: Boolean){
        place.isFavorite = newState
        movieDao.updatePlace(place)
    }

    fun getPlacesFavorite() : LiveData<List<TourismDataEntity>> = movieDao.getFavoritePlace()

    fun insertResult(uploadResultEntity: UploadResultEntity) =
        uploadResultDao.insertRecordResult(uploadResultEntity)

    fun getUploadResult(tglDatang: String) =
        uploadResultDao.getUploadResult(tglDatang)

    fun insertAccom(accomDataEntity: List<AccomDataEntity>) =
        accomDataDao.insertAccom(accomDataEntity)

    fun updateAccom(accomDataEntity: AccomDataEntity) =
        accomDataDao.updateAccom(accomDataEntity)

    fun getAccom() =
        accomDataDao.getAccom()

    fun getDetailAccom(accomId: Int) =
        accomDataDao.getDetailAccom(accomId)

    fun insertNode(guide: GuideMapsEntity){
        guideDao.insertNode(guide)
    }

    fun getNodes(): LiveData<List<GuideMapsEntity>> =
        guideDao.getNodes()

    fun deleteNode(){
        guideDao.deleteNode()
    }

    fun deleteNodes(){
        guideDao.deleteNodes()
    }

    fun insertRecordGuide(record: RecordGuideEntity) {
        recordGuideDao.insertRecordGuide(record)
    }

    fun getAllRecordGuide(backpackerId: String) : LiveData<RecordGuideEntity> =
        recordGuideDao.getAllRecordGuide(backpackerId)

    fun insertRecordVacation(record : List<RecordVacationListEntity>){
        recordVacationDao.insertVacation(record)
    }

    fun deleteRecordVacation(backpackerId: String){
        recordVacationDao.deleteVacation(backpackerId)
    }

    fun getRecordVacation(backpackerId: String) : LiveData<List<RecordVacationListEntity>> =
        recordVacationDao.getVacation(backpackerId)

    fun getDetailRecordVacation(idPerjalanan: Int): LiveData<RecordVacationListEntity> =
        recordVacationDao.getDetailVacation(idPerjalanan)

    companion object {
        private var INSTANCE: LocalDataSource? = null

        fun getInstance(tourismDao: TourismDao, guideDao: GuideDao, accomDataDao: AccomDataDao, uploadResultDao: UploadResultDao, recordGuideDao: RecordGuideDao, recordVacationDao: RecordVacationDao): LocalDataSource =
            INSTANCE ?: LocalDataSource(tourismDao, guideDao, accomDataDao, uploadResultDao, recordGuideDao, recordVacationDao)
    }
}
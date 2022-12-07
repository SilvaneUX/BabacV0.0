package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity

@Dao
interface TourismDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = TourismDataEntity::class)
    fun insertPlace(tourism: List<TourismDataEntity>)

    @Update(entity = TourismDataEntity::class)
    fun updatePlace(tourism: TourismDataEntity)

    @Query("DELETE FROM tourism_table WHERE tourism_id = :name")
    fun deleteTourism(name: Int): Int

    @Query("SELECT * FROM tourism_table WHERE type = :placeType")
    fun getPlace(placeType: String): DataSource.Factory<Int, TourismDataEntity>

    @Query("SELECT * FROM tourism_table WHERE type = :placeType ORDER BY slug")
    fun getChoosePlace(placeType: String): DataSource.Factory<Int, TourismDataEntity>

    @Query("SELECT * FROM tourism_table WHERE type = :placeType ORDER BY price ASC")
    fun getChoosePlaceByPrice(placeType: String): DataSource.Factory<Int, TourismDataEntity>

    @Query("SELECT * FROM tourism_table WHERE isFavorite = 1")
    fun getFavoritePlace(): LiveData<List<TourismDataEntity>>

    @Query("SELECT * FROM tourism_table WHERE tourism_id = :placeId")
    fun getDetailPlace(placeId: Int): LiveData<TourismDataEntity>

    @Query("SELECT * FROM tourism_table WHERE price < :priceTarget GROUP BY price ORDER BY RANDOM()")
    fun getRandomByPrice(priceTarget: Int): DataSource.Factory<Int, TourismDataEntity>

//    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = UploadResultEntity::class)
//    fun insertRecordResult(message: String, isSuccess: Boolean): LiveData<UploadResultEntity>
}
package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity

@Dao
interface AccomDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = AccomDataEntity::class)
    fun insertAccom(tourism: List<AccomDataEntity>)

    @Update(entity = AccomDataEntity::class)
    fun updateAccom(accom: AccomDataEntity)

    @Query("SELECT * FROM accom_table")
    fun getAccom(): DataSource.Factory<Int, AccomDataEntity>

    @Query("SELECT * FROM accom_table WHERE accom_id = :accomId")
    fun getDetailAccom(accomId: Int): LiveData<AccomDataEntity>
}
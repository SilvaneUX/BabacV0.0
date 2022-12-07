package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.UploadResultEntity

@Dao
interface UploadResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = UploadResultEntity::class)
    fun insertRecordResult(uploadResultEntity: UploadResultEntity)

    @Query("SELECT * FROM tourism_response_table WHERE message = :tglDatang")
    fun getUploadResult(tglDatang: String): LiveData<UploadResultEntity>
}
package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity

@Dao
interface RecordGuideDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = RecordGuideEntity::class)
    fun insertRecordGuide(recordGuide: RecordGuideEntity)

    @Query("SELECT * FROM record_guide_table WHERE backpacker_id = :backpackerId")
    fun getAllRecordGuide(backpackerId: String): LiveData<RecordGuideEntity>

}
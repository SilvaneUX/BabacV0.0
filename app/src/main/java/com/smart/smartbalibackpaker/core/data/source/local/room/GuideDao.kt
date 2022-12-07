package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity

@Dao
interface GuideDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = GuideMapsEntity::class)
    fun insertNode(guide: GuideMapsEntity)

    @Query("SELECT * FROM guide_maps_table ORDER by placeNumber ASC")
    fun getNodes(): LiveData<List<GuideMapsEntity>>

    @Query("DELETE FROM guide_maps_table WHERE placeNumber IN (SELECT placeNumber FROM guide_maps_table ORDER BY placeNumber LIMIT 1)")
    fun deleteNode()

    @Query("DELETE FROM guide_maps_table")
    fun deleteNodes()

}
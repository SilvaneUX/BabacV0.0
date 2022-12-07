package com.smart.smartbalibackpaker.core.data.source.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.smart.smartbalibackpaker.core.data.source.local.entity.GuideMapsEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity

@Dao
interface RecordVacationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE, entity = RecordVacationListEntity::class)
    fun insertVacation(recordVacationListEntity: List<RecordVacationListEntity>)

    @Query("DELETE FROM table_vacation_list WHERE id_backpacker = :backpackerId")
    fun deleteVacation(backpackerId: String)

    @Query("SELECT * FROM table_vacation_list WHERE id_backpacker = :backpackerId")
    fun getVacation(backpackerId: String): LiveData<List<RecordVacationListEntity>>

    @Query("SELECT * FROM table_vacation_list WHERE id_perjalanan = :idPerjalanan")
    fun getDetailVacation(idPerjalanan: Int) : LiveData<RecordVacationListEntity>
}
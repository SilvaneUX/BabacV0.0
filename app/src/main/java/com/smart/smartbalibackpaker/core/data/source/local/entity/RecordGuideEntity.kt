package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.smart.smartbalibackpaker.core.data.source.remote.response.ListPerjalananItem
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "record_guide_table")
@Parcelize
data class RecordGuideEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "id")
    var id : Int? = null,

    @ColumnInfo(name = "backpacker_id")
    var backpackerId : String? = null,

    @ColumnInfo(name = "no_hp")
    var noHp : String? = null,

    @ColumnInfo(name = "vacation_count")
    var vacationCount : Int? = null,

) : Parcelable
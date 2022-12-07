package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName="table_vacation_list")
@Parcelize
data class RecordVacationListEntity(

    @PrimaryKey(autoGenerate = false)
    @NonNull
    @ColumnInfo(name = "id")
    val id : String,

    @ColumnInfo(name = "id_backpacker")
    val idBackpacker : String? = null,

    @ColumnInfo(name ="tgl_perjalanan")
    val tglPerjalanan: String? = null,

    // list
    @ColumnInfo(name ="id_tempat_wisata")
    val idTempatWisata: String? = null,

    @ColumnInfo(name = "id_perjalanan")
    val idPerjalanan: Int? = null,

    // any
    @ColumnInfo(name = "id_akomodasi")
    val idAkomodasi: String? = null,

    @ColumnInfo(name = "tgl_pulang")
    val tglPulang: String? = null
) : Parcelable
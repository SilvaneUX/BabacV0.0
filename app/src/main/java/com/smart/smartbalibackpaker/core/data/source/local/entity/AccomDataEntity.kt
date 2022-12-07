package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "accom_table")
@Parcelize
data class AccomDataEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "accom_id")
    var id: Int? = null,

    @ColumnInfo(name = "id_car")
    var id_car : Int? = null,

    @ColumnInfo(name = "no_car")
    var no_car: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "status")
    var status: String? = null,

    @ColumnInfo(name = "year_production")
    var rent_price: String? = null,

    @ColumnInfo(name = "passenger_capacity")
    var passenger_capacity: String? = null,

    @ColumnInfo(name = "pictures")
    var pictures: String? = null
): Parcelable

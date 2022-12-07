package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "tourism_table")
@Parcelize
data class TourismDataEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "tourism_id")
    var id: Int? = null,
    var thumbnail: String? = null,
    var address: String? = null,
    @Ignore
    var updatedAt: String? = null,
    var latitude: String? = null,
    var longtitude: String? = null,
    var price: Int? = 0,
    @Ignore
    var createdAt: String? = null,
    var title: String? = null,
    var type: String? = null,
    var slug: Double? = 0.0,
    var desc: String? = null,
    var isFavorite: Boolean = false,
//    var pictures: List<String?>? = null,
) : Parcelable
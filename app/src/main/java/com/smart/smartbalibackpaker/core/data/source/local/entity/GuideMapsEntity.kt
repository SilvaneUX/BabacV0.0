package com.smart.smartbalibackpaker.core.data.source.local.entity

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import java.lang.reflect.Constructor

@Entity(tableName = "guide_maps_table")
data class GuideMapsEntity(
    @PrimaryKey()
    @NonNull
    var id: Int,
    var idPlace: Int= 0,
    var namePlace: String = "",
    var placeNumber : Int = 0,
    var placeLatLng: String = "",
)
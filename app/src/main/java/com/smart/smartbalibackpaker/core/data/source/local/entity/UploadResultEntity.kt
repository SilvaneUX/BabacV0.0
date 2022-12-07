package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull

@Entity(tableName = "tourism_response_table")
@Parcelize
data class UploadResultEntity (
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "upload_result_id")
    var upload_result_id: Int? = null,
    @ColumnInfo(name = "message")
    var message: String? = null,
    @ColumnInfo(name = "isSuccess")
    var isSuccess: Boolean? = false,

): Parcelable {constructor(): this(upload_result_id = 0, message = "", isSuccess = false)}

//    var email: String? = null,
//    var nama: String? = null,
//    var noHp: String? = null,
//    var tglDatang: String? = null,
//    var tglpergi: String? = null,
//    var tmpDatang: String?,
//    var akomodasi: String?,
//    var hotel: String?,
//    var tmpWisata: String?
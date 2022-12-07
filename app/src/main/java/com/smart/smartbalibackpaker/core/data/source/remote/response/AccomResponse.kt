package com.smart.smartbalibackpaker.core.data.source.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccomResponse(

	@field:SerializedName("input")
	val input: List<String?>? = null,

	@field:SerializedName("recordsFiltered")
	val recordsFiltered: Int? = null,

	@field:SerializedName("data")
	val data: List<DataItemAccom?>? = null,

	@field:SerializedName("draw")
	val draw: Int? = null,

	@field:SerializedName("recordsTotal")
	val recordsTotal: Int? = null
) : Parcelable

//@Parcelize
//data class PicturesItem(
//
//	@field:SerializedName("id")
//	val id: Int? = null,
//
//	@field:SerializedName("car_id")
//	val carId: Int? = null,
//
//	@field:SerializedName("picture")
//	val picture: String? = null
//) : Parcelable

// Changed (DataItem --> AccomResponse)
@Parcelize
data class DataItemAccom(

	@field:SerializedName("rent_price")
	val rentPrice: String? = null,

	@field:SerializedName("passenger_capacity")
	val passengerCapacity: Int? = null,

//	@field:SerializedName("verified")
//	val verified: Any? = null,

	@field:SerializedName("year_production")
	val yearProduction: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("no_car")
	val noCar: String? = null,

//	//Changed (List<Any> --> List<String>)
//	@field:SerializedName("pictures")
//	val pictures: List<String?>? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("fuel_capacity")
	val fuelCapacity: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("purchase_price")
	val purchasePrice: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("actions")
	val actions: String? = null,

	@field:SerializedName("DT_RowIndex")
	val dTRowIndex: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
) : Parcelable

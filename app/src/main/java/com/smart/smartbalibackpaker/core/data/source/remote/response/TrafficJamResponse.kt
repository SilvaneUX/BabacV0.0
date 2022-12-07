package com.smart.smartbalibackpaker.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TrafficJamResponse(

	@field:SerializedName("destination_addresses")
	val destinationAddresses: List<String?>? = null,

	@field:SerializedName("rows")
	val rows: List<RowsItem?>? = null,

	@field:SerializedName("origin_addresses")
	val originAddresses: List<String?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Duration(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
)

data class ElementsItem(

	@field:SerializedName("duration")
	val duration: Duration? = null,

	@field:SerializedName("distance")
	val distance: Distance? = null,

	@field:SerializedName("duration_in_traffic")
	val durationInTraffic: DurationInTraffic? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Distance(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
)

data class DurationInTraffic(

	@field:SerializedName("text")
	val text: String? = null,

	@field:SerializedName("value")
	val value: Int? = null
)

data class RowsItem(

	@field:SerializedName("elements")
	val elements: List<ElementsItem?>? = null
)

package com.smart.smartbalibackpaker.registration

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ItemGridChoosePlaceBinding
import android.os.Bundle
import android.util.Log
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import com.smart.smartbalibackpaker.core.data.source.remote.response.NearbySearchResponse
import com.smart.smartbalibackpaker.core.utils.Coordinate
import com.smart.smartbalibackpaker.core.utils.Formatter
import com.smart.smartbalibackpaker.core.utils.distance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*


class ChoosePlaceAdapter : PagedListAdapter<TourismDataEntity, ChoosePlaceAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(itemView: ItemGridChoosePlaceBinding) : RecyclerView.ViewHolder(itemView.root) {

        private val imgPhoto = itemView.ivPlace
        private val tvTitle = itemView.tvTitlePlace
        private val tvLocation = itemView.tvLocationPlace
        private val tvPlaceDistance = itemView.tvPlaceDistance
        private val tvPlaceDesc = itemView.tvPlaceDesc
        private val tvTicketPrice = itemView.tvTicketPrice
        fun bind(place: TourismDataEntity) {
            tvTitle.text = place.title
            tvLocation.text = place.address
//            tvLocation.text = place.address
//            val activity = ChoosePlaceActivity()
//            val arrivalPlace =
//                activity.intent.getStringExtra(ChoosePlaceActivity.NEXT_NODE_LAT)?.toDouble()?.let {
//                    Coordinate(
//                        it, activity.intent.getStringExtra(ChoosePlaceActivity.NEXT_NODE_LAT)?.toDouble()!!
//                            .toDouble())
//                }
//            val destNode = Coordinate(place.latitude?.toDouble()!!, place.longtitude?.toDouble()!!)
//            val resultDist = arrivalPlace?.let { distance(it, destNode) }
            tvPlaceDistance.text = "${place.slug.toString()} km"

            // API Key Google Maps
            val apiKeyGMaps = "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM"
            // Lat Lng each Response
            val location = "${place.latitude},${place.longtitude}"
            // Search Radius (meters)
            val radius = "500"

            val name = place.title.toString()

            var placeRate = ""
            var placeRateCount = ""

            ConfigNetwork
                .getPlacesNetwork().searchNearby(name, location,radius,apiKeyGMaps)
                .enqueue(object: Callback<NearbySearchResponse> {
                    override fun onResponse(
                        call: Call<NearbySearchResponse>,
                        response: Response<NearbySearchResponse>
                    ) {
                        if(response.isSuccessful){
                            val placeData = response.body()?.results?.firstOrNull { it?.rating != null }
//                            val placeData = response.body()?.results?.firstNotNullOf { it?.rating.toString() }
                            placeRate = placeData?.rating.toString()
                            Log.d("HASILNYARATE", placeData?.rating.toString())
                            placeRateCount = placeData?.userRatingsTotal.toString()
                            tvPlaceDesc.text = "${placeData?.rating} (${placeData?.userRatingsTotal} reviews)"
                        }
                    }

                    override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                        placeRate = "-"
                        placeRateCount = "-"
                        Log.d("DATADISTANCEEE", "GAGAL")
                    }
                })
            tvPlaceDesc.text = "$placeRate ($placeRateCount reviews)"

            tvTicketPrice.text = place.price?.toDouble()?.let { Formatter.rupiahFormatter(it) }

            Glide.with(itemView.context)
                .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${place.thumbnail}")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .apply(RequestOptions().transform(RoundedCorners(10)))
                .into(imgPhoto)
            itemView.setOnClickListener {
                val activity = ChoosePlaceActivity()
//                val arguments: Bundle? = fragment.arguments
                val emailIntent = (it.context as Activity).intent
                val desiredString = emailIntent.getStringExtra(ChoosePlaceActivity.ARRIVAL_PLACE)
                val desiredPlace = emailIntent.getStringExtra(ChoosePlaceActivity.PLACE_DATA_EXIST)  ?: desiredString
                val desiredPlaceName = emailIntent.getStringExtra(ChoosePlaceActivity.PLACE_NAME_EXIST)  ?: desiredString

                val email = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_EMAIL).toString()
                val nama = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_NAMA).toString()
                val noHp = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_NOHP).toString()
                val tglDatang = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_DATANG).toString()
                val tglPergi = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_PERGI).toString()
                val tmpDatang = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_TEMP_DATANG).toString()
                val budget = emailIntent.getStringExtra(ChoosePlaceActivity.EXTRA_BUDGET)?.toInt() ?: 0
                val budgetLeft = budget - place.price!!
                Log.d("SISABUDGET", "$budget dannn $budgetLeft")

                val intent = Intent(itemView.context, RegistSecondFormActivity::class.java)
                if (desiredPlace != "PLACE_DATA" && desiredPlace != "null") {
                    intent.putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${place.id}")
                    intent.putExtra(RegistSecondFormActivity.PLACE_NAME, "$desiredPlaceName,${place.title}")
                    intent.putExtra(RegistSecondFormActivity.DELETED_PLACE_DATA, place.id.toString())
                    intent.apply {
                        putExtra(RegistSecondFormActivity.EXTRA_ID, place.id)
                        putExtra(RegistSecondFormActivity.EXTRA_NAME, place.title)
    //                    putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${place.title},")
                    }
                    intent.putExtra(RegistSecondFormActivity.EXTRA_EMAIL, email)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NAMA, nama )
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NOHP, noHp)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_DATANG, tglDatang)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_PERGI, tglPergi)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_TEMP_DATANG, tmpDatang)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_BUDGET, budgetLeft.toString())
                    intent.putExtra(RegistSecondFormActivity.EXTRA_BUDGET_LEFT, budgetLeft.toString())
                }

                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemGridChoosePlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TourismDataEntity>() {
            override fun areItemsTheSame(
                oldItem: TourismDataEntity,
                newItem: TourismDataEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TourismDataEntity,
                newItem: TourismDataEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}
package com.smart.smartbalibackpaker.core.data.source.remote

import com.smart.smartbalibackpaker.core.data.source.remote.service.*
import com.smart.smartbalibackpaker.core.utils.Constant.GOOGLE_MAPS_PLACE_URL
import com.smart.smartbalibackpaker.core.utils.Constant.GOOGLE_MAPS_URL
import com.smart.smartbalibackpaker.core.utils.Constant.TOURISM_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigNetwork {
    companion object {
        fun getRetrofit(): TourismService {
            val retrofit = Retrofit.Builder()
                .baseUrl(TOURISM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TourismService::class.java)
        }

        fun getRoutesNetwork(): RouteService {
            val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(RouteService::class.java)
        }

        fun getPlacesNetwork(): PlaceService {
            val retrofit = Retrofit.Builder()
                .baseUrl(GOOGLE_MAPS_PLACE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(PlaceService::class.java)
        }

        fun getTrafficJamNetwork(): TrafficJamService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/distancematrix/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(TrafficJamService::class.java)
        }

        fun getRecordNetwork(): DetailGuideService {
            val retrofit = Retrofit.Builder()
                .baseUrl(TOURISM_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(DetailGuideService::class.java)
        }
    }
}
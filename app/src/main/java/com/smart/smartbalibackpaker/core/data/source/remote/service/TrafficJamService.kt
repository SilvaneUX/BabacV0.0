package com.smart.smartbalibackpaker.core.data.source.remote.service

import com.smart.smartbalibackpaker.core.data.source.remote.response.TrafficJamResponse
import com.smart.smartbalibackpaker.core.model.ResponseRoute
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TrafficJamService {
    @GET("json")
    fun getTrafficJam(
        @Query("origins") origin: String,
        @Query("destinations") destination: String,
        @Query("departure_time") waypoints: String = "now",
        @Query("key") key: String
    ) : Call<TrafficJamResponse>
}
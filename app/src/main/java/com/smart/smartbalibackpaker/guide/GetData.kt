package com.smart.smartbalibackpaker.guide

import com.smart.smartbalibackpaker.core.model.ResponseRoute


interface GetData{
    fun onGetData(data: Int?, idxOrigin: Int, idxDestination: Int)
}

interface GetTrafficJamData{
    fun onGetTrafficJamData(expectedDuration: Int, durationInTraffic: Int)
}
package com.smart.smartbalibackpaker.core.model

data class TrafficJam(
    var expectedDuration : String = "",
    var durationInTraffic : String = "",
    var slice: Int = 0
)
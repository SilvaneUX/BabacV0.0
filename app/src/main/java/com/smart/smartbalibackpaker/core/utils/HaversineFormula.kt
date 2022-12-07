package com.smart.smartbalibackpaker.core.utils

import kotlin.math.*

const val R = 6372.8 // Earth radius in miles

fun distance(koordinat1: Coordinate, koordinat2: Coordinate): Double {

    val selisihLat = abs(koordinat1.lat - koordinat2.lat)
    val selisihLong = abs(koordinat1.lng - koordinat2.lng)

    val a = sin(abs(selisihLat) / 2).pow(2.0) + cos(koordinat1.lat) * cos(koordinat2.lat) *
            sin(abs(selisihLong) / 2).pow(2.0)

    val c = 2 * asin(sqrt(a))

    val d = R * c

    return (d * 100.0).roundToInt() / 100.0
}

//        val dLat = Math.toRadians(destination.lat - this.lat);
//        val dLon = Math.toRadians(destination.lon - this.lon);
//        val originLat = Math.toRadians(this.lat);
//        val destinationLat = Math.toRadians(destination.lat);

//        val a = sin(c2.lat-c1.lat / 2).pow(2.toDouble()) + sin(c2.lng-c1.lng / 2).pow(2.toDouble()) * cos(c1.lat) * cos(c2.lat);
//        val c = 2 * asin(sqrt(a));
//        return R * c;

//    return 2 * R * asin(
//        sqrt(
//            sin(abs(c1.lat - c2.lat) / 2).pow(2.0) + cos(c1.lat) * cos(c2.lat) *
//                    sin(abs(c1.lng - c2.lng) / 2).pow(2.0)
//        )
//    )
class Coordinate(_lat: Double, _long: Double) {
    // Google uses degrees, but Java trig functions expect radians
    val lat = Math.toRadians(_lat)
    val lng = Math.toRadians(_long)
}


//class Geo(private val lat: Double, private val lon: Double) {
//
//    companion object {
//        const val earthRadiusKm: Double = 6372.8
//    }
//
//    /**
//     * Haversine formula. Giving great-circle distances between two points on a sphere from their longitudes and latitudes.
//     * It is a special case of a more general formula in spherical trigonometry, the law of haversines, relating the
//     * sides and angles of spherical "triangles".
//     *
//     * https://rosettacode.org/wiki/Haversine_formula#Java
//     *
//     * @return Distance in kilometers
//     */
//    fun haversine(destination: Geo): Double {
//        val dLat = Math.toRadians(destination.lat - this.lat);
//        val dLon = Math.toRadians(destination.lon - this.lon);
//        val originLat = Math.toRadians(this.lat);
//        val destinationLat = Math.toRadians(destination.lat);
//
//        val a = Math.pow(Math.sin(dLat / 2), 2.toDouble()) + Math.pow(Math.sin(dLon / 2), 2.toDouble()) * Math.cos(originLat) * Math.cos(destinationLat);
//        val c = 2 * Math.asin(Math.sqrt(a));
//        return earthRadiusKm * c;
//    }
//
//}
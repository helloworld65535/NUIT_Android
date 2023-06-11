package com.dzy.chiyan.api

import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class IPLocationApiClient {
    private final val apiKey = "LMUBZ-OZYKQ-FHO56-4UB3Y-KNCFO-7YB34"
    private val client = OkHttpClient()
    fun getLocation(callback: IPLocationCallback) {
        val url = "https://apis.map.qq.com/ws/location/v1/ip?ip=&key=$apiKey"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    val locationData = parseLocationData(responseData)
                    callback.onSuccess(locationData)
                } else {
                    callback.onError("Failed to get location data")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e.message ?: "An error occurred")
            }
        })
    }

    private fun parseLocationData(responseData: String): LocationData {
        val json = JSONObject(responseData)
        val status = json.getInt("status")
        val message = json.getString("message")
        val resultJson = json.getJSONObject("result")
        val ip = resultJson.getString("ip")
        val locationJson = resultJson.getJSONObject("location")
        val lat = locationJson.getDouble("lat")
        val lng = locationJson.getDouble("lng")
        val adInfoJson = resultJson.getJSONObject("ad_info")
        val nation = adInfoJson.getString("nation")
        val province = adInfoJson.getString("province")
        val city = adInfoJson.optString("city")
        val district = adInfoJson.optString("district")
        val adcode = adInfoJson.getInt("adcode")

        return LocationData(status, message, ip, lat, lng, nation, province, city, district, adcode)
    }
}

data class LocationData(
    val status: Int,
    val message: String,
    val ip: String,
    val lat: Double,
    val lng: Double,
    val nation: String,
    val province: String,
    val city: String?,
    val district: String?,
    val adcode: Int
)

interface IPLocationCallback {
    fun onSuccess(locationData: LocationData)
    fun onError(errorMessage: String)
}

package com.dzy.chiyan.api

import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WeatherApiClient() {
    private val client = OkHttpClient()
    private final val apiKey = "fu34p3a6qCJpFRlJ"
    fun getRealtimeWeather(latitude: Double, longitude: Double, callback: WeatherCallback) {
        val url = "https://api.caiyunapp.com/v2.5/$apiKey/$longitude,$latitude/realtime"
        Log.d("url", url)
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                if (response.isSuccessful && responseData != null) {
                    Log.d("json","$responseData")
                    val weatherData = parseWeatherData(responseData)
                    callback.onSuccess(weatherData)
                } else {
                    callback.onError("Failed to get weather data")
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e.message ?: "An error occurred")
            }
        })
    }

    private fun parseWeatherData(responseData: String): WeatherData {
        val json = JSONObject(responseData)
        val temperature = json.getJSONObject("result").getJSONObject("realtime").getDouble("temperature")
        val humidity = json.getJSONObject("result").getJSONObject("realtime").getDouble("humidity")
        val windSpeed =
            json.getJSONObject("result").getJSONObject("realtime").getJSONObject("wind").getDouble("speed") / 10
        val windDirection =
            json.getJSONObject("result").getJSONObject("realtime").getJSONObject("wind").getDouble("direction")
        val datetime = convertTimestampToTime(json.getLong("server_time"))
        // 解析其他所需的天气数据

        return WeatherData(temperature, humidity, windSpeed, windDirection, datetime)
    }

    fun convertTimestampToTime(timestamp: Long): String {
        // 创建一个SimpleDateFormat对象，指定输出的时间格式
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        // 将时间戳转换为Date对象
        val date = Date(timestamp * 1000)
        // 使用SimpleDateFormat格式化Date对象为字符串
        val formattedTime = dateFormat.format(date)
        return formattedTime
    }
}

data class WeatherData(
    val temperature: Double,//气温
    val humidity: Double,//相对湿度
    val windSpeed: Double,//风速
    val windDirection: Double,//风向
    val datetime: String
    // 添加其他所需的天气数据字段
)

interface WeatherCallback {
    fun onSuccess(weatherData: WeatherData)
    fun onError(errorMessage: String)
}

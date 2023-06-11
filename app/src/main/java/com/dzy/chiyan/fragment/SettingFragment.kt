package com.dzy.chiyan.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dzy.chiyan.R
import com.dzy.chiyan.api.*

class SettingFragment : Fragment() {
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        val apiClient = IPLocationApiClient()

        var lat: Double = -1.0
        var lng: Double = -1.0
        apiClient.getLocation(object : IPLocationCallback {
            override fun onSuccess(locationData: LocationData) {
                handler.post {
                    view.findViewById<TextView>(R.id.city).text = locationData.city
                    view.findViewById<TextView>(R.id.district).text = locationData.district

                    Log.d("SettingFragment", "$locationData")
                }
            }

            override fun onError(errorMessage: String) {
                // 处理请求失败的情况
            }
        })
        val weatherApiClient = WeatherApiClient()
        weatherApiClient.getRealtimeWeather(lat, lng, object : WeatherCallback {
            override fun onSuccess(weatherData: WeatherData) {
                handler.post {
                    view.findViewById<TextView>(R.id.temperature).text = weatherData.temperature.toString()
                    view.findViewById<TextView>(R.id.humidity).text =
                        String.format("%.0f", (weatherData.humidity * 100))
                    view.findViewById<TextView>(R.id.windDirection).text = weatherData.windDirection.toString()
                    view.findViewById<TextView>(R.id.windSpeed).text = String.format("%.3f", weatherData.windSpeed)
                    view.findViewById<TextView>(R.id.datetime).text = weatherData.datetime
                    Log.d("SettingFragment", "$weatherData")
                }
            }

            override fun onError(errorMessage: String) {
                // 处理请求失败的情况
            }
        })

        return view
    }

}
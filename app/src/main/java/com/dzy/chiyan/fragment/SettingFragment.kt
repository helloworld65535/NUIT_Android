package com.dzy.chiyan.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dzy.chiyan.R
import com.dzy.chiyan.api.*
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.UserDaoImpl
import com.dzy.chiyan.data.UserInfoDaoImpl

class SettingFragment(private val userID: Int) : Fragment() {
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
        val context = requireContext()
        val nickname = UserInfoDaoImpl(DBHelper(context)).getUser(userID)?.nickname
        val username = UserDaoImpl(DBHelper(context)).getUserById(userID)?.username
        view.findViewById<TextView>(R.id.userNickname).text = nickname
        view.findViewById<TextView>(R.id.username).text = username

        view.findViewById<Button>(R.id.forceOffline).setOnClickListener {
            val intent = Intent("com.dzy.chiyan.FORCE_OFFLINE")
            context.sendBroadcast(intent)
        }

        return view
    }

}
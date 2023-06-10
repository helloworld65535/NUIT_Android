package com.dzy.chiyan

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dzy.chiyan.fragment.ChatFragment
import com.dzy.chiyan.fragment.FriendFragment
import com.dzy.chiyan.fragment.SettingFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        //获取传递的id
        val userID = intent.getIntExtra("userID", 0)
        Log.d("UserActivity","$userID")

        loadFragment(ChatFragment(userID))
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chat -> {
                    loadFragment(ChatFragment(userID))
                    true
                }

                R.id.friends -> {
                    loadFragment(FriendFragment(userID))
                    true
                }

                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true
                }

                else -> {
                    false
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}
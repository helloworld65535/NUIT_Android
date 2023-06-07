package com.dzy.chiyan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.dzy.chiyan.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity() {
    lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        loadFragment(ChatFragment())
        bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chat -> {
                    loadFragment(ChatFragment())
                    true
                }
                R.id.friends -> {
                    loadFragment(FriendFragment())
                    true
                }
                R.id.settings -> {
                    loadFragment(SettingFragment())
                    true
                }
                else -> {false}
            }
        }
    }
    private  fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()

//        传递参数START
        // 将字符串值存储在Bundle中
        val username="hello"
        val bundle = Bundle()
        bundle.putString("username", username)

        // 将Bundle传递给FriendFragment
        fragment.arguments = bundle
//        传递参数END


        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}
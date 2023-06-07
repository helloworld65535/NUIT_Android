package com.dzy.chiyan.data

import android.content.SharedPreferences

/**
 * 实现保存配置
 * getSharedPreferences("userData", Context.MODE_PRIVATE)
 */
class SharePreferences(private val sharePreferences: SharedPreferences) {
    companion object {
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
    }

    fun saveUser(user: User) {
        val edit = sharePreferences.edit()
        edit.putString(USERNAME, user.username)
        edit.putString(PASSWORD, user.password)
        edit.apply()
    }

    fun readUser(): User {
        val user = User()
        user.username = sharePreferences.getString(USERNAME, null)
        user.password = sharePreferences.getString(PASSWORD, null)
        return user
    }

    fun deleteUser() {
        val edit = sharePreferences.edit()
        edit.remove(USERNAME)
        edit.remove(PASSWORD)
        edit.apply()
    }
}
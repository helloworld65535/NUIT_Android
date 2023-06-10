package com.dzy.chiyan.data

import android.content.ContentValues
import android.database.Cursor

interface UserInfoDao {
    fun addUserInfo(userInfo: UserInfo): Boolean
    fun deleteUser(userId: Int)
    fun updateUser(userInfo: UserInfo)
    fun getUser(userId: Int): UserInfo?
}

class UserInfoDaoImpl(private val dbHelper: DBHelper) : UserInfoDao {

    override fun addUserInfo(userInfo: UserInfo): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USER_ID, userInfo.userId)
            put(DBHelper.COLUMN_GENDER, userInfo.gender)
            put(DBHelper.COLUMN_BIRTHDAY, userInfo.birthday)
            put(DBHelper.COLUMN_NICKNAME, userInfo.nickname)
        }
        val ret = db.insert(DBHelper.TABLE_USER_INFO, null, values) >= 1L
        db.close()

        return ret
    }

    override fun deleteUser(userId: Int) {
        val db = dbHelper.writableDatabase
        val selection = "${DBHelper.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(userId.toString())
        db.delete(DBHelper.TABLE_USER_INFO, selection, selectionArgs)
        db.close()
    }

    override fun updateUser(userInfo: UserInfo) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_GENDER, userInfo.gender)
            put(DBHelper.COLUMN_BIRTHDAY, userInfo.birthday)
            put(DBHelper.COLUMN_NICKNAME, userInfo.nickname)
        }
        val selection = "${DBHelper.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(userInfo.userId.toString())
        db.update(DBHelper.TABLE_USER_INFO, values, selection, selectionArgs)
        db.close()
    }

    override fun getUser(userId: Int): UserInfo? {
        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            DBHelper.COLUMN_USER_ID,
            DBHelper.COLUMN_GENDER,
            DBHelper.COLUMN_BIRTHDAY,
            DBHelper.COLUMN_NICKNAME
        )
        val selection = "${DBHelper.COLUMN_USER_ID} = ?"
        val selectionArgs = arrayOf(userId.toString())
        val cursor: Cursor? = db.query(
            DBHelper.TABLE_USER_INFO,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        var userInfo: UserInfo? = null
        cursor?.let {
            if (it.moveToFirst()) {
                val gender = it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_GENDER))
                val birthday = it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_BIRTHDAY))
                val nickname = it.getString(it.getColumnIndexOrThrow(DBHelper.COLUMN_NICKNAME))
                userInfo = UserInfo(userId, gender, birthday, nickname)
            }
            it.close()
        }
        db.close()
        return userInfo
    }
}


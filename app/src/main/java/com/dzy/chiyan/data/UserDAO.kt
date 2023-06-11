package com.dzy.chiyan.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor

interface UserDao {
    fun getUserById(id: Int): User?
    fun getIdByUsername(username: String): Int?
    fun addUser(user: User): Boolean
    fun updateUser(user: User): Boolean
    fun deleteUser(user: User)
    fun login(user: User): Boolean

}

class UserDaoImpl(private val dbHelper: DBHelper) : UserDao {

    override fun getUserById(id: Int): User? {
        val db = dbHelper.readableDatabase
        val selection = "${DBHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(id.toString())

        val cursor = db.query(
            DBHelper.TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = getUserFromCursor(cursor)
        }
        cursor.close()

        return user
    }

    @SuppressLint("Range")
    override fun getIdByUsername(username: String): Int? {
        val db = dbHelper.readableDatabase
        val selection = "${DBHelper.COLUMN_USERNAME} = ?"
        val selectionArgs = arrayOf(username)

        val cursor = db.query(
            DBHelper.TABLE_USERS,
            arrayOf(DBHelper.COLUMN_ID),
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        var id: Int? = null
        if (cursor.moveToFirst()) {
            id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID))
        }
        cursor.close()

        return id
    }

    override fun addUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USERNAME, user.username)
            put(DBHelper.COLUMN_PASSWORD, user.password)
        }

        return db.insert(DBHelper.TABLE_USERS, null, values) >= 1L
    }

    override fun updateUser(user: User): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USERNAME, user.username)
            put(DBHelper.COLUMN_PASSWORD, user.password)
        }

        val selection = "${DBHelper.COLUMN_USERNAME}= ?"
        val selectionArgs = arrayOf(user.username.toString())

        return db.update(DBHelper.TABLE_USERS, values, selection, selectionArgs) >= 1L
    }

    override fun deleteUser(user: User) {
        val db = dbHelper.writableDatabase
        val selection = "${DBHelper.COLUMN_ID} = ?"
        val selectionArgs = arrayOf(user.id.toString())

        db.delete(DBHelper.TABLE_USERS, selection, selectionArgs)
    }

    override fun login(user: User): Boolean {
        val db = dbHelper.readableDatabase
        val selection = "${DBHelper.COLUMN_USERNAME} = ? AND ${DBHelper.COLUMN_PASSWORD} = ?"
        val selectionArgs = arrayOf(user.username, user.password)

        val cursor = db.query(
            DBHelper.TABLE_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val hasMatchingUser: Boolean = cursor.moveToFirst()
        cursor.close()

        return hasMatchingUser
    }


    @SuppressLint("Range")
    private fun getUserFromCursor(cursor: Cursor): User {
        val id = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ID))
        val username = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_USERNAME))
        val password = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PASSWORD))

        return User(id, username, password)
    }

}

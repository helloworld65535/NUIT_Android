package com.dzy.chiyan.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.util.Log
import com.dzy.chiyan.adapter.Friend

interface FriendshipDAO {
    fun findFriendById(id: Int): MutableList<Friend>

    fun add(id1: Int, id2: Int): Boolean

    fun delete(id: Int): Boolean
}

class FriendshipDAOImpl(private val dbHelper: DBHelper) : FriendshipDAO {

    override fun add(id1: Int, id2: Int): Boolean {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_USER_ID_FRIENDSHIPS, id1)
            put(DBHelper.COLUMN_FRIEND_ID, id2)
        }
        return db.insert(DBHelper.TABLE_FRIENDSHIPS, null, values) >= 1L
    }

    //TODO 实现删除好友操作
    override fun delete(id: Int): Boolean {
        val db = dbHelper.writableDatabase
        val selection = "${DBHelper.COLUMN_FRIENDSHIP_ID} = ? "
        val selectionArgs = arrayOf(id.toString())
        val rowsDeleted = db.delete(DBHelper.TABLE_FRIENDSHIPS, selection, selectionArgs)
        return rowsDeleted > 0
    }


    @SuppressLint("Range")
    override fun findFriendById(id: Int): MutableList<Friend> {
        val db = dbHelper.readableDatabase

        val query =
            "SELECT friendships.id, user_info.nickname\n" +
                    "FROM friendships\n" +
                    "         JOIN user_info ON (friendships.friend_id = user_info.user_id)\n" +
                    "WHERE friendships.user_id = ${id} OR friendships.friend_id =${id};"


        val cursor = db.rawQuery(query, null)

        Log.d("FriendshipDAOImpl", query)
        val mutableList = mutableListOf<Friend>()

        if (cursor.moveToFirst()) {
            do {
                val userId = cursor.getInt(cursor.getColumnIndex("id"))
                val nickname = cursor.getString(cursor.getColumnIndex("nickname"))
                val friend = Friend(userId, nickname)
                mutableList.add(friend)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return mutableList
    }


}
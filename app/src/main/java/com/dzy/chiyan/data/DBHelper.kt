package com.dzy.chiyan.data


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "chiyan.db"
        private const val DATABASE_VERSION = 1

        // Define table and column names
        const val TABLE_USERS = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"

        const val TABLE_USER_INFO = "user_info"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_GENDER = "gender"
        const val COLUMN_BIRTHDAY = "birthday"
        const val COLUMN_NICKNAME = "nickname"

        const val TABLE_FRIENDSHIPS = "friendships"
        const val COLUMN_FRIENDSHIP_ID = "id"
        const val COLUMN_USER_ID_FRIENDSHIPS = "user_id"
        const val COLUMN_FRIEND_ID = "friend_id"

        const val TABLE_MESSAGES = "messages"
        const val COLUMN_MESSAGE_ID = "id"
        const val COLUMN_SENDER_ID = "sender_id"
        const val COLUMN_RECEIVER_ID = "receiver_id"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_SENT_AT = "sent_at"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create tables when the database is first created
        val createUserTable = "CREATE TABLE $TABLE_USERS " +
                "($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " + // Modify primary key with AUTOINCREMENT
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT," +
                "UNIQUE($COLUMN_USERNAME))"

        val createUserInfoTable = "CREATE TABLE $TABLE_USER_INFO " +
                "($COLUMN_USER_ID INTEGER PRIMARY KEY, " + // Modify primary key
                "$COLUMN_GENDER TEXT, " +
                "$COLUMN_BIRTHDAY DATE, " +
                "$COLUMN_NICKNAME TEXT, " +
                "FOREIGN KEY($COLUMN_USER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE)" // Add foreign key constraint

        val createFriendshipsTable = "CREATE TABLE $TABLE_FRIENDSHIPS " +
                "($COLUMN_FRIENDSHIP_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USER_ID_FRIENDSHIPS INTEGER, " +
                "$COLUMN_FRIEND_ID INTEGER, " +
                "FOREIGN KEY($COLUMN_USER_ID_FRIENDSHIPS) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE, " +
                "FOREIGN KEY($COLUMN_FRIEND_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE, " +
                "UNIQUE($COLUMN_USER_ID_FRIENDSHIPS, $COLUMN_FRIEND_ID), " +
                "CHECK($COLUMN_USER_ID_FRIENDSHIPS <> $COLUMN_FRIEND_ID))"

        val createMessagesTable = "CREATE TABLE $TABLE_MESSAGES " +
                "($COLUMN_MESSAGE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_SENDER_ID INTEGER, " +
                "$COLUMN_RECEIVER_ID INTEGER, " +
                "$COLUMN_CONTENT TEXT, " +
                "$COLUMN_SENT_AT DATETIME, " +
                "FOREIGN KEY($COLUMN_SENDER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE, " +
                "FOREIGN KEY($COLUMN_RECEIVER_ID) REFERENCES $TABLE_USERS($COLUMN_ID) ON DELETE CASCADE)"

        db.execSQL(createUserTable)
        db.execSQL(createUserInfoTable)
        db.execSQL(createFriendshipsTable)
        db.execSQL(createMessagesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Handle database upgrade if needed
        // This method is called when DATABASE_VERSION is increased
    }
}




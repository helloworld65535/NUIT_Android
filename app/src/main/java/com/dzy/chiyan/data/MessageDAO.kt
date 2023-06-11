package com.dzy.chiyan.data

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.Cursor
import com.dzy.chiyan.adapter.Chat

interface MessageDAO {
    fun insertMessage(message: Message): Boolean
    fun getAllMessages(): List<Message>
    fun getMessagesBySenderReceiver(senderId: Int, receiverId: Int): List<Message>
}


class MessageDAOImpl(private val dbHelper: DBHelper) : MessageDAO {
    @SuppressLint("Range")
    fun getChatsForUserId(userId: Int): List<Chat> {
        val db = dbHelper.readableDatabase
        val chats = mutableListOf<Chat>()

        val query =
            "SELECT\n" +
                    "    sub.object_id,\n" +
                    "    sub.nickname,\n" +
                    "    sub.content,\n" +
                    "    sub.sent_at\n" +
                    "FROM\n" +
                    "    (\n" +
                    "        SELECT\n" +
                    "            CASE\n" +
                    "                WHEN sender_id = ${userId} THEN receiver_id\n" +
                    "                ELSE sender_id\n" +
                    "                END AS object_id,\n" +
                    "            user_info.nickname,\n" +
                    "            messages.content,\n" +
                    "            messages.sent_at\n" +
                    "        FROM\n" +
                    "            messages\n" +
                    "                JOIN user_info ON (CASE WHEN sender_id = ${userId} THEN receiver_id ELSE sender_id END = user_info.user_id)\n" +
                    "        WHERE\n" +
                    "                sender_id = ${userId} OR receiver_id = ${userId}\n" +
                    "        ORDER BY messages.sent_at ASC\n" +
                    "    ) AS sub\n" +
                    "GROUP BY sub.object_id"


        val cursor: Cursor? = db.rawQuery(query, null)

        cursor?.use {
            while (cursor.moveToNext()) {
                val friendId = cursor.getInt(cursor.getColumnIndex("object_id"))
                val name = cursor.getString(cursor.getColumnIndex("nickname"))
                val message = cursor.getString(cursor.getColumnIndex("content"))
                val time = cursor.getString(cursor.getColumnIndex("sent_at"))

                val chat = Chat(friendId, name, message, time)
                chats.add(chat)
            }
        }

        return chats
    }

    @SuppressLint("Range")
    override fun getMessagesBySenderReceiver(id1: Int, id2: Int): List<Message> {
        val db = dbHelper.readableDatabase
        val selection =
            "(${DBHelper.COLUMN_SENDER_ID} = ? AND ${DBHelper.COLUMN_RECEIVER_ID} = ?) OR (${DBHelper.COLUMN_SENDER_ID}  = ? AND  ${DBHelper.COLUMN_RECEIVER_ID} = ?)"
        val selectionArgs =
            arrayOf(id1.toString(), id2.toString(), id2.toString(), id1.toString())
        val sortOrder = "${DBHelper.COLUMN_SENT_AT} ASC"

        val cursor = db.query(
            DBHelper.TABLE_MESSAGES,
            null,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )

        val messageList = mutableListOf<Message>()

        cursor.use {
            while (cursor.moveToNext()) {
                val messageId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_MESSAGE_ID))
                val senderId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SENDER_ID))
                val receiverId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_RECEIVER_ID))
                val content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT))
                val sentAt = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SENT_AT))
                val message = Message(messageId, senderId, receiverId, content, sentAt)
                messageList.add(message)
            }
        }

        return messageList
    }

    /// 添加消息数据
    override fun insertMessage(message: Message): Boolean {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DBHelper.COLUMN_SENDER_ID, message.senderId)
            put(DBHelper.COLUMN_RECEIVER_ID, message.receiverId)
            put(DBHelper.COLUMN_CONTENT, message.content)
            put(DBHelper.COLUMN_SENT_AT, message.sentAt)
        }

        return db.insert(DBHelper.TABLE_MESSAGES, null, values) >= 1L
    }

    ///获取所有消息数据
    @SuppressLint("Range")
    override fun getAllMessages(): List<Message> {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_MESSAGES,
            null,
            null,
            null,
            null,
            null,
            null
        )

        val messageList = mutableListOf<Message>()

        cursor.use {
            while (cursor.moveToNext()) {
                val messageId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_MESSAGE_ID))
                val senderId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_SENDER_ID))
                val receiverId = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_RECEIVER_ID))
                val content = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_CONTENT))
                val sentAt = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_SENT_AT))
                val message = Message(messageId, senderId, receiverId, content, sentAt)
                messageList.add(message)
            }
        }

        return messageList
    }

}

package com.dzy.chiyan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.data.Message
import java.text.SimpleDateFormat
import java.util.*


class MessageAdapter(
    private val messageList: MutableList<Message>,
    private val userID: Int,
    private val userName: String,
    private val friendName: String
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // 定义不同的 View 类型
    private val MESSAGE_SENT_TYPE = 0
    private val MESSAGE_RECEIVED_TYPE = 1

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (message.senderId == userID) {
            MESSAGE_SENT_TYPE
        } else {
            MESSAGE_RECEIVED_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            MESSAGE_SENT_TYPE -> {
                val msgSentView = inflater.inflate(R.layout.item_message_sent, parent, false)
                SentMessageViewHolder(msgSentView)
            }

            MESSAGE_RECEIVED_TYPE -> {
                val msgReceivedView = inflater.inflate(R.layout.item_message_received, parent, false)
                ReceivedMessageViewHolder(msgReceivedView)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageList[position]

        when (holder.itemViewType) {
            MESSAGE_SENT_TYPE -> {
                val sentHolder = holder as SentMessageViewHolder
                // 设置用户发送的消息样式
                sentHolder.bind(message)
            }

            MESSAGE_RECEIVED_TYPE -> {
                val receivedHolder = holder as ReceivedMessageViewHolder
                // 设置其他用户发送的消息样式
                receivedHolder.bind(message)
            }
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    // ViewHolder 类的定义
    inner class SentMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val msgContentView: TextView = itemView.findViewById(R.id.textView_messageContent_S)
        private val msgDateView: TextView = itemView.findViewById(R.id.textView_datetime_S)
        private val msgNickname: TextView = itemView.findViewById(R.id.textView_nickname_S)
        fun bind(message: Message) {
            // 设置用户发送的消息数据和样式
            msgNickname.text = userName
            msgContentView.text = message.content
            msgDateView.text = convertUnixTimestampToString(message.sentAt)
            Log.d("SentMessageViewHolder", "message.senderId:${message.senderId}")
        }
    }


    inner class ReceivedMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val msgContentView: TextView = itemView.findViewById(R.id.textView_messageContent_R)
        private val msgDateView: TextView = itemView.findViewById(R.id.textView_datetime_R)
        private val msgNickname: TextView = itemView.findViewById(R.id.textView_nickname_R)
        fun bind(message: Message) {
            // 设置用户发送的消息数据和样式
            msgNickname.text = friendName
            msgContentView.text = message.content
            msgDateView.text = convertUnixTimestampToString(message.sentAt)
            Log.d("ReceivedMessageViewHolder", "message.senderId:${message.senderId}")
        }
    }

    fun convertUnixTimestampToString(unixTimestamp: String): String {
        try {
            val timestampLong = unixTimestamp.toLong() * 1000
            val date = Date(timestampLong)
            val currentDate = Date()

            val sameDay = date.year == currentDate.year &&
                    date.month == currentDate.month &&
                    date.date == currentDate.date

            val dateFormat = if (sameDay) {
                SimpleDateFormat("HH:mm", Locale.getDefault())
            } else {
                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            }

            return dateFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun addMessage(message: Message) {
        messageList.add(message)
        notifyItemInserted(messageList.size - 1)
    }

    fun reloadData(newFriendList: List<Message>) {
        messageList.clear()
        messageList.addAll(newFriendList)
        notifyDataSetChanged()
    }
}

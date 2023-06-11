package com.dzy.chiyan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.ChatActivity
import com.dzy.chiyan.R
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(private val chatList: MutableList<Chat>,val userID: Int) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private lateinit var context: Context

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nicknameTextView: TextView = itemView.findViewById(R.id.nicknameTextView)
        val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = chatList[position]

        // 设置昵称、最新消息和时间
        holder.nicknameTextView.text = chat.name
        holder.messageTextView.text = chat.message
        holder.timeTextView.text = convertUnixTimestampToString(chat.time)

        holder.itemView.setOnClickListener {
            //跳转用户聊天界面
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("friendId", chat.friendId)
            intent.putExtra("userID", userID)
            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            // 显示删除会话的弹窗或执行其他操作
            showDeleteConfirmationDialog(holder.adapterPosition)
            true
        }
    }

    private fun convertUnixTimestampToString(unixTimestamp: String): String {
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

    override fun getItemCount(): Int {
        return chatList.size
    }

    private fun showDeleteConfirmationDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("删除会话")
            .setMessage("确定要删除该会话吗？")
            .setPositiveButton("确定") { dialog, which ->
                // 执行删除会话的操作，例如删除对应的数据
                chatList.removeAt(position)
                notifyItemRemoved(position)
            }
            .setNegativeButton("取消", null)
            .create()

        alertDialog.show()
    }


    fun addChat(chat: Chat) {
        chatList.add(chat)
        notifyItemInserted(chatList.size - 1)
    }

    fun reloadData(newFriendList: List<Chat>) {
        chatList.clear()
        chatList.addAll(newFriendList)
        notifyDataSetChanged()
    }

}

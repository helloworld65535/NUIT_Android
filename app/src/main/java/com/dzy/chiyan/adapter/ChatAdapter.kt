package com.dzy.chiyan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R

class ChatAdapter(private val chatList: MutableList<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
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
        holder.timeTextView.text = chat.time

        holder.itemView.setOnClickListener {
            //TODO 跳转聊天界面
//            val intent = Intent(context, MainActivity::class.java)
//            intent.putExtra("friendId", chatList[holder.adapterPosition].friendId)
//            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            // 显示删除会话的弹窗或执行其他操作
            showDeleteConfirmationDialog(holder.adapterPosition)
            true
        }
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


}

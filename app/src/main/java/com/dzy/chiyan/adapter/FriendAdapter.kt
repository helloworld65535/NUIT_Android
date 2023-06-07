package com.dzy.chiyan.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.MainActivity
import com.dzy.chiyan.R

class FriendAdapter(private val friendList: MutableList<Friend>) : RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
    private lateinit var context: Context

    class FriendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_friend, parent, false)
        return FriendViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friend = friendList[position]

        holder.nameTextView.text = friend.name

        //点击item传入friendId然后跳转
        holder.itemView.setOnClickListener {
            //TODO 跳转用户详情界面
//            val intent = Intent(context, MainActivity::class.java)
//            intent.putExtra("friendId", friend.id)
//            context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener {
            // 弹出删除确认对话框
            showDeleteConfirmationDialog(holder.adapterPosition)
            true
        }
    }

    override fun getItemCount() = friendList.size

    private fun showDeleteConfirmationDialog(position: Int) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("删除好友")
            .setMessage("确定要删除该好友吗？")
            .setPositiveButton("确定") { dialog, which ->
                // 删除对应的数据
                friendList.removeAt(position)

                //TODO 删除数据库中对应的好友信息

                // 刷新适配器
                notifyItemRemoved(position)
            }
            .setNegativeButton("取消", null)
            .create()

        alertDialog.show()
    }


}



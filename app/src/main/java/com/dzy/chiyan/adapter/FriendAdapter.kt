package com.dzy.chiyan.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.FriendshipDAOImpl

class FriendAdapter(private val friendList: MutableList<Friend>) :
    RecyclerView.Adapter<FriendAdapter.FriendViewHolder>() {
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

                val del = friendList[position].friendshipID
                friendList.removeAt(position)
                notifyItemRemoved(position)

                //TODO 删除数据库中对应的好友信息
                val dao = FriendshipDAOImpl(DBHelper(context))
                val msg: String
                if (dao.delete(del)) {
                    msg = "删除成功"
                } else {
                    msg = "删除失败"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()

                // 刷新适配器
            }
            .setNegativeButton("取消", null)
            .create()

        alertDialog.show()
    }

    /**
     * 添加数据
     */
    fun addFriend(friend: Friend) {
        friendList.add(friend)
        notifyItemInserted(friendList.size - 1)
    }

    /**
     * 重新加载数据
     */
    fun reloadData(newFriendList: List<Friend>) {
        friendList.clear()
        friendList.addAll(newFriendList)
        notifyDataSetChanged()
    }
}



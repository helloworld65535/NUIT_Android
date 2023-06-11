package com.dzy.chiyan.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.adapter.Friend
import com.dzy.chiyan.adapter.FriendAdapter
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.FriendshipDAOImpl
import com.dzy.chiyan.data.UserDaoImpl


class FriendFragment(private val userID: Int) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendAdapter
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_friend, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)

        // 获取 ImageButton 对象
        val addFriendButton: ImageButton = view.findViewById(R.id.imageBut_addFriend)
        // 设置点击事件监听器
        addFriendButton.setOnClickListener {
            onAddFriend()
        }

        // 设置RecyclerView的布局管理器
        context = requireContext()
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        // 初始化适配器
        adapter = FriendAdapter(mutableListOf<Friend>(), userID) // 替换为你的适配器类
        recyclerView.adapter = adapter

        //  在这里设置适配器的数据源
        loadData()


        return view
    }

    private fun loadData() {
        // 加载用户的好友列表
        val dao = FriendshipDAOImpl(DBHelper(context))
        adapter.reloadData(dao.findFriendById(userID))
    }

    /**
     * 添加好友的按钮点击事件
     */
    private fun onAddFriend() {
        val alertDialog = AlertDialog.Builder(context)
        val input = EditText(context)
        alertDialog.setView(input)
        alertDialog.setTitle("添加好友")
        alertDialog.setMessage("请输入好友的账号")

        alertDialog.setPositiveButton("确认") { dialog, which ->
            val friendName = input.text.toString()
            if (friendName.matches(Regex("\\d{11}"))) {
                val db = DBHelper(context)
                // 在这里执行添加好友的逻辑
                val friendshipDAOImpl = FriendshipDAOImpl(db)
                val user = UserDaoImpl(db)
                val friendID = user.getIdByUsername(friendName) ?: 0

                val msg: String
                if (friendshipDAOImpl.add(userID, friendID)) {
                    msg = "添加成功"
                    loadData()
                } else {
                    msg = "添加失败"
                }
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            } else {
                // 格式验证不通过
                Toast.makeText(context, "账号输入错误，请检查你的输入的账号格式", Toast.LENGTH_SHORT).show()
            }
        }
        alertDialog.setNegativeButton("取消") { dialog, which ->
            dialog.cancel()
        }
        alertDialog.show()
    }


}
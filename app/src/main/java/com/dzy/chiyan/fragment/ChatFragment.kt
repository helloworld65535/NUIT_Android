package com.dzy.chiyan.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.adapter.Chat
import com.dzy.chiyan.adapter.ChatAdapter
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.MessageDAOImpl


class ChatFragment(var userID: Int) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private lateinit var context: Context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        context = requireContext()

        recyclerView = view.findViewById(R.id.recyclerView)

        // 设置RecyclerView的布局管理器
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        // 初始化适配器
        adapter = ChatAdapter(mutableListOf<Chat>(), userID) // 替换为你的适配器类
        recyclerView.adapter = adapter

        // TODO 在这里设置适配器的数据源
        loadData()

        return view
    }

    private fun loadData() {
        //TODO 加载用户的聊天
        val dao = MessageDAOImpl(DBHelper(context))
        adapter.reloadData(dao.getChatsForUserId(userID))
    }

    override fun onResume() {
        super.onResume()
        // 在界面返回时重新加载数据
        loadData()
    }
}
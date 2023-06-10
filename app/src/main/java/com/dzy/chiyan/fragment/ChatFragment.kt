package com.dzy.chiyan.fragment

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


class ChatFragment(var userID: Int) : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ChatAdapter
    private val chats = mutableListOf<Chat>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)


        //TODO 在这里使用data字符串值进行操作

        recyclerView = view.findViewById(R.id.recyclerView)

        // 设置RecyclerView的布局管理器
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager


        // TODO 在这里设置适配器的数据源
        loadData()

        // 初始化适配器
        adapter = ChatAdapter(chats) // 替换为你的适配器类
        recyclerView.adapter = adapter

        return view
    }

    private fun loadData() {
        //TODO 加载用户的聊天
        chats.add(Chat(1, "Friend 1", "Hello!", "10:00 AM"))
        chats.add(Chat(2, "Friend 2", "Hi there!", "11:30 AM"))
        chats.add(Chat(3, "Friend 3", "Hey!", "12:45 PM"))

    }
}
package com.dzy.chiyan.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.adapter.Friend
import com.dzy.chiyan.adapter.FriendAdapter


class FriendFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FriendAdapter // 替换为你的适配器类
    private var friends = mutableListOf<Friend>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_friend, container, false)

        // 获取传递的字符串值
        val data = arguments?.getString("data")
        //TODO 在这里使用data字符串值进行操作


        recyclerView = rootView.findViewById(R.id.recyclerView)

        // 设置RecyclerView的布局管理器
        val layoutManager = LinearLayoutManager(requireContext())
        recyclerView.layoutManager = layoutManager


        // TODO 在这里设置适配器的数据源
        loadData()

        // 初始化适配器
        adapter = FriendAdapter(friends) // 替换为你的适配器类
        recyclerView.adapter = adapter

        return rootView
    }

    private fun loadData() {
        friends.add(Friend("张三", 1))
        friends.add(Friend("李四", 2))
        friends.add(Friend("王五", 3))
        friends.add(Friend("赵六", 3))
        friends.add(Friend("孙八", 3))
    }
}
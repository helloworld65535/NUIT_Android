package com.dzy.chiyan.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dzy.chiyan.R
import com.dzy.chiyan.adapter.MessageAdapter
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.Message
import com.dzy.chiyan.data.MessageDAOImpl
import com.dzy.chiyan.data.UserInfoDaoImpl

class ChatActivity : BaseActivity() {

    private lateinit var messageAdapter: MessageAdapter
    private lateinit var recyclerView: RecyclerView
    private var userID = 0
    private var friendId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        userID = intent.getIntExtra("userID", 0)
        friendId = intent.getIntExtra("friendId", 0)

        Log.d("ChatActivity", "userID:$userID friendId:$friendId")
        val dao = UserInfoDaoImpl(DBHelper(this))
        val contactView = findViewById<TextView>(R.id.text_contact_people)
        contactView.text = dao.getUser(friendId)?.nickname

        val userName = dao.getUser(userID)?.nickname!!
        val friendName = dao.getUser(friendId)?.nickname!!

        // 初始化 RecyclerView
        recyclerView = findViewById(R.id.recyclerView_messages)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 创建适配器实例
        messageAdapter = MessageAdapter(mutableListOf<Message>(), userID, userName, friendName)

        loadData()

        // 将适配器应用到 RecyclerView
        recyclerView.adapter = messageAdapter
    }

    private fun loadData() {
        val dao = MessageDAOImpl(DBHelper(this))
        messageAdapter.reloadData(dao.getMessagesBySenderReceiver(userID, friendId))
        // 滚动到底部
        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount())
    }

    fun onBack(view: View) {
        finish()
    }

    fun onSendMessage(view: View) {
        //获取输入的消息内容
        val msg_Text = findViewById<TextView>(R.id.edit_message)
        val msg_content = msg_Text.text.toString()
        msg_Text.text = ""
        if (msg_content.isEmpty()) return
        val sentAt = (System.currentTimeMillis() / 1000).toString()
        val message = Message(0, userID, friendId, msg_content, sentAt)
        val dao = MessageDAOImpl(DBHelper(this))
        if (dao.insertMessage(message)) {
            messageAdapter.addMessage(message)

            // 滚动到底部
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1)

            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "添加失败", Toast.LENGTH_SHORT).show()
        }
    }
}
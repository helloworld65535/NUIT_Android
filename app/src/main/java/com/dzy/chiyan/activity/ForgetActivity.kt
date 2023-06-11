package com.dzy.chiyan.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.dzy.chiyan.R
import com.dzy.chiyan.data.DBHelper
import com.dzy.chiyan.data.User
import com.dzy.chiyan.data.UserDaoImpl

class ForgetActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget)
    }

    fun onForget(view: View) {
        val username = findViewById<EditText>(R.id.edit_username).text.toString()
        val password = findViewById<EditText>(R.id.edit_password).text.toString()
        val confirmPassword =
            findViewById<EditText>(R.id.edit_confirm_password).text.toString()

        val regex = "\\d+".toRegex()
        val usernameValid = (username.length == 11) && regex.matches(username)
        val passwordValid = password.length in 8..12
        val confirmPasswordValid = (confirmPassword == password)

        Log.d("ForgetActivity","username:$username password:$password confirmPassword:$confirmPassword")
        // 数据合法则修改密码
        if (usernameValid && passwordValid && confirmPasswordValid) {
            val userDao = UserDaoImpl(DBHelper(this))
            val user = User(null, username, password)
            val res = userDao.updateUser(user)
            Log.d("ForgetActivity","user:$user res:$res")
            val builder = AlertDialog.Builder(this)
            if (res) {
                //添加成功，跳出提示
                builder.setTitle("修改密码成功")
                builder.setMessage("账号：${user.username},修改密码成功!")
                builder.setPositiveButton("确认") { _, _ ->
                    finish()
                }
            } else {
                builder.setTitle("修改密码失败")
                builder.setMessage("修改密码失败！")
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(this, "修改密码失败,请检查信息是否符合要求", Toast.LENGTH_SHORT).show()
        }
    }

    fun onCancel(view: View) {
        finish()
    }
}
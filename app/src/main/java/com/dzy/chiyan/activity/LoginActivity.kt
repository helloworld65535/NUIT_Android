package com.dzy.chiyan.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.dzy.chiyan.R
import com.dzy.chiyan.data.*

class LoginActivity : BaseActivity() {
    private lateinit var sharePreferences: SharePreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharePreferences = SharePreferences(getSharedPreferences("userData", Context.MODE_PRIVATE))
        //读取是否保存账号信息
        val user = sharePreferences.readUser()
        if (user.canLogin()) {
            findViewById<CheckBox>(R.id.checkBox_remember).isChecked = true
            findViewById<EditText>(R.id.edit_username).setText(user.username)
            findViewById<EditText>(R.id.edit_password).setText(user.password)
        }
    }

    /**
     * 登录按钮的点击事件
     */
    fun onLogin(view: View) {

        val userDao = UserDaoImpl(DBHelper(this))
        val user = getLoginAccount()

        user.id = userDao.getIdByUsername(user.username!!)
        if (userDao.login(user)) {
            // 登录成功逻辑
            Log.d("LoginActivity", "登录成功 ${user.id}")
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show()
            //更新本地账号信息
            updateAccountData()
            // 跳转到用户界面,并且传递当前的用户ID
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("userID", user.id)
            startActivity(intent)
            finish()
        } else {
            // 登录失败逻辑
            Log.d("LoginActivity", "登录失败，请检查账号和密码")
            Toast.makeText(this, "登录失败，请检查账号和密码", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 获取输入框中的账号信息
     * @return User
     */
    private fun getLoginAccount(): User {
        val username = findViewById<EditText>(R.id.edit_username).text.toString()
        val password = findViewById<EditText>(R.id.edit_password).text.toString()
        return User(null, username, password)
    }

    /**
     * @return 是否勾选记住密码
     */
    private fun isRememberChecked(): Boolean {
        return findViewById<CheckBox>(R.id.checkBox_remember).isChecked
    }

    /**
     * 根据是否勾选记住密码来更新本地账号信息
     */
    private fun updateAccountData() {
        if (isRememberChecked()) {
            val user = getLoginAccount()
            sharePreferences.saveUser(user)
        } else {
            sharePreferences.deleteUser()
        }
    }

    //注册账号的点击事件
    fun onRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    fun onForget(view: View) {
        val intent = Intent(this, ForgetActivity::class.java)
        startActivity(intent)
    }
}


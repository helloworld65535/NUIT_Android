package com.dzy.chiyan


import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.dzy.chiyan.data.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<RadioButton>(R.id.radiobutton_male).isChecked = true
    }


    fun onRegister(view: View) {
        // 获取输入的数据
        val username = findViewById<EditText>(R.id.edit_username).text.toString()
        val password = findViewById<EditText>(R.id.edit_password).text.toString()
        val confirmPassword =
            findViewById<EditText>(R.id.edit_confirm_password).text.toString()
        val nickname = findViewById<EditText>(R.id.edit_nickname).text.toString()

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup_gender)
        val gender =
            radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId).text.toString()
        val birthday = findViewById<Button>(R.id.button_DateSelect).text.toString()
        Log.d(
            "Register", "username:${username}" +
                    "password:${password}" +
                    "confirmPassword:${confirmPassword}" +
                    "nickname:${nickname}" +
                    "gender:${gender}" +
                    "birthday:${birthday}"
        )

        // 验证输入
        val regex = "\\d+".toRegex()
        val usernameValid = (username.length == 11) && regex.matches(username)
        val passwordValid = password.length in 8..12
        val confirmPasswordValid = (confirmPassword == password)
        val nicknameValid = nickname.length in 1..20

        // 数据合法则进行注册
        if (usernameValid && passwordValid && confirmPasswordValid && nicknameValid) {
            val dbHelper = DBHelper(this)
            val userDao = UserDaoImpl(dbHelper)
            //在数据库中添加账号信息
            val user = User(null, username, password)
            var res = userDao.addUser(user)
            if (res) {
                //添加用户信息
                val userInfo = UserInfo(userDao.getIdByUsername(username), gender, birthday, nickname)
                Log.d("userInfo", "${userInfo.userId}\t ${userInfo.gender}\t")
                val userInfoDao = UserInfoDaoImpl(dbHelper)
                res = res and userInfoDao.addUserInfo(userInfo)

            }
            dbHelper.close()

            val builder = AlertDialog.Builder(this)
            if (res) {
                //添加成功，跳出提示
                builder.setTitle("注册成功")
                builder.setMessage("账号：${user.username},注册成功!")
                builder.setPositiveButton("确认") { _, _ ->
                    finish()
                }
            } else {
                builder.setTitle("注册失败")
                builder.setMessage("账号已被注册")
            }
            val dialog = builder.create()
            dialog.show()
        } else {
            Toast.makeText(this, "注册失败,请检查注册信息是否符合要求", Toast.LENGTH_SHORT).show()
        }
    }


    fun onCancel(view: View) {
        finish()
    }

    fun onDateSelector(view: View) {
        view as Button
        val calendar: Calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                // 更新TextView来显示选择的日期
                val dateText = "${year}-${monthOfYear+1}-${dayOfMonth}"
                view.text = dateText
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
}

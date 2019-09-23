package com.example.todoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.loginField
import kotlinx.android.synthetic.main.activity_login.password
import kotlinx.android.synthetic.main.activity_login.registerButton
import android.widget.*


class SignInActivity : AppCompatActivity() {

    fun showToast(message: String){
        val toast = Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
        val v = toast.getView().findViewById(android.R.id.message) as TextView
        v.gravity = Gravity.CENTER
        toast.show()
    }

    fun isEmpty(): Boolean {
        return (password.text.isBlank() || loginField.text.isBlank())
    }

    fun saveLogin(login: String?) {
        val sharedPreference = getSharedPreferences("com.example.todoapp.prefs", 0)
        var editor = sharedPreference.edit()
        editor.putString("login", login)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener(){
            if(isEmpty()){
                showToast("Żadne z pól nie może pozostać puste!")
            } else {
                val response = serveCredentialsAsync(loginField.text.toString(), password.text.toString(), "login").execute().get()
                if(response == "OK"){
                    saveLogin(loginField.text.toString())
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("USERNAME", loginField.text.toString())
                    loginField.getText().clear()
                    password.getText().clear()
                    startActivity(intent)
                } else {
                    showToast("Niepoprawna nazwa użytkownika lub hasło!")
                }
            }
        }

        registerButton.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }
}

package com.example.randomgame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.text.TextUtils.isEmpty
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.loginField
import kotlinx.android.synthetic.main.activity_sign_in.password
import kotlinx.android.synthetic.main.activity_sign_in.registerButton

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginButton.setOnClickListener(){
            if(isEmpty()){
                showToast("Żadne z pól nie może pozostać puste!")
            } else {
                val response = serveCredentialsAsync(loginField.text.toString(), password.text.toString(), "login").execute().get()
                if(response == "OK"){
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

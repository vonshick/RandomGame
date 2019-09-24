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

    private fun showToast(message: String){
        val toast = Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        )
        val v = toast.getView().findViewById(android.R.id.message) as TextView
        v.gravity = Gravity.CENTER
        toast.show()
    }

    private fun isEmpty(): Boolean {
        return (password.text.isBlank() || loginField.text.isBlank())
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
                    val intent = Intent(this, TaskListActivity::class.java)
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

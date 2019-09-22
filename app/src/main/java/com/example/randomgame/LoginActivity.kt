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
import kotlinx.android.synthetic.main.activity_sign_in.*
import kotlinx.android.synthetic.main.activity_sign_in.loginField
import kotlinx.android.synthetic.main.activity_sign_in.password

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

    fun extractPassword(rawCredentials: String): String {
            val preparedList = rawCredentials.split(",".toRegex())
            val extractedPassword = preparedList[1]
                .replace("password", "")
                .replace("\"", "")
                .replace("}", "")
                .replace(":", "")
            return extractedPassword
    }

    fun checkCredentials() : Boolean {
        val rawCredentials = getCredentialsAsync(loginField.text.toString()).execute().get().toString()
        if (rawCredentials != "{}") {
            val extractedPassword = extractPassword(rawCredentials)
            if (extractedPassword == password.text.toString()){
                return true
            } else {
                return false
            }
        } else {
            return false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        loginButton.setOnClickListener(){

            if(isEmpty()){
                showToast("Żadne z pól nie może pozostać puste!")
            } else {
                if(checkCredentials()){
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

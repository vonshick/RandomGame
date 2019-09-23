package com.example.randomgame
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.loginField
import kotlinx.android.synthetic.main.activity_registration.password
import kotlinx.android.synthetic.main.activity_registration.registerButton
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import java.net.URL


class RegistrationActivity : AppCompatActivity() {
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

    private fun saveCredentials(){
        val response = serveCredentialsAsync(loginField.text.toString(), password.text.toString(), "register").execute().get()
        if(response == "OK"){
            loginField.getText().clear()
            password.getText().clear()
            confirmation.getText().clear()
            showToast("Nowy użytkownik utworzony!")
        } else {
            showToast("Użytkownik o danej nazwie już istnieje")
        }

    }

    private fun isEmpty(): Boolean {
        return (password.text.isBlank() || confirmation.text.isBlank() || loginField.text.isBlank())
    }

    private fun passwordsEqual():Boolean{
        return password.text.toString().equals(confirmation.text.toString())
    }

    private fun validate(): Boolean {
        if (isEmpty()) {
            showToast("Żadne pole nie może być puste!")
            return (false)
        } else if (!passwordsEqual()) {
            showToast("Hasła nie są takie same!")
            return (false)
        } else {
            return (true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        registerButton.setOnClickListener {
            if(validate()){
                saveCredentials()
            }
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}


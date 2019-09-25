package com.example.notes
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.support.v7.app.AlertDialog
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.loginField
import kotlinx.android.synthetic.main.activity_registration.password
import kotlinx.android.synthetic.main.activity_registration.registerButton
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView


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

    private fun redundantUserDialog(){
        val builder = AlertDialog.Builder(this@RegistrationActivity)
        builder.setTitle("Error")
        builder.setMessage("User already exists!")
        builder.setPositiveButton("OK") { _, _ -> }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun saveCredentials(){
        val response = serveCredentialsAsync(loginField.text.toString(), password.text.toString(), "register").execute().get()
        if(response == "OK"){
            loginField.getText().clear()
            password.getText().clear()
            confirmation.getText().clear()
            showToast("New user registered")
        } else {
            redundantUserDialog()
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
            showToast("None of fields can be empty!")
            return (false)
        } else if (!passwordsEqual()) {
            showToast("Passwords are not equal!")
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

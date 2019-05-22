package com.example.randomgame

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.loginField
import kotlinx.android.synthetic.main.activity_registration.password
import kotlinx.android.synthetic.main.activity_registration.registerButton
import kotlinx.android.synthetic.main.activity_sign_in.*
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView

class Credentials(username: String, password: String) {
    var username: String? = username
    var password: String? = password
}

class CredentialsDBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " + TABLE_NAME + "(" + USERNAME_COLUMN_NAME +
                " TEXT PRIMARY KEY," + PASSWORD_COLUMN_NAME + " TEXT" + ")")
        Log.d("Magic", CREATE_PRODUCTS_TABLE)
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addResult(credentials: Credentials) {
        val values = ContentValues()
        values.put(USERNAME_COLUMN_NAME, credentials.username)
        values.put(PASSWORD_COLUMN_NAME, credentials.password)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllResults(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
    fun deleteAllResults() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "randomGame5.db"
        val TABLE_NAME = "credentials"
        val USERNAME_COLUMN_NAME = "username"
        val PASSWORD_COLUMN_NAME = "password"
    }
}


class RegistrationActivity : AppCompatActivity() {
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

    fun saveCredentialsToDb(){
        val dbHandler = CredentialsDBOpenHelper(this, null)
        dbHandler.addResult(Credentials(loginField.text.toString(), password.text.toString()))
        loginField.getText().clear()
        password.getText().clear()
        confirmation.getText().clear()
        showToast("Nowy użytkownik utworzony!")
    }


    fun isEmpty(): Boolean {
        return (password.text.isBlank() || confirmation.text.isBlank() || loginField.text.isBlank())
    }

    fun passwordsEqual():Boolean{
        return password.text.toString().equals(confirmation.text.toString())
    }

    fun validate(): Boolean {
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
                saveCredentialsToDb()
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

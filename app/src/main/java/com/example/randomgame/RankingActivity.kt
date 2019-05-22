package com.example.randomgame

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.BaseColumns
import android.view.View
import android.support.v4.app.NavUtils
import android.util.JsonReader
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ranking.*
import org.json.JSONStringer
import java.io.StringReader
import java.net.URL
import android.widget.ArrayAdapter
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.view.Gravity
import android.widget.TextView
import java.lang.Boolean.TRUE


class Result(var id: Int, userName: String, result: String) {
    var userName: String? = userName
    var result: String? = result
}

class RecordsDBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," +
                USERNAME_COLUMN_NAME
                + " TEXT," +
                RESULT_COLUMN_NAME
                + " TEXT" + ")")
//        val CREATE_PRODUCTS_TABLE = "CREATE TABLE records(_id INTEGER PRIMARY KEY, username TEXT, result TEXT)"
        Log.d("Magic", CREATE_PRODUCTS_TABLE)
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addResult(result: Result) {
        val values = ContentValues()
        values.put(COLUMN_ID, result.id)
        values.put(USERNAME_COLUMN_NAME, result.userName)
        values.put(RESULT_COLUMN_NAME, result.result)
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
        private val DATABASE_NAME = "randomGame4.db"
        val TABLE_NAME = "records"
        val COLUMN_ID = "_id"
        val USERNAME_COLUMN_NAME = "username"
        val RESULT_COLUMN_NAME = "result"
    }
}

class getListAsync() : AsyncTask<Void, Void, String>() {
    override fun doInBackground(vararg params: Void?): String {
        val text = URL("http://hufiecgniezno.pl/br/record.php?f=get").readText()
        return(text)
    }
    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}

class RankingActivity : AppCompatActivity() {
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

    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun getDataFromDb(dbHandler: RecordsDBOpenHelper): String{
        var resultText = "  \nIndeks  -  Wynik\n\n"
        var counter = 1
        showToast("Sieć niedostępna.\nWyniki wyświetlono z bazy danych.")
        val cursor = dbHandler.getAllResults()
        cursor!!.moveToFirst()
        resultText = resultText  + counter.toString() + ".  " +
                cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.USERNAME_COLUMN_NAME)) + "  -  " +
                cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.RESULT_COLUMN_NAME)) + "\n"
        counter++
        while (cursor.moveToNext()) {
            resultText = resultText  + counter.toString() + ".  " +
                    cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.USERNAME_COLUMN_NAME)) + "  -  " +
                    cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.RESULT_COLUMN_NAME)) + "\n"
            counter++
        }
        cursor.close()
        return(resultText)
    }

    fun downloadDataFromServer(dbHandler: RecordsDBOpenHelper): String{
        var resultText = "  \nIndeks  -  Wynik\n\n"
        var result : Result
        var counter = 1
        val text = getListAsync().execute().get().toString()
        val preparedList = text.split("],".toRegex())
        dbHandler.deleteAllResults()
        for (elem: String in preparedList) {
            val dividedAndCleared = elem
                .replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .split(",".toRegex())
            resultText = resultText + counter.toString() + ".  " + dividedAndCleared[1] + "  -  " + dividedAndCleared[2] + "\n"
            Log.d("Magic", dividedAndCleared.toString())
            result = Result(counter, dividedAndCleared[1], dividedAndCleared[2])
            dbHandler.addResult(result)
            counter++
        }
        return(resultText)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        //https://blog.mindorks.com/android-sqlite-database-in-kotlin
        //great tutorial for sqlite db
        val dbHandler = RecordsDBOpenHelper(this, null)

        if (isNetworkAvailable()) {
            ranking.text = downloadDataFromServer(dbHandler)
        } else {
            ranking.text = getDataFromDb(dbHandler)
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
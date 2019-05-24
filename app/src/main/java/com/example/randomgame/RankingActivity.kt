package com.example.randomgame

import android.annotation.SuppressLint
import android.app.Activity
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
import android.support.v4.app.NavUtils
import android.util.JsonReader
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ranking.*
import org.json.JSONStringer
import java.io.StringReader
import java.net.URL
import android.net.NetworkInfo
import android.net.ConnectivityManager
import android.view.*
import android.widget.*
import java.lang.Boolean.TRUE


class Result(var id: Int, userName: String, result: String) {
    var userName: String? = userName
    var result: String? = result
}

class ResultsListViewAdapter(private val activity: Activity, results: ArrayList<Result>) : BaseAdapter() {

    private var results: ArrayList<Result>

    init {
        this.results = results
    }

    override fun getCount(): Int {
        return results.size
    }

    override fun getItem(i: Int): Any {
        return i
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var vi = inflater.inflate(R.layout.list_item, null)
        var place = vi.findViewById(R.id.place) as TextView
        var username = vi.findViewById(R.id.username) as TextView
        var result = vi.findViewById(R.id.result) as TextView
        place.text = results[i].id.toString()+"."
        username.text = results[i].userName
        result.text = results[i].result
        return vi
    }
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

    fun getDataFromDb(dbHandler: RecordsDBOpenHelper) {
        var counter = 1
        var result: Result
        var resultsList = ArrayList<Result>()

        showToast("Sieć niedostępna.\nWyniki wyświetlono z bazy danych.")
        val cursor = dbHandler.getAllResults()
        cursor!!.moveToFirst()
        result = Result(
            counter,
            cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.USERNAME_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.RESULT_COLUMN_NAME))
        )
        resultsList.add(result)
        counter++
        while (cursor.moveToNext()) {
            result = Result(counter,
                cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.USERNAME_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(RecordsDBOpenHelper.RESULT_COLUMN_NAME)))
            resultsList.add(result)
            counter++
        }

        cursor.close()
        var adapter = ResultsListViewAdapter(this, resultsList)
        listView.adapter = adapter
    }

    fun downloadDataFromServer(dbHandler: RecordsDBOpenHelper) {
        var resultsList = ArrayList<Result>()
        var adapter: ResultsListViewAdapter
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
            result = Result(counter, dividedAndCleared[1], dividedAndCleared[2])
            resultsList.add(result)
            dbHandler.addResult(result)
            counter++
        }
        adapter = ResultsListViewAdapter(this, resultsList)
        listView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ranking)

        val dbHandler = RecordsDBOpenHelper(this, null)

        if (isNetworkAvailable()) {
            downloadDataFromServer(dbHandler)
        } else {
            getDataFromDb(dbHandler)
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
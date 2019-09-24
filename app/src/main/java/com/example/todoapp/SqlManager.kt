package com.example.todoapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.ID

class TasksDatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE_COLUMN_NAME
                + " TEXT," +
                DESCRIPTION_COLUMN_NAME
                + " TEXT" + ")")
        db.execSQL(CREATE_PRODUCTS_TABLE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }
    fun addResult(task: Task) {
        val values = ContentValues()
        values.put(TITLE_COLUMN_NAME, task.title)
        values.put(DESCRIPTION_COLUMN_NAME, task.description)
        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }
    fun getAllResults(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun getTask(id: Int): Task {
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ID)))
        val title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME))
        val description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN_NAME))
        cursor.close()

        return Task(id, title, description)
    }

    fun editTask(id: Int, title: String, description: String) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(TITLE_COLUMN_NAME, title)
        values.put(DESCRIPTION_COLUMN_NAME, description)
        db.update(TABLE_NAME, values, COLUMN_ID + "=?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteElement(id: Int){
        val db = this.writableDatabase
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", arrayOf(id.toString()))
        db.close()
    }
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "todoApp_v2.db"
        val TABLE_NAME = "tasks"
        val COLUMN_ID = "_id"
        val TITLE_COLUMN_NAME = "title"
        val DESCRIPTION_COLUMN_NAME = "description"
    }
}
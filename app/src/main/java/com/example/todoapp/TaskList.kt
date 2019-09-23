package com.example.todoapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskList : AppCompatActivity() {

    private lateinit var dbHandler: TasksDatabaseHelper

    fun getDataFromDb(dbHandler: TasksDatabaseHelper) {
        var counter = 1
        var task: Task
        var taskList = ArrayList<Task>()

        val cursor = dbHandler.getAllResults()
        cursor!!.moveToFirst()
        task = Task(
            cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.STATUS_COLUMN_NAME))
        )
        taskList.add(task)
        counter++
        while (cursor.moveToNext()) {
            task = Task(
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.STATUS_COLUMN_NAME))
            )
            taskList.add(task)
            counter++
        }
        cursor.close()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        dbHandler = TasksDatabaseHelper(this, null)
        getDataFromDb(dbHandler)

        button.setOnClickListener {
            val intent = Intent(this, TaskForm::class.java)
            startActivity(intent)
        }
    }
}

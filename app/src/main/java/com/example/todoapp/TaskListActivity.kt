package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_ranking.*
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : AppCompatActivity() {

    private lateinit var dbHandler: TasksDatabaseHelper

    fun getDataFromDb(dbHandler: TasksDatabaseHelper) {
        var counter = 1
        var task: Task
        var taskList = ArrayList<Task>()

        val cursor = dbHandler.getAllResults()

        if((cursor != null) && (cursor.getCount() > 0)) {
            task = Task(
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME))
            )
            taskList.add(task)
            counter++
            while (cursor.moveToNext()) {
                task = Task(
                    cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME))
                )
                taskList.add(task)
                counter++
            }
            cursor.close()
            var adapter = TaskListViewAdapter(this, taskList)
            listView.adapter = adapter
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        dbHandler = TasksDatabaseHelper(this, null)
        getDataFromDb(dbHandler)

        newTaskButton.setOnClickListener {
            val intent = Intent(this, TaskFormActivity::class.java)
            startActivity(intent)
        }
    }
}

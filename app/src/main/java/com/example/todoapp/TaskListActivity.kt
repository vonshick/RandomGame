package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_task_list.*
import android.widget.TextView
import android.widget.Toast


open class TaskListActivity : AppCompatActivity() {

    private lateinit var dbHandler: TasksDatabaseHelper
    private var chosenListViewElement: Int = -1

    private fun refreshListView() {
        editTaskButton.isEnabled = false
        doneButton.isEnabled = false

        var counter = 1
        var task: Task
        var taskList = ArrayList<Task>()

        val cursor = dbHandler.getAllResults()
        cursor!!.moveToFirst()

        if((cursor != null) && (cursor.getCount() > 0)) {
            task = Task(
                cursor.getInt(cursor.getColumnIndex(TasksDatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME))
            )
            taskList.add(task)
            counter++
            while (cursor.moveToNext()) {
                task = Task(
                    cursor.getInt(cursor.getColumnIndex(TasksDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.TITLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(TasksDatabaseHelper.DESCRIPTION_COLUMN_NAME))
                )
                taskList.add(task)
                counter++
            }
            cursor.close()
            var adapter = TaskListViewAdapter(this, taskList)
            tasksListView.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        refreshListView()
    }

    private fun saveLogin(task: Task) {
        val sharedPreference = getSharedPreferences("com.example.todoapp.prefs", 0)
        var editor = sharedPreference.edit()
        editor.putInt("id", task.id)
        editor.putString("title", task.title)
        editor.putString("description", task.description)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)
        tasksListView.isLongClickable = true
        dbHandler = TasksDatabaseHelper(this, null)
        refreshListView()

        newTaskButton.setOnClickListener {
            val intent = Intent(this, TaskFormActivity::class.java)
            intent.putExtra("EDIT_OR_NEW", "NEW")
            startActivityForResult(intent, 1)
        }

        tasksListView.setOnItemClickListener { parent, view, position, id ->
            chosenListViewElement = id.toInt()
            editTaskButton.isEnabled = true
            doneButton.isEnabled = true
        }

        editTaskButton.setOnClickListener {
            val databaseId = tasksListView.adapter.getItem(chosenListViewElement).toString().toInt()
            saveLogin(dbHandler.getTask(databaseId))
            val intent = Intent(this, TaskFormActivity::class.java)
            intent.putExtra("EDIT_OR_NEW", "EDIT")
            startActivityForResult(intent, 1)
        }

        doneButton.setOnClickListener {
            val databaseId = tasksListView.adapter.getItem(chosenListViewElement).toString().toInt()
            dbHandler.deleteElement(databaseId)
            refreshListView()
        }

    }
}

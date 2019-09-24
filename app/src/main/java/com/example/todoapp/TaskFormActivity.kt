package com.example.todoapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_task_form.*

class TaskFormActivity : AppCompatActivity() {

    private var title = ""
    private var description = ""

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


    private fun saveTaskToDb(task: Task){
        val dbHandler = TasksDatabaseHelper(this, null)
        dbHandler.addResult(task)
    }

    private fun saveTask()  {
        if(titleEditText.text.toString() == "") {
            showToast("Title can not be empty!")
        } else {
            title = titleEditText.text.toString()
            description = descriptionEditText.text.toString()
            saveTaskToDb(Task(-1, title, description))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_form)

        addButton.setOnClickListener {
            saveTask()
        }

        cancelButton.setOnClickListener {
            finish()
        }

    }


}

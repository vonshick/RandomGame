package com.example.todoapp

import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_todo_form.*

class TaskForm : AppCompatActivity() {

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


    private fun saveCredentialsToDb(task: Task){
        val dbHandler = TasksDatabaseHelper(this, null)
        dbHandler.addResult(task)
    }

    private fun saveTask()  {
        if(titleEditText.text.toString() == "") {
            showToast("Title can not be empty!")
        } else {
            title = titleEditText.text.toString()
            description = descriptionEditText.text.toString()
            saveCredentialsToDb(Task(title, description, "TODO"))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_form)

        addButton.setOnClickListener {
            saveTask()
        }

    }


}

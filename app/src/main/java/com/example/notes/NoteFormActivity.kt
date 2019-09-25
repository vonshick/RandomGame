package com.example.notes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_note_form.*

class NoteFormActivity : AppCompatActivity() {

    private var title = ""
    private var description = ""
    private var noteDbId : Int = 0
    private lateinit var  dbHandler : NotesDatabaseHelper
    private lateinit var mode : String //edit or create new

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

    private fun saveNote()  {
        if(titleEditText.text.toString() == "") {
            showToast("Title can not be empty!")
        } else {
            title = titleEditText.text.toString()
            description = descriptionEditText.text.toString()

            if(mode == "EDIT") {
                dbHandler.editNote(noteDbId, title, description)
            } else {
                dbHandler.addResult(Note(-1, title, description))
            }

            finish()
        }
    }

    fun restoreSavedData(){
        val sharedPreference = getSharedPreferences("com.example.notes.prefs", 0)
        titleEditText.setText(sharedPreference.getString("title", ""))
        descriptionEditText.setText(sharedPreference.getString("description", ""))
        noteDbId = sharedPreference.getInt("id", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_form)
        dbHandler = NotesDatabaseHelper(this, null)

        if (savedInstanceState == null) {
            val extras = intent.extras
            if (extras != null) {
                mode = extras.getString("EDIT_OR_NEW")
            }
        } else {
            mode = savedInstanceState.getSerializable("EDIT_OR_NEW") as String
        }

        if(mode == "EDIT"){
            restoreSavedData()
        }

        addButton.setOnClickListener {
            saveNote()
        }

        cancelButton.setOnClickListener {
            finish()
        }

    }


}

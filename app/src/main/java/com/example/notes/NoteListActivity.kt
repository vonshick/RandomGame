package com.example.notes

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_note_list.*


open class NoteListActivity : AppCompatActivity() {

    private lateinit var dbHandler: NotesDatabaseHelper
    private var chosenListViewElement: Int = -1

    private fun refreshListView() {
        editNoteButton.isEnabled = false
        deleteButton.isEnabled = false

        var counter = 1
        var note: Note
        var noteList = ArrayList<Note>()

        val cursor = dbHandler.getAllResults()
        cursor!!.moveToFirst()

        if((cursor != null) && (cursor.getCount() > 0)) {
            note = Note(
                cursor.getInt(cursor.getColumnIndex(NotesDatabaseHelper.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.TITLE_COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.DESCRIPTION_COLUMN_NAME))
            )
            noteList.add(note)
            counter++
            while (cursor.moveToNext()) {
                note = Note(
                    cursor.getInt(cursor.getColumnIndex(NotesDatabaseHelper.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.TITLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(NotesDatabaseHelper.DESCRIPTION_COLUMN_NAME))
                )
                noteList.add(note)
                counter++
            }
            cursor.close()
            var adapter = NoteListViewAdapter(this, noteList)
            noteListView.adapter = adapter
        }
    }

    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        refreshListView()
    }

    private fun saveLogin(note: Note) {
        val sharedPreference = getSharedPreferences("com.example.notes.prefs", 0)
        var editor = sharedPreference.edit()
        editor.putInt("id", note.id)
        editor.putString("title", note.title)
        editor.putString("description", note.description)
        editor.apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_list)
        noteListView.isLongClickable = true
        dbHandler = NotesDatabaseHelper(this, null)
        refreshListView()

        newNoteButton.setOnClickListener {
            val intent = Intent(this, NoteFormActivity::class.java)
            intent.putExtra("EDIT_OR_NEW", "NEW")
            startActivityForResult(intent, 1)
        }

        noteListView.setOnItemClickListener { parent, view, position, id ->
            chosenListViewElement = id.toInt()
            editNoteButton.isEnabled = true
            deleteButton.isEnabled = true
        }

        editNoteButton.setOnClickListener {
            val databaseId = noteListView.adapter.getItem(chosenListViewElement).toString().toInt()
            saveLogin(dbHandler.getNote(databaseId))
            val intent = Intent(this, NoteFormActivity::class.java)
            intent.putExtra("EDIT_OR_NEW", "EDIT")
            startActivityForResult(intent, 1)
        }

        deleteButton.setOnClickListener {
            val databaseId = noteListView.adapter.getItem(chosenListViewElement).toString().toInt()
            dbHandler.deleteElement(databaseId)
            refreshListView()
        }

    }
}

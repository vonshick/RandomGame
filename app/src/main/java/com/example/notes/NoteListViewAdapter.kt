package com.example.notes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class NoteListViewAdapter(private val activity: Activity, notes: ArrayList<Note>) : BaseAdapter() {

    private var notes: ArrayList<Note>

    init {
        this.notes = notes
    }

    override fun getCount(): Int {
        return notes.size
    }

    override fun getItem(i: Int): Int? {
        return this.notes[i].id
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    private fun cutDescription(description: String): String {
        return if (description.length > 50) {
            description.substring(0, 50) + "..."
        } else {
            description
        }
    }

    @SuppressLint("InflateParams", "ViewHolder")
    override fun getView(i: Int, convertView: View?, viewGroup: ViewGroup): View {
        val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var vi = inflater.inflate(R.layout.list_item, null)
        var title = vi.findViewById(R.id.title) as TextView
        var description = vi.findViewById(R.id.description) as TextView
        title.text = notes[i].title
        description.text = notes[i].description?.let { cutDescription(it) }
        return vi
    }
}
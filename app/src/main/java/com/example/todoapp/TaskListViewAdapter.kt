package com.example.todoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class TaskListViewAdapter(private val activity: Activity, tasks: ArrayList<Task>) : BaseAdapter() {

    private var tasks: ArrayList<Task>

    init {
        this.tasks = tasks
    }

    override fun getCount(): Int {
        return tasks.size
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
        var title = vi.findViewById(R.id.place) as TextView
        title.text = tasks[i].title
        return vi
    }
}
package com.example.randomgame

import android.os.AsyncTask
import java.net.URL

class saveRecordAsync(record: Int, username: String?) : AsyncTask<Void, Void, String>() {
    val innerRecord: Int = record
    val innerUsername: String? = username
    override fun doInBackground(vararg params: Void?): String {
        val text = URL("http://hufiecgniezno.pl/br/record.php?f=add&id="+innerUsername+"&r="+innerRecord.toString()).readText()
        return(text)
    }
    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}
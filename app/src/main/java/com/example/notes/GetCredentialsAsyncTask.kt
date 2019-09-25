package com.example.notes

import android.os.AsyncTask
import java.net.URL

class getCredentialsAsync(username: String?) : AsyncTask<Void, Void, String>() {
    val innerUsername: String? = username
    override fun doInBackground(vararg params: Void?): String {
        val text = URL("https://am-projekt.herokuapp.com/?login=" + innerUsername).readText()
        return(text)
    }
    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}
package com.example.todoapp

import android.os.AsyncTask
import com.github.kittinunf.fuel.httpPost

class serveCredentialsAsync(login: String?, password: String?, endpoint: String?) : AsyncTask<Void, Void, String>() {
    val innerLogin: String? = login
    val innerPassword: String? = password
    val innerEndpoint: String? = endpoint

    private fun processResponse(rawResponse: String): String {
        val preparedList = rawResponse.split(":".toRegex())
        val extractedResponse = preparedList[1]
            .replace("\"", "")
            .replace("}", "")
        return extractedResponse
    }

    override fun doInBackground(vararg params: Void?): String {
        val jsonPayload = "{ \"login\" : \"" + innerLogin + "\", " + " \"password\" : \"" + innerPassword + "\"}"
        val url = "https://am-projekt.herokuapp.com/" + innerEndpoint
        val (request, response, result) = url
            .httpPost()
            .body(jsonPayload, Charsets.UTF_8)
            .header("Content-Type" to "application/json")
            .responseString()
        return if(result.component1() == null){
            "error"
        } else {
            processResponse(result.component1().toString())
        }
    }
    override fun onPreExecute() {
        super.onPreExecute()
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}
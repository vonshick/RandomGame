package com.example.randomgame

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import kotlin.random.Random

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.textView
import kotlinx.android.synthetic.main.activity_sign_in.*
import java.net.URL


class MainActivity : AppCompatActivity() {
    var currentNumber = 0
    var record = 0
    var shots = 0
    var currentShot = 0
    var scoresSum = 0
    var username : String? = ""

    fun getSavedNumber(){
        val sharedPreference = getSharedPreferences("com.example.randomgame.prefs", 0)
        record = sharedPreference.getInt("savedNumber", 0)
        textView.text = "Suma twoich punktów w sesji wynosi: "+record.toString()
    }

    fun editSavedNumber() {
        record += scoresSum
        val sharedPreference = getSharedPreferences("com.example.randomgame.prefs", 0)
        var editor = sharedPreference.edit()
        editor.putInt("savedNumber", record)
        editor.apply()
    }

    fun countScores(shots: Int): Int {
        if (shots == 1){
            return 5
        } else if ((shots >= 2) and (shots<=4)){
            return 3
        } else if ((shots >= 5) and (shots<=6)){
            return 2
        } else if ((shots >= 7) and (shots<=10)){
            return 1
        } else {
            return 0
        }
    }

    fun newGame(){
        scoresSum = 0
        shots = 0
        currentNumber = Random.nextInt(0, 20)
    }

    fun showToast(message: String){
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun loseDialog(){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Przegrałeś!")
        builder.setMessage("Gra rozpocznie się od nowa")
        builder.setPositiveButton("OK") { _,_ ->
            showToast("Rozpoczynamy nową grę!")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun winDialog(scores: Int){
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("Trafiłeś!")
        builder.setMessage("Liczba strzałów potrzebnych do zwycięstwa: " + shots.toString() +"\nUzyskane punkty: "+scores.toString())
        builder.setPositiveButton("Super") { _, _ ->
            showToast("Rozpoczynamy nową grę!")
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun getResponseLastLine(text: String): String{
        return(text.takeLast(2))
    }

    fun saveRecord(){
        val rawText = saveRecordAsync(record, username).execute().get().toString()
        val response = getResponseLastLine(rawText)

        if(response.equals("OK")){
            showToast("Rekord zapisany na serwerze!")
        } else {
            showToast("Nie udało się zapisać rekordu na serwerze")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                username = data.extras!!.getString("USERNAME")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        username = getIntent().getStringExtra("USERNAME")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView.text = "Nie ustanowiłeś jeszcze rekordu"
        currentNumber = Random.nextInt(0,20)

        number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                var isReady = !number.getText().toString().isEmpty();
                shoot.setEnabled(isReady)
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        newGameButton.setOnClickListener {
            newGame()
            showToast("Wylosowano nową liczbę!")
        }

        logout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        shoot.setOnClickListener {
            shots++
            if(shots > 10){
                shots = 0
                loseDialog()
                number.getText().clear()
            } else {
                currentShot = number.text.toString().toInt()
                number.getText().clear()
                if (currentShot > 20 || currentShot < 0) {
                    showToast("Liczba musi należeć do przedziału od 0 do 20!")
                } else {
                    if (currentShot > currentNumber) {
                        showToast("Wybierz mniejszą liczbę! \nLiczba strzałów: " + shots.toString())
                    } else if (currentShot < currentNumber) {
                        showToast("Wybierz większą liczbę! \nLiczba strzałów: " + shots.toString())
                    } else {
                        var scores = countScores(shots)
                        scoresSum += scores
                        winDialog(scores)
                        editSavedNumber()
                        getSavedNumber()
                        saveRecord()
                        newGame()
                    }
                }
            }
        }
        records.setOnClickListener(){
            val intent = Intent(this, RankingActivity::class.java)
            intent.putExtra("USERNAME", username)
            startActivity(intent)
        }
    }
}

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
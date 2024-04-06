package com.example.kotlin_version

import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.example.kotlin_version.databinding.ActivityMainBinding
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity(){

    private lateinit var editTextPrompt: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Find EditText and Button
        editTextPrompt = findViewById(R.id.message_input_field)
        val sendButton = findViewById<Button>(R.id.send_button)
        val userText = findViewById<TextView>(R.id.user_text)
        // Listener for EditText and Button actions
        editTextPrompt.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val userInput = editTextPrompt.text.toString()
                processUserInput(userInput)
                editTextPrompt.text.clear()
                userText.text = userInput
                true // Indicate event handled
            } else {
                false
            }
        }
        // Handle Button click
        sendButton.setOnClickListener {
            val userInput = editTextPrompt.text.toString()
            processUserInput(userInput)
            editTextPrompt.text.clear()
            userText.text = userInput
        }
    }

    // Function to process user input and update TextView
    private fun processUserInput(userInput: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://10.0.2.2:5000/generate") // Replace with your Flask server URL
                val conn = url.openConnection() as HttpURLConnection
                conn.requestMethod = "POST"
                conn.setRequestProperty("Content-Type", "application/json; utf-8")
                conn.setRequestProperty("Accept", "application/json")
                conn.doOutput = true

                val jsonInputString = JSONObject().apply {
                    put("prompt", userInput)
                }.toString()

                OutputStreamWriter(conn.outputStream).use { os ->
                    os.write(jsonInputString)
                    os.flush()
                }

                val inputStream = BufferedInputStream(conn.inputStream)
                val response = inputStream.bufferedReader().use(BufferedReader::readText)

                launch(Dispatchers.Main) {
                    // Update TextView with response
                    val userText = findViewById<TextView>(R.id.bot_text)
                    userText.text = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}

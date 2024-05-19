package com.example.kotlin_version

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPrompt: EditText
    private lateinit var chatList: ListView
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Declaring EditText, Button and ListView
        editTextPrompt = findViewById(R.id.message_input_field)
        val sendButton = findViewById<Button>(R.id.send_button)
        chatList = findViewById(R.id.chat_list)

        // Initializing the chat adapter
        chatAdapter = ChatAdapter(this, R.layout.item_chat_message, mutableListOf())
        chatList.adapter = chatAdapter

        // Listener for EditText and Button actions
        editTextPrompt.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val userInput = editTextPrompt.text.toString()
                processUserInput(userInput)
                editTextPrompt.text.clear()
                true // Indicate event handled
            } else {
                false
            }
        }
        // Handling the send button click
        sendButton.setOnClickListener {
            val userInput = editTextPrompt.text.toString()
            processUserInput(userInput)
            editTextPrompt.text.clear()
        }
    }

    // Function to process user input and update ListView
    private fun processUserInput(userInput: String) {
        // addint the user input to the chat adapter
        chatAdapter.add(ChatMessage("You: $userInput", true))

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://10.0.2.2:5000/generate")
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
                    // Add bot's response to chat adapter
                    chatAdapter.add(ChatMessage("Bot: $response", false))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
data class ChatMessage(val message: String, val isUser: Boolean)
class ChatAdapter(context: Context, resource: Int, objects: MutableList<ChatMessage>)
    : ArrayAdapter<ChatMessage>(context, resource, objects) {
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: inflater.inflate(R.layout.item_chat_message, parent, false)

        val logoImageView = view.findViewById<ImageView>(R.id.logo)
        val messageTextView = view.findViewById<TextView>(R.id.message)

        val chatMessage = getItem(position)

        if (chatMessage != null) {
            messageTextView.text = chatMessage.message

            if (chatMessage.isUser) {
                logoImageView.setImageResource(R.drawable.user_logo)
            } else {
                logoImageView.setImageResource(R.drawable.bot_logo)
            }
        }
        return view
    }
}

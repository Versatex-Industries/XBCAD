package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Message
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.ChatAdapter

class ChatActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendButton: Button
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<Message>()
    private lateinit var contactNameTextView: TextView

    private lateinit var contactId: String
    private lateinit var currentUserId: String
    private lateinit var contactName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // Initialize UI components
        backButton = findViewById(R.id.backButton)
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendButton = findViewById(R.id.sendButton)
        contactNameTextView = findViewById(R.id.contactName)

        // Get intent extras
        contactId = intent.getStringExtra("contactId") ?: ""
        contactName = intent.getStringExtra("contactName") ?: ""

        // Set up RecyclerView
        adapter = ChatAdapter(messages, "")
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = adapter

        // Fetch current user ID from the API
        fetchUserIdAndInitialize()

        // Back button functionality
        backButton.setOnClickListener { finish() }

        // Send button functionality
        sendButton.setOnClickListener {
            val content = messageInput.text.toString().trim()
            if (content.isNotEmpty()) {
                sendMessage(content)
            }
        }
    }

    override fun onBackPressed() {
        // Custom navigation logic
        val intent = Intent(this, MessagesActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        startActivity(intent)

        // Call super.onBackPressed() to respect the system's expectations
        super.onBackPressed()
    }




    private fun fetchUserIdAndInitialize() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    response.body()?.let { user ->
                        currentUserId = user._id
                        withContext(Dispatchers.Main) {
                            // Update adapter with user ID
                            println("NBNBNB: " + currentUserId)
                            adapter.updateCurrentUserId(currentUserId)
                            loadMessages()
                        }
                    }
                    println("Profile Response: ${response.body()}")

                } else {
                    showError("Failed to fetch user profile: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error fetching user profile: ${e.message}")
            }
        }
    }

    private fun loadMessages() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getMessagesWithContact("Bearer ${getToken()}", contactId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        messages.clear()
                        messages.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                        scrollToBottom()
                    }
                } else {
                    showError("Failed to load messages: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading messages: ${e.message}")
            }
        }
    }

    private fun sendMessage(content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)

                // Construct the message payload
                val message = Message(
                    senderId = currentUserId,  // The current user's ID
                    recipientId = contactId,  // The contact the user is chatting with
                    content = content,
                    timestamp = "" // Backend will handle timestamp
                )

                // Make the API call
                val response = api.sendMessage(
                    "Bearer ${getToken()}",
                    contactId,
                    message
                )

                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        messageInput.text.clear()
                        loadMessages() // Refresh messages after sending
                    }
                } else {
                    showError("Failed to send message: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error sending message: ${e.message}")
            }
        }
    }


    private fun scrollToBottom() {
        messagesRecyclerView.scrollToPosition(messages.size - 1)
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@ChatActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

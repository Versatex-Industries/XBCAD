package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Conversation
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.ConversationsAdapter

class MessagesActivity : AppCompatActivity() {

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var newMessageFab: FloatingActionButton
    private lateinit var adapter: ConversationsAdapter
    private val conversations = mutableListOf<Conversation>()
    private var currentUserId: String? = null // Dynamically set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        newMessageFab = findViewById(R.id.newMessageFab)

        // Fetch current user ID
        fetchCurrentUserId()

        // New message button
        newMessageFab.setOnClickListener {
            startNewMessage()
        }
    }

    private fun fetchCurrentUserId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    currentUserId = response.body()?._id
                    withContext(Dispatchers.Main) {
                        if (currentUserId != null) {
                            setupRecyclerView()
                            loadConversations()
                        } else {
                            showError("Failed to fetch user ID")
                        }
                    }
                } else {
                    showError("Failed to fetch profile: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error fetching profile: ${e.message}")
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = ConversationsAdapter(conversations, currentUserId!!) { conversation ->
            openChat(conversation.contactId, conversation.contactName)
        }
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesRecyclerView.adapter = adapter
    }

    private fun loadConversations() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAllConversations("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        conversations.clear()
                        conversations.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load conversations: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading conversations: ${e.message}")
            }
        }
    }

    private fun openChat(contactId: String, contactName: String) {
        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("contactId", contactId)
            putExtra("contactName", contactName)
        }
        startActivity(intent)
        finish()
    }

    private fun startNewMessage() {
        val intent = Intent(this, StartNewMessageActivity::class.java)
        startActivity(intent)
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@MessagesActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

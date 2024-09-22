package za.edu.vcconnect.xbcad7319.schoolsync

import MessagesAdapter
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class MessagesActivity : AppCompatActivity() {

    private lateinit var profileButton: ImageButton
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private lateinit var recipientEmailInput: EditText
    private lateinit var opportunitySpinner: Spinner
    private lateinit var messageInput: EditText
    private lateinit var addCategoryButton: Button
    private lateinit var attachPhotoButton: Button
    private lateinit var sendButton: Button
    private lateinit var messagesRecyclerView: RecyclerView

    private lateinit var messagesAdapter: MessagesAdapter
    private val messagesList = mutableListOf<Message>()

    private val apiService = ApiService()
    private val opportunitiesList = mutableListOf<String>() // Titles of the opportunities
    private val opportunityIds = mutableListOf<String>() // IDs of the opportunities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.messages)

        // Initialize views
        profileButton = findViewById(R.id.profileButton)
        searchView = findViewById(R.id.searchView)
        recipientEmailInput = findViewById(R.id.recipientEmailInput)
        opportunitySpinner = findViewById(R.id.opportunitySpinner)
        messageInput = findViewById(R.id.messageInput)
        addCategoryButton = findViewById(R.id.addCategoryButton)
        attachPhotoButton = findViewById(R.id.attachPhotoButton)
        sendButton = findViewById(R.id.sendButton)
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)

        // Set up RecyclerView
        messagesRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesAdapter = MessagesAdapter(messagesList)
        messagesRecyclerView.adapter = messagesAdapter

        // Fetch volunteer opportunities and populate the spinner
        fetchOpportunities()

        // Fetch messages from API
        fetchMessages()

        // Set up listeners
        sendButton.setOnClickListener {
            val message = messageInput.text.toString().trim()
            val recipientEmail = recipientEmailInput.text.toString().trim()
            val selectedOpportunity = opportunitySpinner.selectedItem.toString()

            if (message.isNotEmpty() && recipientEmail.isNotEmpty()) {
                sendMessage(message, recipientEmail)
            } else {
                Toast.makeText(this, "Please enter a message and recipient email", Toast.LENGTH_SHORT).show()
            }
        }

        addCategoryButton.setOnClickListener {
            Toast.makeText(this, "Add category feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        attachPhotoButton.setOnClickListener {
            Toast.makeText(this, "Attach photo feature coming soon!", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to fetch volunteer opportunities and populate the spinner
    private fun fetchOpportunities() {
        val sharedPref: SharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)

        if (token != null) {
            apiService.getVolunteerOpportunities(token, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MessagesActivity, "Failed to load opportunities", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        if (response.isSuccessful) {
                            val responseData = response.body?.string()
                            if (responseData != null) {
                                try {
                                    val jsonArray = JSONArray(responseData)
                                    opportunitiesList.clear()
                                    opportunityIds.clear()
                                    for (i in 0 until jsonArray.length()) {
                                        val opportunityObject = jsonArray.getJSONObject(i)
                                        val title = opportunityObject.getString("title")
                                        val id = opportunityObject.getString("_id")
                                        opportunitiesList.add(title)
                                        opportunityIds.add(id)
                                    }
                                    // Update spinner with opportunities
                                    val adapter = ArrayAdapter(this@MessagesActivity, android.R.layout.simple_spinner_item, opportunitiesList)
                                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                                    opportunitySpinner.adapter = adapter
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        } else {
                            Toast.makeText(this@MessagesActivity, "Failed to load opportunities", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }

    // Function to fetch messages
    private fun fetchMessages() {
        // Retrieve the token from shared preferences
        val sharedPref: SharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)

        val selectedOpportunity = opportunitySpinner.selectedItem?.toString() ?: run {
            Toast.makeText(this, "No opportunity selected", Toast.LENGTH_SHORT).show()
            return
        }

        if (token != null) {
            apiService.getMessages(token, selectedOpportunity, object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MessagesActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (responseData != null) {
                            try {
                                val jsonArray = JSONArray(responseData)
                                messagesList.clear()
                                if (jsonArray.length() > 0) {
                                    for (i in 0 until jsonArray.length()) {
                                        val messageObject = jsonArray.getJSONObject(i)
                                        val message = Message(
                                            senderId = messageObject.getString("senderId"),
                                            messageContent = messageObject.getString("messageContent"),
                                            timestamp = messageObject.getString("timestamp")
                                        )
                                        messagesList.add(message)
                                    }
                                    runOnUiThread {
                                        messagesAdapter.notifyDataSetChanged()
                                    }
                                } else {
                                    runOnUiThread {
                                        Toast.makeText(this@MessagesActivity, "No messages found", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MessagesActivity, "Failed to load messages", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }



    // Function to send a message
    private fun sendMessage(messageContent: String, recipientEmail: String) {
        val sharedPref: SharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)
        val senderEmail = sharedPref.getString("userEmail", null) // Fetch the sender's email from SharedPreferences

        val selectedOpportunityId = opportunityIds[opportunitySpinner.selectedItemPosition] // Get selected opportunity ID

        val requestBody = JSONObject().apply {
            put("messageContent", messageContent)
            put("toEmail", recipientEmail)
            put("fromEmail", senderEmail) // Include the sender's email
            put("opportunityId", selectedOpportunityId) // Use opportunity ID
        }

        if (token != null) {
            apiService.sendMessage(token, requestBody.toString(), object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    runOnUiThread {
                        Toast.makeText(this@MessagesActivity, "Failed to send message", Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    runOnUiThread {
                        if (response.isSuccessful) {
                            Toast.makeText(this@MessagesActivity, "Message sent", Toast.LENGTH_SHORT).show()
                            messageInput.text.clear()
                            fetchMessages() // Refresh the message list
                        } else {
                            val errorMessage = response.body?.string() ?: "Unknown error"
                            Toast.makeText(this@MessagesActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}

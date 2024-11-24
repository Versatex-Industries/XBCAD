package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.User
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.ContactsAdapter

class StartNewMessageActivity : AppCompatActivity() {

    private lateinit var contactsRecyclerView: RecyclerView
    private val contacts = mutableListOf<User>()
    private lateinit var adapter: ContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_new_message)

        contactsRecyclerView = findViewById(R.id.contactsRecyclerView)
        adapter = ContactsAdapter(contacts) { contact ->
            startChat(contact)
        }
        contactsRecyclerView.layoutManager = LinearLayoutManager(this)
        contactsRecyclerView.adapter = adapter

        loadContacts()
    }

    private fun loadContacts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)

                val response = api.getAllUsers("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    println(response.body())
                    withContext(Dispatchers.Main) {
                        contacts.clear()
                        contacts.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load contacts: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading contacts: ${e.message}")
            }
        }
    }

    private fun startChat(contact: User) {
        println("Contact ID: "+contact.id)
        println("contactName: "+contact.username)

        val intent = Intent(this, ChatActivity::class.java).apply {
            putExtra("contactId", contact.id)
            putExtra("contactName", contact.username)

        }
        println("contactId"+ contact.id)
        println("contactName"+ contact.username)

        startActivity(intent)
        finish()
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private fun showError(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(this@StartNewMessageActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

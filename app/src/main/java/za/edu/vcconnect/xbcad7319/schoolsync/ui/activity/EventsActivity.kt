package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Event
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.EventsAdapter

class EventsActivity : AppCompatActivity() {

    private lateinit var eventsRecyclerView: RecyclerView
    private lateinit var eventsAdapter: EventsAdapter
    private val eventsList = mutableListOf<Event>()
    private lateinit var createEventButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
        createEventButton = findViewById(R.id.createEventButton)

        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
        eventsAdapter = EventsAdapter(eventsList, ::onJoinButtonClick)
        eventsRecyclerView.adapter = eventsAdapter

        setupRoleSpecificUI(getUserRoleFromPreferences())
        fetchEvents()
    }

    private fun getUserRoleFromPreferences(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("role", "Student") ?: "Student"
    }

    private fun setupRoleSpecificUI(role: String) {
        if (role == "Teacher") {
            createEventButton.visibility = View.VISIBLE
            createEventButton.setOnClickListener {
                // Navigate to CreateEventActivity (to be implemented below)
                startActivity(Intent(this, CreateEventActivity::class.java))
            }
        } else {
            createEventButton.visibility = View.GONE
        }
    }

    private fun fetchEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getEvents("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    response.body()?.let {
                        eventsList.clear()
                        eventsList.addAll(it)
                        withContext(Dispatchers.Main) {
                            eventsAdapter.notifyDataSetChanged()
                        }
                    }
                } else {
                    showError("Failed to fetch events: ${response.message()}")
                }
            } catch (e: Exception) {
                showError(e.message ?: "An error occurred")
            }
        }
    }

    private fun onJoinButtonClick(event: Event) {
        if (event.participants.contains(getUserId())) {
            Toast.makeText(this, "You have already joined ${event.eventName}.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.joinEvent("Bearer ${getToken()}", event._id)
                if (response.isSuccessful) {
                    fetchEvents() // Refresh events to update participant list
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@EventsActivity, "Successfully joined ${event.eventName}!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    showError("Failed to join event: ${response.message()}")
                }
            } catch (e: Exception) {
                showError(e.message ?: "An error occurred")
            }
        }
    }

    private fun getUserId(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("user_id", "") ?: ""
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@EventsActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

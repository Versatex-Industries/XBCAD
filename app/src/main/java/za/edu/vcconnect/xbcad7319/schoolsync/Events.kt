package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class Events : AppCompatActivity() {

//    private lateinit var eventsRecyclerView: RecyclerView
//    private lateinit var joinEventButton: Button
//    private lateinit var leaveEventButton: Button
//    private lateinit var viewDetailsButton: Button
//    private lateinit var eventName: TextView
//    private lateinit var eventDetails: TextView
//
//   // private val apiService = ApiService() // Replace with your ApiService implementation
//   // private val token = "your-auth-token" // Replace with your actual token
//    private var selectedEventId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
    }
}


//        // Initialize views
//        eventsRecyclerView = findViewById(R.id.eventsRecyclerView)
//        joinEventButton = findViewById(R.id.joinEventButton)
//        leaveEventButton = findViewById(R.id.leaveEventButton)
//        viewDetailsButton = findViewById(R.id.viewDetailsButton)
//        eventName = findViewById(R.id.eventName)
//        eventDetails = findViewById(R.id.eventDetails)
//
//        // Set up RecyclerView
//        eventsRecyclerView.layoutManager = LinearLayoutManager(this)
//        val adapter = EventsAdapter { eventId -> onEventSelected(eventId) }
//        eventsRecyclerView.adapter = adapter
//
//        // Fetch and display events
//        fetchUpcomingEvents(adapter)
//
//        // Handle Join Event button click
//        joinEventButton.setOnClickListener {
//            selectedEventId?.let {
//                joinEvent(it)
//            } ?: Toast.makeText(this, "Please select an event", Toast.LENGTH_SHORT).show()
//        }
//
//        // Handle Leave Event button click
//        leaveEventButton.setOnClickListener {
//            selectedEventId?.let {
//                leaveEvent(it)
//            } ?: Toast.makeText(this, "Please select an event", Toast.LENGTH_SHORT).show()
//        }
//
//        // Handle View Details button click
//        viewDetailsButton.setOnClickListener {
//            selectedEventId?.let {
//                fetchEventDetails(it)
//            } ?: Toast.makeText(this, "Please select an event", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    // Fetch upcoming events
//    private fun fetchUpcomingEvents(adapter: EventsAdapter) {
//        apiService.getUpcomingEvents(token, object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@Events, "Failed to fetch events", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    val eventsJsonArray = JSONArray(responseBody ?: "")
//                    val events = mutableListOf<Event>()
//
//                    for (i in 0 until eventsJsonArray.length()) {
//                        val eventJson = eventsJsonArray.getJSONObject(i)
//                        events.add(
//                            Event(
//                                id = eventJson.getString("id"),
//                                name = eventJson.getString("name"),
//                                details = eventJson.getString("details")
//                            )
//                        )
//                    }
//
//                    runOnUiThread {
//                        adapter.submitList(events)
//                    }
//                } else {
//                    runOnUiThread {
//                        Toast.makeText(this@Events, "Failed to fetch events", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//    }
//
//    // Join an event
//    private fun joinEvent(eventId: String) {
//        apiService.registerForEvent(token, eventId, object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@EventsActivity, "Failed to join event", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                runOnUiThread {
//                    if (response.isSuccessful) {
//                        Toast.makeText(this@EventsActivity, "Joined event successfully", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(this@EventsActivity, "Failed to join event", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//    }
//
//    // Leave an event
//    private fun leaveEvent(eventId: String) {
//        // Implement leave event functionality (if supported by the API)
//        Toast.makeText(this, "Leave event functionality not yet implemented", Toast.LENGTH_SHORT).show()
//    }
//
//    // Fetch event details
//    private fun fetchEventDetails(eventId: String) {
//        apiService.getEventDetails(token, eventId, object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                runOnUiThread {
//                    Toast.makeText(this@Events, "Failed to fetch event details", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val responseBody = response.body?.string()
//                    val eventJson = JSONObject(responseBody ?: "")
//
//                    runOnUiThread {
//                        eventName.text = eventJson.getString("name")
//                        eventDetails.text = eventJson.getString("details")
//                    }
//                } else {
//                    runOnUiThread {
//                        Toast.makeText(this@Events, "Failed to fetch event details", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        })
//    }
//
//    // Handle event selection
//    private fun onEventSelected(eventId: String) {
//        selectedEventId = eventId
//    }





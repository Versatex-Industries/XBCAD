package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException
import org.json.JSONArray
import org.json.JSONObject

class VolunteerActivity : AppCompatActivity() {

    private lateinit var eventTitle1: TextView
    private lateinit var eventFund1: TextView
    private lateinit var eventTitle2: TextView
    private lateinit var eventFund2: TextView
    private lateinit var opportunityTitle1: TextView
    private lateinit var opportunityTime1: TextView
    private lateinit var signUpButton1: Button
    private lateinit var opportunityTitle2: TextView
    private lateinit var opportunityTime2: TextView
    private lateinit var signUpButton2: Button
    private lateinit var registerButton: Button

    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.volunteer)

        // Initialize views
        eventTitle1 = findViewById(R.id.eventTitle1)
        eventFund1 = findViewById(R.id.eventFund1)
        eventTitle2 = findViewById(R.id.eventTitle2)
        eventFund2 = findViewById(R.id.eventFund2)
        opportunityTitle1 = findViewById(R.id.opportunityTitle1)
        opportunityTime1 = findViewById(R.id.opportunityTime1)
        signUpButton1 = findViewById(R.id.signUpButton1)
        opportunityTitle2 = findViewById(R.id.opportunityTitle2)
        opportunityTime2 = findViewById(R.id.opportunityTime2)
        signUpButton2 = findViewById(R.id.signUpButton2)
        registerButton = findViewById(R.id.registerButton)

        // Fetch data from the API
        val token = "your-jwt-token-here" // Replace with actual token
        fetchVolunteerOpportunities(token)
        fetchUpcomingEvents(token)

        // Set up listeners for sign-up and register buttons
        signUpButton1.setOnClickListener { signUpForOpportunity(token, "opportunityId1") }
        signUpButton2.setOnClickListener { signUpForOpportunity(token, "opportunityId2") }
        registerButton.setOnClickListener { registerForEvent(token, "eventId1") }
    }

    // Fetch volunteer opportunities
    private fun fetchVolunteerOpportunities(token: String) {
        apiService.getVolunteerOpportunities(token, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@VolunteerActivity, "Failed to fetch opportunities", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && responseData != null) {
                        val opportunities = JSONArray(responseData)
                        if (opportunities.length() > 0) {
                            val opportunity1 = opportunities.getJSONObject(0)
                            opportunityTitle1.text = opportunity1.getString("title")
                            opportunityTime1.text = opportunity1.getString("date")

                            if (opportunities.length() > 1) {
                                val opportunity2 = opportunities.getJSONObject(1)
                                opportunityTitle2.text = opportunity2.getString("title")
                                opportunityTime2.text = opportunity2.getString("date")
                            }
                        }
                    } else {
                        Toast.makeText(this@VolunteerActivity, "No opportunities available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    // Fetch upcoming events
    private fun fetchUpcomingEvents(token: String) {
        apiService.getUpcomingEvents(token, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@VolunteerActivity, "Failed to fetch events", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && responseData != null) {
                        val events = JSONArray(responseData)
                        if (events.length() > 0) {
                            val event1 = events.getJSONObject(0)
                            eventTitle1.text = event1.getString("title")
                            eventFund1.text = "R${event1.getInt("currentAmount")} raised of R${event1.getInt("goalAmount")}"

                            if (events.length() > 1) {
                                val event2 = events.getJSONObject(1)
                                eventTitle2.text = event2.getString("title")
                                eventFund2.text = "R${event2.getInt("currentAmount")} raised of R${event2.getInt("goalAmount")}"
                            }
                        }
                    } else {
                        Toast.makeText(this@VolunteerActivity, "No events available", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    // Sign up for a volunteer opportunity
    private fun signUpForOpportunity(token: String, opportunityId: String) {
        apiService.signUpForOpportunity(token, opportunityId, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@VolunteerActivity, "Failed to sign up", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VolunteerActivity, "Signed up successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@VolunteerActivity, "Sign up failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    // Register for an event
    private fun registerForEvent(token: String, eventId: String) {
        apiService.registerForEvent(token, eventId, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@VolunteerActivity, "Failed to register", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VolunteerActivity, "Registered for the event", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@VolunteerActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

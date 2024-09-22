package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class DashboardActivity : AppCompatActivity() {

    private lateinit var busInfo1: TextView
    private lateinit var busLocation1: TextView
    private lateinit var busInfo2: TextView
    private lateinit var busLocation2: TextView
    private lateinit var messageTitle1: TextView
    private lateinit var messageTime1: TextView
    private lateinit var messageTitle2: TextView
    private lateinit var messageTime2: TextView
    private lateinit var eventTitle1: TextView
    private lateinit var eventDate1: TextView
    private lateinit var eventTitle2: TextView
    private lateinit var eventDate2: TextView

    private lateinit var sendMessageButton: Button
    private lateinit var homeButton: Button
    private lateinit var eventsButton: Button
    private lateinit var notificationsButton: Button
    private lateinit var profileButton: Button

    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize views
        busInfo1 = findViewById(R.id.busInfo1)
        busLocation1 = findViewById(R.id.busLocation1)
        busInfo2 = findViewById(R.id.busInfo2)
        busLocation2 = findViewById(R.id.busLocation2)
        messageTitle1 = findViewById(R.id.messageTitle1)
        messageTime1 = findViewById(R.id.messageTime1)
        messageTitle2 = findViewById(R.id.messageTitle2)
        messageTime2 = findViewById(R.id.messageTime2)
        eventTitle1 = findViewById(R.id.eventTitle1)
        eventDate1 = findViewById(R.id.eventDate1)
        eventTitle2 = findViewById(R.id.eventTitle2)
        eventDate2 = findViewById(R.id.eventDate2)

        sendMessageButton = findViewById(R.id.sendMessageButton)
        homeButton = findViewById(R.id.homeButton)
        eventsButton = findViewById(R.id.eventsButton)
        notificationsButton = findViewById(R.id.notificationsButton)
        profileButton = findViewById(R.id.profileButton)

        // Set button listeners
        sendMessageButton.setOnClickListener {
            navigateToMessages()  // Navigate to messages page
        }

        homeButton.setOnClickListener {
            navigateToHome()
        }

        eventsButton.setOnClickListener {
            navigateToVolunteerOpportunities()  // Navigate to volunteer opportunities page
        }

        notificationsButton.setOnClickListener {
            navigateToNotifications()  // Navigate to notifications page
        }

        profileButton.setOnClickListener {
            navigateToProfile()  // Navigate to profile page
        }

        // Retrieve the token from shared preferences
        val sharedPref: SharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)

        if (token != null) {
            // Fetch dashboard data using the token
            fetchDashboardData(token)
        } else {
            // Handle the case where the token is missing
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
            // Optionally, you could redirect the user to the login screen here
        }
    }

    private fun fetchDashboardData(token: String) {
        apiService.getDashboard(token, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@DashboardActivity, "Failed to load dashboard", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val responseData = response.body?.string()
                        if (responseData != null) {
                            updateDashboard(JSONObject(responseData))
                        } else {
                            Toast.makeText(this@DashboardActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        println(response.toString())
                        Toast.makeText(this@DashboardActivity, "Error loading dashboard", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun updateDashboard(data: JSONObject) {
        val buses = data.optJSONArray("buses")
        val notifications = data.optJSONArray("notifications")

        // Check if buses array has data
        if (buses != null && buses.length() > 0) {
            val bus1 = buses.getJSONObject(0)
            busInfo1.text = bus1.getString("busNumber")
            busLocation1.text = "Status: ${bus1.getString("status")}"

            if (buses.length() > 1) {
                val bus2 = buses.getJSONObject(1)
                busInfo2.text = bus2.getString("busNumber")
                busLocation2.text = "Status: ${bus2.getString("status")}"
            } else {
                busInfo2.text = "No second bus available"
                busLocation2.text = ""
            }
        } else {
            // No buses available
            busInfo1.text = "No Data Available"
            busLocation1.text = ""
            busInfo2.text = ""
            busLocation2.text = ""
        }

        // Check if notifications array has data
        if (notifications != null && notifications.length() > 0) {
            messageTitle1.text = notifications.getString(0)
            messageTime1.text = "2d ago" // Example timing

            if (notifications.length() > 1) {
                messageTitle2.text = notifications.getString(1)
                messageTime2.text = "4d ago" // Example timing
            } else {
                messageTitle2.text = "No additional notifications"
                messageTime2.text = ""
            }
        } else {
            // No notifications available
            messageTitle1.text = "No Data Available"
            messageTime1.text = ""
            messageTitle2.text = ""
            messageTime2.text = ""
        }

        // Display placeholder event data (static placeholders for now)
        eventTitle1.text = "Winter Break"
        eventDate1.text = "Dec 20 - Jan 3"

        eventTitle2.text = "No School"
        eventDate2.text = "Jan 17"
    }

    // Navigation methods
    private fun navigateToMessages() {
        val intent = Intent(this, MessagesActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        // Example: Navigating to home screen (if you have a dedicated activity for that)
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToVolunteerOpportunities() {
        val intent = Intent(this, VolunteerActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToNotifications() {
       // val intent = Intent(this, NotificationsActivity::class.java)
       // startActivity(intent)
    }

    private fun navigateToProfile() {
      //  val intent = Intent(this, ProfileActivity::class.java)
      //  startActivity(intent)
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private lateinit var bottomNavigationView: BottomNavigationView

    private val apiService = ApiService()

    @SuppressLint("MissingInflatedId")
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


        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Stay on Dashboard
                    Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_events -> {
                    val intent = Intent(this, VolunteerActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_tracking_bus -> {
                    val intent = Intent(this, ViewBusScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_profile -> {
                    val intent = Intent(this, UserProfile::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        // Retrieve the token from shared preferences
        val sharedPref: SharedPreferences = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)

        if (token != null) {
            // Fetch dashboard data using the token
            fetchDashboardData(token)
        } else {
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@DashboardActivity, "Error loading dashboard", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateDashboard(data: JSONObject) {
        val buses = data.optJSONArray("buses")
        val notifications = data.optJSONArray("notifications")

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
            busInfo1.text = "No Data Available"
            busLocation1.text = ""
            busInfo2.text = ""
            busLocation2.text = ""
        }

        if (notifications != null && notifications.length() > 0) {
            messageTitle1.text = notifications.getString(0)
            messageTime1.text = "2d ago"

            if (notifications.length() > 1) {
                messageTitle2.text = notifications.getString(1)
                messageTime2.text = "4d ago"
            } else {
                messageTitle2.text = "No additional notifications"
                messageTime2.text = ""
            }
        } else {
            messageTitle1.text = "No Data Available"
            messageTime1.text = ""
            messageTitle2.text = ""
            messageTime2.text = ""
        }

        eventTitle1.text = "Winter Break"
        eventDate1.text = "Dec 20 - Jan 3"


    }
}

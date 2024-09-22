package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONObject
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class ViewBusScheduleActivity : AppCompatActivity() {

    private lateinit var txtRouteA: TextView
    private lateinit var txtRouteB: TextView
    private lateinit var txtRouteC: TextView

    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_bus_schedule)

        // Initialize views
        txtRouteA = findViewById(R.id.txtRouteA)
        txtRouteB = findViewById(R.id.txtRouteB)
        txtRouteC = findViewById(R.id.txtRouteC)

        // Fetch bus schedule from API
        val token = "your-jwt-token-here" // Replace with actual token
        val busId = "busId1" // Replace with the actual bus ID you want to get the schedule for
        fetchBusSchedule(token, busId)
    }

    private fun fetchBusSchedule(token: String, busId: String) {
        apiService.getBusSchedule(token, busId, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ViewBusScheduleActivity, "Failed to load schedule", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseData = response.body?.string()
                runOnUiThread {
                    if (response.isSuccessful && responseData != null) {
                        try {
                            val schedule = JSONObject(responseData)
                            updateSchedule(schedule)
                        } catch (e: Exception) {
                            Toast.makeText(this@ViewBusScheduleActivity, "Error parsing schedule", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@ViewBusScheduleActivity, "Failed to fetch schedule", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun updateSchedule(schedule: JSONObject) {
        // Assuming the API response contains the routes and times, you would adjust this based on actual data
        txtRouteA.text = "Route A: Pick Up - ${schedule.getString("routeA_pickup")}, Drop Off - ${schedule.getString("routeA_dropoff")}"
        txtRouteB.text = "Route B: Pick Up - ${schedule.getString("routeB_pickup")}, Drop Off - ${schedule.getString("routeB_dropoff")}"
        txtRouteC.text = "Route C: Pick Up - ${schedule.getString("routeC_pickup")}, Drop Off - ${schedule.getString("routeC_dropoff")}"
    }
}

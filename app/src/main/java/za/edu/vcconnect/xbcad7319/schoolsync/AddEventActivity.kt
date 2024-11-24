package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class AddEventActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        // Find views
        val etEventName: EditText = findViewById(R.id.etEventName)
        val etEventDetails: EditText = findViewById(R.id.etEventDetails)
        val etEventDate: EditText = findViewById(R.id.etEventDate)
        val etEventLocation: EditText = findViewById(R.id.etEventLocation)
        val etCreatedBy: EditText = findViewById(R.id.etCreatedBy)
        val btnSubmitEvent: Button = findViewById(R.id.btnSubmitEvent)

        // Submit button click listener
        btnSubmitEvent.setOnClickListener {
            val eventName = etEventName.text.toString()
            val eventDetails = etEventDetails.text.toString()
            val eventDate = etEventDate.text.toString()
            val eventLocation = etEventLocation.text.toString()
            val createdBy = etCreatedBy.text.toString()

            // Validate input
            if (eventName.isEmpty() || eventDetails.isEmpty() || eventDate.isEmpty() || eventLocation.isEmpty() || createdBy.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Prepare event data as JSON
            val eventData = JSONObject().apply {
                put("eventName", eventName)
                put("details", eventDetails)
                put("date", eventDate)
                put("location", JSONObject().apply {
                    put("type", "Point")
                    put("coordinates", eventLocation.split(",").map { it.trim().toDouble() })
                })
                put("createdBy", createdBy)
            }

            // Send data to backend
            submitEvent(eventData)
        }
    }

    private fun submitEvent(eventData: JSONObject) {
        val client = OkHttpClient()
        val requestBody = eventData.toString().toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://your-backend-api.com/events") // Replace with your API URL
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AddEventActivity, "Failed to add event: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddEventActivity, "Event added successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after successful submission
                    } else {
                        Toast.makeText(this@AddEventActivity, "Failed to add event: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
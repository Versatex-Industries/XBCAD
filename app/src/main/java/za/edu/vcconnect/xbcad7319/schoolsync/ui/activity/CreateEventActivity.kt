package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.EventCreateRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Location
import java.text.SimpleDateFormat
import java.util.*

class CreateEventActivity : AppCompatActivity() {

    private lateinit var eventNameInput: EditText
    private lateinit var eventDetailsInput: EditText
    private lateinit var eventDateInput: EditText
    private lateinit var eventTimeInput: EditText
    private lateinit var eventLocationInput: EditText
    private lateinit var createButton: Button

    private val calendar = Calendar.getInstance()
    private var selectedLocation: LatLng? = null // Store the selected latitude and longitude

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_event)

        // Initialize UI elements
        eventNameInput = findViewById(R.id.eventNameInput)
        eventDetailsInput = findViewById(R.id.eventDetailsInput)
        eventDateInput = findViewById(R.id.eventDateInput)
        eventTimeInput = findViewById(R.id.eventTimeInput)
        eventLocationInput = findViewById(R.id.eventLocationInput)
        createButton = findViewById(R.id.createButton)

        // Date Picker for Event Date
        eventDateInput.setOnClickListener { showDatePicker() }

        // Time Picker for Event Time
        eventTimeInput.setOnClickListener { showTimePicker() }

        // Location Picker for Event Location
        eventLocationInput.setOnClickListener { showLocationPicker() }

        // Create Event Button Click
        createButton.setOnClickListener { createEvent() }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInput()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeInput()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun showLocationPicker() {
        val intent = Intent(this, LocationPickerActivity::class.java)
        startActivityForResult(intent, LOCATION_PICKER_REQUEST)
    }

    private fun updateDateInput() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        eventDateInput.setText(dateFormat.format(calendar.time))
    }

    private fun updateTimeInput() {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        eventTimeInput.setText(timeFormat.format(calendar.time))
    }

    private fun createEvent() {
        val name = eventNameInput.text.toString()
        val details = eventDetailsInput.text.toString()
        val date = "${eventDateInput.text}T${eventTimeInput.text}"
        val location = selectedLocation

        // Validate input
        if (name.isEmpty() || details.isEmpty() || date.isEmpty() || location == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val eventRequest = EventCreateRequest(
            eventName = name,
            details = details,
            date = date,
            location = Location("Point", listOf(location.longitude, location.latitude))
        )

        // Make API call to create the event
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.createEvent("Bearer ${getToken()}", eventRequest)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@CreateEventActivity, "Event created successfully", Toast.LENGTH_SHORT).show()

                        // Start a new instance of EventsActivity
                        val intent = Intent(this@CreateEventActivity, EventsActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)

                        finish() // Finish CreateEventActivity
                    }
                } else {
                    showError("Failed to create event: ${response.message()}")
                }
            } catch (e: Exception) {
                showError(e.message ?: "An error occurred")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@CreateEventActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LOCATION_PICKER_REQUEST && resultCode == RESULT_OK) {
            val latitude = data?.getDoubleExtra("latitude", 0.0) ?: 0.0
            val longitude = data?.getDoubleExtra("longitude", 0.0) ?: 0.0
            selectedLocation = LatLng(latitude, longitude)
            eventLocationInput.setText("Lat: $latitude, Lng: $longitude")
        }
    }

    companion object {
        private const val LOCATION_PICKER_REQUEST = 1001
    }
}

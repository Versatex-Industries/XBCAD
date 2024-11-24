package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassResponse

class CaptureTimetableActivity : AppCompatActivity() {

    private lateinit var classSpinner: Spinner
    private lateinit var daySpinner: Spinner
    private lateinit var etPeriod: EditText
    private lateinit var etSubject: EditText
    private lateinit var saveButton: Button

    private val classes = mutableListOf<ClassResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_timetable)

        // Initialize UI components
        classSpinner = findViewById(R.id.classSpinner)
        daySpinner = findViewById(R.id.daySpinner)
        etPeriod = findViewById(R.id.etPeriod)
        etSubject = findViewById(R.id.etSubject)
        saveButton = findViewById(R.id.saveButton)

        // Load classes
        loadClasses()

        // Set up day spinner
        val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
        val dayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, days)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        daySpinner.adapter = dayAdapter

        // Save button functionality
        saveButton.setOnClickListener { saveTimetableEntry() }
    }

    private fun loadClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getClasses("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        classes.clear()
                        classes.addAll(response.body() ?: emptyList())
                        val classNames = classes.map { it.name }
                        val adapter = ArrayAdapter(this@CaptureTimetableActivity, android.R.layout.simple_spinner_item, classNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        classSpinner.adapter = adapter
                    }
                } else {
                    showError("Failed to load classes: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading classes: ${e.message}")
            }
        }
    }

    private fun saveTimetableEntry() {
        val selectedClassIndex = classSpinner.selectedItemPosition
        val selectedClassId = if (selectedClassIndex >= 0) classes[selectedClassIndex]._id else null
        val selectedDay = daySpinner.selectedItem.toString()
        val period = etPeriod.text.toString()
        val subject = etSubject.text.toString()

        if (selectedClassId.isNullOrEmpty() || selectedDay.isEmpty() || period.isEmpty() || subject.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val payload = hashMapOf(
                    "classId" to selectedClassId,
                    "day" to selectedDay,
                    "period" to period,
                    "subject" to subject
                )

                val response = api.addTimetableEntry("Bearer ${getToken()}", payload)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CaptureTimetableActivity,
                            "Timetable entry saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                    } else {
                        showError("Failed to save timetable entry: ${response.message()}")
                    }
                }
            } catch (e: HttpException) {
                e.printStackTrace()
                showError("Error saving timetable entry: ${e.message}")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@CaptureTimetableActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.StudentResponse

class CaptureAttendanceActivity : AppCompatActivity() {

    private lateinit var classSpinner: Spinner
    private lateinit var studentSpinner: Spinner
    private lateinit var dateInput: EditText
    private lateinit var statusSpinner: Spinner
    private lateinit var remarksInput: EditText
    private lateinit var submitButton: Button

    private val classes = mutableListOf<ClassResponse>()
    private val students = mutableListOf<StudentResponse>()
    private lateinit var selectedClassId: String
    private lateinit var selectedStudentId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_capture_attendance)

        // Initialize UI components
        classSpinner = findViewById(R.id.classSpinner)
        studentSpinner = findViewById(R.id.studentSpinner)
        dateInput = findViewById(R.id.dateInput)
        statusSpinner = findViewById(R.id.statusSpinner)
        remarksInput = findViewById(R.id.remarksInput)
        submitButton = findViewById(R.id.submitAttendanceButton)

        // Load classes
        loadClasses()

        // Set up status options
        val statuses = listOf("Present", "Absent", "Late")
        val statusAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statuses)
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        statusSpinner.adapter = statusAdapter

        // Class selection listener
        classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedClassId = classes[position]._id
                loadStudents(selectedClassId)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Submit button listener
        submitButton.setOnClickListener {
            markAttendance()
        }
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
                        val classNames = classes.map { classObj ->
                            classObj.name
                        }
                        val adapter = ArrayAdapter(
                            this@CaptureAttendanceActivity,
                            android.R.layout.simple_spinner_item,
                            classNames
                        )
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



    private fun loadStudents(classId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getStudentsInClass("Bearer ${getToken()}", classId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        students.clear()
                        students.addAll(response.body() ?: emptyList())
                        val studentNames = students.map { "${it.profile.name} ${it.profile.surname}" }
                        val adapter = ArrayAdapter(this@CaptureAttendanceActivity, android.R.layout.simple_spinner_item, studentNames)
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        studentSpinner.adapter = adapter
                    }
                } else {
                    showError("Failed to load students: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading students: ${e.message}")
            }
        }
    }

    private fun markAttendance() {
        selectedStudentId = students[studentSpinner.selectedItemPosition]._id
        val date = dateInput.text.toString()
        val status = statusSpinner.selectedItem.toString()
        val remarks = remarksInput.text.toString()

        if (date.isEmpty() || status.isEmpty()) {
            Toast.makeText(this, "Date and status are required", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val payload = mapOf(
                    "studentId" to selectedStudentId,
                    "date" to date,
                    "status" to status,
                    "remarks" to remarks,
                    "classId" to selectedClassId
                )
                val response = api.markAttendance("Bearer ${getToken()}", payload)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CaptureAttendanceActivity, "Attendance marked successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        showError("Failed to mark attendance: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                showError("Error marking attendance: ${e.message}")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        System.err.println(message)
        withContext(Dispatchers.Main) {
            Toast.makeText(this@CaptureAttendanceActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.view.View
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

class CaptureGradeActivity : AppCompatActivity() {

    private lateinit var studentSpinner: Spinner
    private lateinit var etSubject: EditText
    private lateinit var etGrade: EditText
    private lateinit var etRemarks: EditText
    private lateinit var saveGradeButton: Button
    private val students = mutableListOf<String>() // To store student IDs and names
    private val studentIdMap = mutableMapOf<String, String>() // Map student names to IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_grade)

        studentSpinner = findViewById(R.id.studentSpinner)
        etSubject = findViewById(R.id.etSubject)
        etGrade = findViewById(R.id.etGrade)
        etRemarks = findViewById(R.id.etRemarks)
        saveGradeButton = findViewById(R.id.saveGradeButton)

        loadStudents()

        saveGradeButton.setOnClickListener {
            captureGrade()
        }
    }

    private fun loadStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getStudentsFromClasses("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val studentList = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        students.clear()
                        studentIdMap.clear()
                        for (student in studentList) {
                            val name = "${student.profile.name} ${student.profile.surname}"
                            if (!studentIdMap.containsKey(name)) { // Avoid duplicates
                                students.add(name)
                                studentIdMap[name] = student.id
                            }
                        }
                        val adapter = ArrayAdapter(
                            this@CaptureGradeActivity,
                            android.R.layout.simple_spinner_item,
                            students
                        )
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        studentSpinner.adapter = adapter
                    }
                } else {
                    handleError("Failed to load students: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Error loading students")
            }
        }
    }


    private fun captureGrade() {
        val selectedStudent = studentSpinner.selectedItem?.toString()
        if (selectedStudent.isNullOrEmpty()) {
            showToast("Please select a student")
            return
        }

        val studentId = studentIdMap[selectedStudent] ?: ""
        val subject = etSubject.text.toString().trim()
        val grade = etGrade.text.toString().trim()
        val remarks = etRemarks.text.toString().trim()

        if (studentId.isEmpty() || subject.isEmpty() || grade.isEmpty()) {
            showToast("Please fill all required fields")
            return
        }

        val gradeData = hashMapOf(
            "studentId" to studentId,
            "subject" to subject,
            "grade" to grade,
            "remarks" to remarks
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.captureGrade("Bearer ${getToken()}", gradeData)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        showToast("Grade saved successfully")
                        finish() // Close the activity
                    }
                } else {
                    handleError("Failed to save grade: ${response.message()}")
                }
            } catch (e: HttpException) {
                handleError(e.message ?: "Error saving grade")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private suspend fun handleError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@CaptureGradeActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class CaptureAttendanceActivity : AppCompatActivity() {

    private lateinit var spinnerClass: Spinner
    private lateinit var spinnerGrade: Spinner
    private lateinit var studentListContainer: LinearLayout
    private lateinit var submitAttendanceButton: Button
    private val selectedAttendance = mutableListOf<Pair<String, Boolean>>() // Student ID and Attendance Status

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_attendance)

        spinnerClass = findViewById(R.id.spinnerClass)
        spinnerGrade = findViewById(R.id.spinnerGrade)
        studentListContainer = findViewById(R.id.studentListContainer)
        submitAttendanceButton = findViewById(R.id.btnSubmitAttendance)

        // Fetch and populate classes and grades
        fetchClassesAndGrades()

        // Set listeners for filters
        spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchStudents()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        spinnerGrade.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchStudents()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Submit Attendance
        submitAttendanceButton.setOnClickListener {
            submitAttendance()
        }
    }

    private fun fetchClassesAndGrades() {
        // Mock data for classes and grades
        val classes = listOf("Class A", "Class B", "Class C")
        val grades = listOf("Grade 1", "Grade 2", "Grade 3")

        val classAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classes)
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClass.adapter = classAdapter

        val gradeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, grades)
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGrade.adapter = gradeAdapter
    }

    private fun fetchStudents() {
        val selectedClass = spinnerClass.selectedItem.toString()
        val selectedGrade = spinnerGrade.selectedItem.toString()

        // Simulate API call to fetch students filtered by class and grade
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://your-backend-api.com/students?class=$selectedClass&grade=$selectedGrade")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@CaptureAttendanceActivity, "Failed to load students: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = JSONArray(response.body?.string())
                runOnUiThread {
                    displayStudents(jsonResponse)
                }
            }
        })
    }

    private fun displayStudents(students: JSONArray) {
        studentListContainer.removeAllViews()
        selectedAttendance.clear()

        for (i in 0 until students.length()) {
            val student = students.getJSONObject(i)
            val studentId = student.getString("_id")
            val studentName = student.getString("name")

            val studentView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
            }

            val nameTextView = TextView(this).apply {
                text = studentName
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val attendanceCheckbox = CheckBox(this).apply {
                setOnCheckedChangeListener { _, isChecked ->
                    val attendance = Pair(studentId, isChecked)
                    selectedAttendance.removeIf { it.first == studentId }
                    selectedAttendance.add(attendance)
                }
            }

            studentView.addView(nameTextView)
            studentView.addView(attendanceCheckbox)
            studentListContainer.addView(studentView)
        }
    }

    private fun submitAttendance() {
        val client = OkHttpClient()
        val attendanceArray = JSONArray()

        for (attendance in selectedAttendance) {
            val attendanceObject = JSONObject().apply {
                put("studentId", attendance.first)
                put("present", attendance.second)
            }
            attendanceArray.put(attendanceObject)
        }

        val requestBody = attendanceArray.toString()
            .toRequestBody("application/json".toMediaTypeOrNull())

        val request = Request.Builder()
            .url("https://your-backend-api.com/attendance")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@CaptureAttendanceActivity, "Failed to submit attendance: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CaptureAttendanceActivity, "Attendance submitted successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@CaptureAttendanceActivity, "Failed to submit attendance: ${response.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}
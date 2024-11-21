package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class ParentViewAttendance : AppCompatActivity() {

    private lateinit var spinnerChildren: Spinner
    private lateinit var attendanceListContainer: LinearLayout

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance)



        spinnerChildren = findViewById(R.id.spinnerChildren)
        attendanceListContainer = findViewById(R.id.attendanceListContainer)

        // Fetch and populate children
        fetchChildren()

        // Set listener for selected child
        spinnerChildren.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                fetchAttendance(spinnerChildren.selectedItem.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchChildren() {
        // Simulated backend call to fetch children
        val children = listOf("John Doe", "Jane Doe") // Replace with API call result
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, children)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerChildren.adapter = adapter
    }

    private fun fetchAttendance(childName: String) {
        // Simulated API call to fetch attendance
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://your-backend-api.com/attendance?childName=$childName") // Replace with your actual API URL
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@ParentViewAttendance, "Failed to load attendance: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = JSONArray(response.body?.string())
                runOnUiThread {
                    displayAttendance(jsonResponse)
                }
            }
        })
    }

    private fun displayAttendance(attendanceData: JSONArray) {
        attendanceListContainer.removeAllViews()

        for (i in 0 until attendanceData.length()) {
            val attendance = attendanceData.getJSONObject(i)
            val date = attendance.getString("date")
            val status = attendance.getBoolean("present")

            val attendanceView = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                setPadding(8, 8, 8, 8)
            }

            val dateTextView = TextView(this).apply {
                text = date
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            }

            val statusTextView = TextView(this).apply {
                text = if (status) "Present" else "Absent"
                setTextColor(if (status) android.graphics.Color.GREEN else android.graphics.Color.RED)
            }

            attendanceView.addView(dateTextView)
            attendanceView.addView(statusTextView)
            attendanceListContainer.addView(attendanceView)
        }
    }
}
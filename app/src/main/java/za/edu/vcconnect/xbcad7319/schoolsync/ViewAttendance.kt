package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewAttendance : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var attendanceContainer: LinearLayout
    private lateinit var scrollView: ScrollView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance) // Ensure this matches your XML filename

        // Initialize views
        backButton = findViewById(R.id.imageButton)
        scrollView = findViewById(R.id.ViewAttendanceScrollView)

        // Set a click listener for the back button
        backButton.setOnClickListener {
            finish() // Close this activity and go back
        }

        // Load attendance data (replace with real data)
        val attendanceData = loadAttendanceData()

        // Populate attendance records into the ScrollView
        displayAttendanceRecords(attendanceData)
    }

    /**
     * A method to load attendance data.
     * Replace this with your API or database call to fetch real attendance data.
     */
    private fun loadAttendanceData(): List<Pair<String, String>> {
        return listOf(
            "Math" to "Present (10/10)",
            "Science" to "Absent (8/10)",
            "English" to "Present (9/10)",
            "History" to "Absent (7/10)",
            "Physical Education" to "Present (10/10)"
        )
    }

    /**
     * A method to dynamically add attendance records to the ScrollView.
     */
    @SuppressLint("SetTextI18n")
    private fun displayAttendanceRecords(attendanceData: List<Pair<String, String>>) {
        for (record in attendanceData) {
            val subjectTextView = TextView(this).apply {
                text = "${record.first} - ${record.second}"
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black, theme))
                setPadding(0, 8, 0, 8)
            }
            attendanceContainer.addView(subjectTextView)
        }
    }
}
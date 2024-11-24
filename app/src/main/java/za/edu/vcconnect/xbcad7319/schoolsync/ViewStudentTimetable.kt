package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ViewStudentTimetable : AppCompatActivity() {
    private lateinit var titleText: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_student_timetable) // Ensure this matches your XML file name

        // Initialize views
        titleText = findViewById(R.id.titleText)

        // Set title
        titleText.text = "View Timetable"

        // Optionally, you can fetch and update timetable data dynamically
        displayTimetable(getStudentTimetable()) // Replace with your API or database fetching logic
    }

    // Method to populate timetable dynamically (you can replace this with real data)
    private fun displayTimetable(timetable: List<String>) {
        val timetableContainer: LinearLayout = findViewById(R.id.StudentTimetableContainer)

        timetable.forEach { entry ->
            val textView = TextView(this).apply {
                text = entry
                textSize = 16f
                setTextColor(resources.getColor(android.R.color.black))
                setPadding(0, 8, 0, 8)
            }
            timetableContainer.addView(textView)
        }
    }

    // Dummy data for timetable (replace this with real data fetching logic)
    private fun getStudentTimetable(): List<String> {
        return listOf(
            "Monday - Math, Science, English",
            "Tuesday - History, Geography, Biology",
            "Wednesday - Chemistry, Art, Physical Education",
            "Thursday - Literature, Physics, Computer Science",
            "Friday - Economics, Business Studies, Math"
        )
    }
}
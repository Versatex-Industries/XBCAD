package za.edu.vcconnect.xbcad7319.schoolsync

import android.graphics.Color
import android.os.Bundle
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TeacherTimetableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_timetable)

        // Find the GridLayout
        val timetableGrid = findViewById<GridLayout>(R.id.timetableGrid)

        // Days of the week
        val daysOfWeek = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")

        // Time slots
        val timeSlots = arrayOf(
            "8:00-9:00",
            "9:00-10:00",
            "10:00-11:00",
            "11:00-12:00",
            "12:00-1:00",
            "1:00-2:00",
            "2:00-3:00"
        )

        // Sample subjects for the timetable
        val subjects = arrayOf(
            arrayOf("Math", "History", "English", "Science", "PE"),
            arrayOf("Art", "Physics", "Chemistry", "Music", "Break"),
            arrayOf("Math", "History", "English", "Science", "PE"),
            arrayOf("Art", "Physics", "Chemistry", "Music", "Break"),
            arrayOf("Math", "History", "English", "Science", "PE"),
            arrayOf("Art", "Break", "Chemistry", "Music", "PE"),
            arrayOf("Math", "Physics", "English", "Science", "Break")
        )

        // Populate the timetable dynamically
        populateTimetable(timetableGrid, daysOfWeek, timeSlots, subjects)
    }

    private fun populateTimetable(gridLayout: GridLayout, daysOfWeek: Array<String>, timeSlots: Array<String>, subjects: Array<Array<String>>) {
        // Set the column count for the GridLayout (Time column + 5 days)
        gridLayout.columnCount = daysOfWeek.size + 1

        // Add the first header row
        addHeaderRow(gridLayout, daysOfWeek)

        // Add rows for each time slot
        for (rowIndex in timeSlots.indices) {
            // Add time slot column
            addTimeSlot(gridLayout, timeSlots[rowIndex])

            // Add subjects for each day of the week
            for (dayIndex in daysOfWeek.indices) {
                addSubject(gridLayout, subjects[rowIndex][dayIndex])
            }
        }
    }

    private fun addHeaderRow(gridLayout: GridLayout, daysOfWeek: Array<String>) {
        // Add an empty cell for the top-left corner
        val emptyCell = TextView(this).apply {
            text = "Time/Day"
            setBackgroundColor(Color.parseColor("#007BFF"))
            setTextColor(Color.WHITE)
            textSize = 14f
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        gridLayout.addView(emptyCell)

        // Add headers for each day
        for (day in daysOfWeek) {
            val headerCell = TextView(this).apply {
                text = day
                setBackgroundColor(Color.parseColor("#007BFF"))
                setTextColor(Color.WHITE)
                textSize = 14f
                setPadding(8, 8, 8, 8)
                gravity = android.view.Gravity.CENTER
            }
            gridLayout.addView(headerCell)
        }
    }

    private fun addTimeSlot(gridLayout: GridLayout, timeSlot: String) {
        val timeSlotCell = TextView(this).apply {
            text = timeSlot
            setBackgroundColor(Color.parseColor("#DDDDDD"))
            textSize = 14f
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        gridLayout.addView(timeSlotCell)
    }

    private fun addSubject(gridLayout: GridLayout, subject: String) {
        val subjectCell = TextView(this).apply {
            text = subject
            setBackgroundColor(
                if (subject == "Break") Color.parseColor("#FFC107") else Color.WHITE
            )
            textSize = 14f
            setPadding(8, 8, 8, 8)
            gravity = android.view.Gravity.CENTER
        }
        gridLayout.addView(subjectCell)
    }
}
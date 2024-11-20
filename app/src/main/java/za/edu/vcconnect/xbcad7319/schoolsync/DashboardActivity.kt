package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : AppCompatActivity() {

    // Views
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var busTrackingButton: Button
    private lateinit var updatesButton: Button
    private lateinit var eventsButton: Button

    private lateinit var studentOptionsGroup: LinearLayout
    private lateinit var teacherOptionsGroup: LinearLayout
    private lateinit var parentOptionsGroup: LinearLayout

    // Student-specific buttons
    private lateinit var viewClassesButton: Button
    private lateinit var viewBusRouteButton: Button
    private lateinit var viewTimetableStudentButton: Button
    private lateinit var viewGradesStudentButton: Button
    private lateinit var viewAttendanceStudentButton: Button

    // Teacher-specific buttons
    private lateinit var captureGradesButton: Button
    private lateinit var captureAttendanceButton: Button
    private lateinit var viewLessonPlanButton: Button
    private lateinit var viewTimetableTeacherButton: Button
    private lateinit var addEventsButton: Button

    // Parent-specific buttons
    private lateinit var viewAttendanceParentButton: Button
    private lateinit var viewGradesButton: Button
    private lateinit var viewBusRoutesParentButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard)

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        busTrackingButton = findViewById(R.id.busTrackingButton)
        updatesButton = findViewById(R.id.notificationsButton)
        eventsButton = findViewById(R.id.eventsButton)

        studentOptionsGroup = findViewById(R.id.studentOptionsGroup)
        teacherOptionsGroup = findViewById(R.id.teacherOptionsGroup)
        parentOptionsGroup = findViewById(R.id.parentOptionsGroup)

        // Student buttons
        viewClassesButton = findViewById(R.id.viewClassesButton)
        viewBusRouteButton = findViewById(R.id.viewBusRouteButton)
        viewTimetableStudentButton = findViewById(R.id.viewTimetableStudentButton)
        viewGradesStudentButton = findViewById(R.id.viewGradesStudentButton)
        viewAttendanceStudentButton = findViewById(R.id.viewAttendanceStudentButton)

        // Teacher buttons
        captureGradesButton = findViewById(R.id.captureGradesButton)
        captureAttendanceButton = findViewById(R.id.captureAttendanceButton)
        viewLessonPlanButton = findViewById(R.id.viewLessonPlanButton)
        viewTimetableTeacherButton = findViewById(R.id.viewTimetableTeacherButton)
        addEventsButton = findViewById(R.id.addEventsButton)

        // Parent buttons
        viewAttendanceParentButton = findViewById(R.id.viewAttendanceParentButton)
        viewGradesButton = findViewById(R.id.viewGradesButton)
        viewBusRoutesParentButton = findViewById(R.id.viewBusRoutesParentButton)

        // Set role-based visibility
        setupUserRole("Parent") // Change role here: "Student", "Teacher", "Parent"

        // Set onClick listeners for global buttons
        busTrackingButton.setOnClickListener {
            Toast.makeText(this, "Bus Tracking Clicked", Toast.LENGTH_SHORT).show()
        }

        updatesButton.setOnClickListener {
            Toast.makeText(this, "Notifications Clicked", Toast.LENGTH_SHORT).show()
        }

        eventsButton.setOnClickListener {
            Toast.makeText(this, "View Events/Volunteer Clicked", Toast.LENGTH_SHORT).show()
        }

        // Student button actions
        viewClassesButton.setOnClickListener { showToast("View Classes Clicked") }
        viewBusRouteButton.setOnClickListener { showToast("View Bus Route Clicked") }
        viewTimetableStudentButton.setOnClickListener { showToast("View Timetable Clicked") }
        viewGradesStudentButton.setOnClickListener { showToast("View Grades/Marks Clicked") }
        viewAttendanceStudentButton.setOnClickListener { showToast("View Attendance Clicked") }

        // Teacher button actions
        captureGradesButton.setOnClickListener { showToast("Capture Grades/Marks Clicked") }
        captureAttendanceButton.setOnClickListener { showToast("Capture Attendance Clicked") }
        viewLessonPlanButton.setOnClickListener { showToast("View Lesson Plan Clicked") }
        viewTimetableTeacherButton.setOnClickListener { showToast("View Timetable Clicked") }
        addEventsButton.setOnClickListener { showToast("Add Events Clicked") }

        // Parent button actions
        viewAttendanceParentButton.setOnClickListener { showToast("View Attendance Clicked") }
        viewGradesButton.setOnClickListener { showToast("View Grades Clicked") }
        viewBusRoutesParentButton.setOnClickListener { showToast("View Bus Routes Clicked") }

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_events -> {
                    val intent = Intent(this, VolunteerActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_tracking_bus -> {
                    val intent = Intent(this, ViewBusScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }
    }

    private fun setupUserRole(role: String) {
        // Hide all groups initially
        studentOptionsGroup.visibility = View.GONE
        teacherOptionsGroup.visibility = View.GONE
        parentOptionsGroup.visibility = View.GONE

        // Show the group based on role
        when (role) {
            "Student" -> studentOptionsGroup.visibility = View.VISIBLE
            "Teacher" -> teacherOptionsGroup.visibility = View.VISIBLE
            "Parent" -> parentOptionsGroup.visibility = View.VISIBLE
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

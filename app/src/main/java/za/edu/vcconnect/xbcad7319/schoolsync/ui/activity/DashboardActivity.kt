package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import java.util.Locale

class DashboardActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var studentOptionsGroup: LinearLayout
    private lateinit var teacherOptionsGroup: LinearLayout
    private lateinit var parentOptionsGroup: LinearLayout
    private lateinit var messagesButton: Button
    private lateinit var addStudentsButton: Button
    private lateinit var captureGradesButton: Button
    private lateinit var captureAttendanceButton: Button
    private lateinit var captureTimetableButton: Button
    private lateinit var viewTimetableTeacherButton: Button
    private lateinit var addEventsButton: Button
    private lateinit var viewTimetableStudentButton: Button
    private lateinit var viewGradesStudentButton: Button
    private lateinit var viewGradesButton: Button
    private lateinit var viewAttendanceStudentButton: Button
    private lateinit var viewAttendanceParentButton: Button
    private lateinit var eventsButton: Button
    private lateinit var sendNotificationButton: Button
    private lateinit var notificationsButton: Button
    private lateinit var busTrackingButton: Button

    private lateinit var settingsButton: ImageButton

    override fun attachBaseContext(newBase: Context?) {
        val sharedPreferences = newBase?.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val language = sharedPreferences?.getString("selected_language", "en") ?: "en"
        val locale = Locale(language)
        val config = Configuration()
        config.setLocale(locale)
        super.attachBaseContext(newBase?.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_dashboard)
        applySavedLocale()

        // Initialize UI components
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        studentOptionsGroup = findViewById(R.id.studentOptionsGroup)
        teacherOptionsGroup = findViewById(R.id.teacherOptionsGroup)
        parentOptionsGroup = findViewById(R.id.parentOptionsGroup)
        eventsButton = findViewById(R.id.eventsButton)
        messagesButton = findViewById(R.id.messagesButton)
        addStudentsButton = findViewById(R.id.addStudentsButton)
        captureGradesButton = findViewById(R.id.captureGradesButton)
        captureAttendanceButton = findViewById(R.id.captureAttendanceButton)
        captureTimetableButton = findViewById(R.id.captureTimetableButton)
        viewTimetableTeacherButton = findViewById(R.id.viewTimetableTeacherButton)
        addEventsButton = findViewById(R.id.addEventsButton)
        viewTimetableStudentButton = findViewById(R.id.viewTimetableStudentButton)
        viewGradesStudentButton = findViewById(R.id.viewGradesStudentButton)
        viewAttendanceStudentButton = findViewById(R.id.viewAttendanceStudentButton)
        viewAttendanceParentButton = findViewById(R.id.viewAttendanceParentButton)
        settingsButton = findViewById(R.id.settingsButton)
        sendNotificationButton = findViewById(R.id.sendNotificationButton)
        notificationsButton = findViewById(R.id.notificationsButton)
        busTrackingButton = findViewById(R.id.busTrackingButton)
        viewGradesButton = findViewById(R.id.viewGradesButton)

        // Fetch user role from intent or shared preferences
        val role = intent.getStringExtra("role") ?: getUserRoleFromPreferences()

        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("role", role)
        editor.apply()

        if (role.isNullOrEmpty()) {
            Toast.makeText(this@DashboardActivity, "Failed to get User role", Toast.LENGTH_SHORT)
                .show()
        } else {
            // Set visibility based on role
            updateDashboardForRole(role)
        }

        busTrackingButton.setOnClickListener {
            val intent = Intent(this, BusRoutesActivity::class.java)
            startActivity(intent)
        }
        notificationsButton.setOnClickListener {
            val intent = Intent(this, ViewNotificationsActivity::class.java)
            startActivity(intent)
        }

        sendNotificationButton.setOnClickListener {
            val intent = Intent(this, SendNotificationActivity::class.java)
            startActivity(intent)
        }

        viewAttendanceParentButton.setOnClickListener {
            intent.putExtra("destination", "attendance")
            val intent = Intent(this, ChildSelectorActivity::class.java)
            startActivity(intent)
        }

        eventsButton.setOnClickListener {
            val intent = Intent(this, EventsActivity::class.java)
            startActivity(intent)
        }

        viewAttendanceStudentButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val api = RetrofitClient.instance.create(ApiService::class.java)
                    val response = api.getProfile("Bearer ${getToken()}")
                    if (response.isSuccessful) {
                        val userId = response.body()?._id
                        withContext(Dispatchers.Main) {
                            if (userId != null) {
                                val intent = Intent(
                                    this@DashboardActivity,
                                    ViewAttendanceActivity::class.java
                                )
                                intent.putExtra("childId", userId)
                                startActivity(intent)
                            } else {
                                showError("User ID not found.")
                            }
                        }
                    } else {
                        showError("Failed to fetch user profile: ${response.message()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showError("Error fetching user profile: ${e.message}")
                }
            }
        }


        viewGradesStudentButton.setOnClickListener {
            val intent = Intent(this, ViewGradesActivity::class.java)
            startActivity(intent)
        }

        viewGradesButton.setOnClickListener {
            val intent = Intent(this, ChildSelectorActivity::class.java)
            intent.putExtra("destination", "grades")
            startActivity(intent)
        }

        // Set up click listener for the View Messages button
        messagesButton.setOnClickListener {
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            val intent = Intent(this@DashboardActivity, SettingsActivity::class.java)
            startActivity(intent)
        }
        // Set up click listener for the View Messages button
        captureAttendanceButton.setOnClickListener {
            val intent = Intent(this, CaptureAttendanceActivity::class.java)
            startActivity(intent)
        }
        // Set up click listener for the View Messages button
        captureTimetableButton.setOnClickListener {
            val intent = Intent(this, CaptureTimetableActivity::class.java)
            startActivity(intent)
        }

        viewTimetableStudentButton.setOnClickListener {
            val intent = Intent(this, ViewTimetableActivity::class.java)
            startActivity(intent)
        }

        // Set up click listener for the View Messages button
        viewTimetableTeacherButton.setOnClickListener {
            val intent = Intent(this, ViewTimetableActivity::class.java)
            startActivity(intent)
        }

        // Set up click listener for the View Messages button
        addEventsButton.setOnClickListener {
            val intent = Intent(this, CreateEventActivity::class.java)
            startActivity(intent)
        }

        captureGradesButton.setOnClickListener {
            val intent = Intent(this, CaptureGradeActivity::class.java)
            startActivity(intent)
        }

        // Set up click listener for Add Students button
        addStudentsButton.setOnClickListener {
            val intent = Intent(this, AddStudentsActivity::class.java)
            startActivity(intent)
        }

        // Handle BottomNavigationView item selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_profile -> {
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_events -> {
                    val intent = Intent(this, EventsActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_tracking_bus -> {
                    val intent = Intent(this, BusRoutesActivity::class.java)
                    startActivity(intent)
                    true
                }

                R.id.nav_logout -> {

                    // Clear user session and redirect to login
                    val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().clear().apply() // Clear stored preferences

                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
                    startActivity(intent)
                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    true
                }

                else -> false
            }
        }
    }

    private fun getUserRoleFromPreferences(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("role", null)
    }

    private fun updateDashboardForRole(role: String) {
        // Hide all groups by default
        studentOptionsGroup.visibility = View.GONE
        teacherOptionsGroup.visibility = View.GONE
        parentOptionsGroup.visibility = View.GONE

        // Show the relevant group based on the role
        when (role) {
            "Student" -> studentOptionsGroup.visibility = View.VISIBLE
            "Teacher" -> teacherOptionsGroup.visibility = View.VISIBLE
            "Parent" -> parentOptionsGroup.visibility = View.VISIBLE
            else -> {
                // Handle unknown roles (optional)
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private fun showError(message: String) {
        Toast.makeText(this@DashboardActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun applySavedLocale() {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("selected_language", "en") ?: "en"
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}

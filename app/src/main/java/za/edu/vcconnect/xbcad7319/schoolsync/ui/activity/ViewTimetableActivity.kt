package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.TimetableResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileResponse

class ViewTimetableActivity : AppCompatActivity() {

    private lateinit var timetableContainer: LinearLayout
    private var isTeacher: Boolean = false // Determines whether the user is a teacher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_view_timetable)

        timetableContainer = findViewById(R.id.timetableContainer)

        // Fetch user role and load timetable accordingly
        fetchUserRoleAndLoadTimetable()
    }

    private fun fetchUserRoleAndLoadTimetable() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val userProfile = response.body()
                    isTeacher = userProfile?.role == "Teacher"

                    val classId = if (isTeacher) {
                        // For teachers, allow selecting a class
                        // You can optionally implement a spinner or list
                        // Here, we assume the first managed class is selected by default
                        fetchFirstClassIdForTeacher()
                    } else {
                        // For students, use their assigned classId
                        userProfile?.profile?.classId
                    }

                    if (classId.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            showError("No class found for the user.")
                        }
                        return@launch
                    }

                    // Load timetable for the determined classId
                    loadTimetable(classId)
                } else {
                    showError("Failed to fetch user profile: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error fetching user profile: ${e.message}")
            }
        }
    }

    private suspend fun fetchFirstClassIdForTeacher(): String? {
        return try {
            val api = RetrofitClient.instance.create(ApiService::class.java)
            val response = api.getClasses("Bearer ${getToken()}")
            if (response.isSuccessful) {
                val classes = response.body() ?: emptyList()
                if (classes.isNotEmpty()) classes[0]._id else null
            } else {
                showError("Failed to fetch classes for teacher: ${response.message()}")
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Error fetching classes: ${e.message}")
            null
        }
    }

    private fun loadTimetable(classId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getTimetableForClass("Bearer ${getToken()}", classId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        timetableContainer.removeAllViews()
                        val timetable = response.body() ?: emptyList()
                        for (entry in timetable) {
                            addTimetableEntryToView(entry)
                        }
                    }
                } else {
                    showError("Failed to load timetable: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading timetable: ${e.message}")
            }
        }
    }

    private fun addTimetableEntryToView(entry: TimetableResponse) {
        val view = LayoutInflater.from(this).inflate(R.layout.item_timetable, timetableContainer, false)
        val dayText: TextView = view.findViewById(R.id.dayText)
        val periodText: TextView = view.findViewById(R.id.periodText)
        val subjectText: TextView = view.findViewById(R.id.subjectText)
        val teacherText: TextView = view.findViewById(R.id.teacherText)

        dayText.text = entry.day
        periodText.text = entry.period
        subjectText.text = entry.subject
        teacherText.text = entry.teacherName ?: "N/A"

        timetableContainer.addView(view)
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@ViewTimetableActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

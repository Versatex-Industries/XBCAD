package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.AttendanceResponse
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.AttendanceAdapter

class ViewAttendanceActivity : AppCompatActivity() {

    private lateinit var attendanceRecyclerView: RecyclerView
    private lateinit var adapter: AttendanceAdapter
    private val attendanceList = mutableListOf<AttendanceResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_attendance)

        attendanceRecyclerView = findViewById(R.id.attendanceRecyclerView)
        adapter = AttendanceAdapter(attendanceList)
        attendanceRecyclerView.layoutManager = LinearLayoutManager(this)
        attendanceRecyclerView.adapter = adapter

        val childId = intent.getStringExtra("childId")
        if (childId == null) {
            showError("Child not selected")
            finish()
            return
        }

        loadAttendance(childId)
    }

    private fun loadAttendance(childId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAttendanceForStudent("Bearer ${getToken()}", childId)
                if (response.isSuccessful) {
                    val fetchedAttendance = response.body() ?: emptyList()
                    withContext(Dispatchers.Main) {
                        attendanceList.clear()
                        attendanceList.addAll(fetchedAttendance)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load attendance: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading attendance: ${e.message}")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

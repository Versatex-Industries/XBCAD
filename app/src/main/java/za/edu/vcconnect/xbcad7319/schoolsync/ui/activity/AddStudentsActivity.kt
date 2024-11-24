package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.User
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.StudentsAdapter

class AddStudentsActivity : AppCompatActivity() {

    private lateinit var studentsRecyclerView: RecyclerView
    private lateinit var addStudentsButton: Button
    private val selectedStudentIds = mutableListOf<String>()
    private lateinit var adapter: StudentsAdapter
    private val students = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_students)

        studentsRecyclerView = findViewById(R.id.studentsRecyclerView)
        addStudentsButton = findViewById(R.id.addStudentsButton)

        // Set up RecyclerView
        adapter = StudentsAdapter(students) { selectedIds ->
            selectedStudentIds.clear()
            selectedStudentIds.addAll(selectedIds)
        }
        studentsRecyclerView.layoutManager = LinearLayoutManager(this)
        studentsRecyclerView.adapter = adapter

        // Load students without a class
        loadStudents()

        // Add students button
        addStudentsButton.setOnClickListener {
            if (selectedStudentIds.isNotEmpty()) {
                val intent = Intent(this, SelectClassActivity::class.java)
                intent.putStringArrayListExtra("studentIds", ArrayList(selectedStudentIds))
                startActivity(intent)

            } else {
                Toast.makeText(this, "Please select students to add.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getStudentsWithoutClass("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        students.clear()
                        students.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load students: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading students: ${e.message}")
            }
        }
    }


    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@AddStudentsActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

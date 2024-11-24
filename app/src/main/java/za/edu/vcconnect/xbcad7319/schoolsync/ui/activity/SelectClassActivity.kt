package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

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
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassResponse
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.ClassesAdapter

class SelectClassActivity : AppCompatActivity() {

    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var backButton: Button
    private lateinit var adapter: ClassesAdapter
    private val classes = mutableListOf<ClassResponse>()

    private lateinit var selectedStudentIds: List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_class)

        // Initialize UI components
        classesRecyclerView = findViewById(R.id.classesRecyclerView)
        backButton = findViewById(R.id.backButton)

        // Fetch selected student IDs from intent
        selectedStudentIds = intent.getStringArrayListExtra("studentIds") ?: emptyList()

        // Set up RecyclerView
        adapter = ClassesAdapter(classes) { selectedClass ->
            addStudentsToClass(selectedClass._id)
        }
        classesRecyclerView.layoutManager = LinearLayoutManager(this)
        classesRecyclerView.adapter = adapter

        // Load classes
        loadClasses()

        // Back button listener
        backButton.setOnClickListener { finish() }
    }

    private fun loadClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getClasses("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        classes.clear()
                        classes.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load classes: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading classes: ${e.message}")
            }
        }
    }

    private fun addStudentsToClass(classId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val payload = hashMapOf("studentIds" to selectedStudentIds)

                val response = api.addStudentsToClass("Bearer ${getToken()}", classId, payload)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SelectClassActivity,
                            "Students added successfully to the class",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        showError("Failed to add students: ${response.message()}")
                    }
                }
            } catch (e: Exception) {
                showError("Error adding students: ${e.message}")
                println(e)
            }
        }
    }


    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@SelectClassActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

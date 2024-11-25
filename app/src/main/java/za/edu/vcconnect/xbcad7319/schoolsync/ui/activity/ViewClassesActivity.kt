package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
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

class ViewClassesActivity : AppCompatActivity() {

    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var adapter: ClassesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_view_classes)

        // Initialize RecyclerView
        classesRecyclerView = findViewById(R.id.classesRecyclerView)
        classesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch classes
        fetchClasses()
    }

    private fun fetchClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getClasses("Bearer ${getToken()}")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { classes ->
                            adapter = ClassesAdapter(classes, onClassClick = { classItem ->
                                // Handle class click, e.g., show details
                                Toast.makeText(
                                    this@ViewClassesActivity,
                                    "Clicked: ${classItem.name}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                            classesRecyclerView.adapter = adapter
                        }
                    } else {
                        Toast.makeText(
                            this@ViewClassesActivity,
                            "Error fetching classes: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("ViewClassesError", "Error: ${e.message}")
                    Toast.makeText(
                        this@ViewClassesActivity,
                        "Failed to fetch classes.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}

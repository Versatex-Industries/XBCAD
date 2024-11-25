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
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.GradeResponse
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.GradeAdapter

class ViewGradesActivity : AppCompatActivity() {

    private lateinit var gradesRecyclerView: RecyclerView
    private lateinit var adapter: GradeAdapter
    private val grades = mutableListOf<GradeResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_grades)

        gradesRecyclerView = findViewById(R.id.gradesRecyclerView)
        adapter = GradeAdapter(grades)

        gradesRecyclerView.layoutManager = LinearLayoutManager(this)
        gradesRecyclerView.adapter = adapter

        val childId = intent.getStringExtra("childId")
        if (childId == null) {
           loadGrades()
        } else{
            loadChildGrades(childId)
        }

    }

    private fun loadGrades() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getGrades("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        grades.clear()
                        grades.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load grades: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading grades: ${e.message}")
            }
        }
    }

    private fun loadChildGrades(childId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getStudentGrades("Bearer ${getToken()}", childId)
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        grades.clear()
                        grades.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    showError("Failed to load grades: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading grades: ${e.message}")
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

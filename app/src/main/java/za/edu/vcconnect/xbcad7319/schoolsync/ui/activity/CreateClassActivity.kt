package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassCreationRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassCreationResponse

class CreateClassActivity : AppCompatActivity() {

    private lateinit var etClassName: EditText
    private lateinit var createClassButton: Button
    private lateinit var statusMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_create_class)

        // Initialize Views
        etClassName = findViewById(R.id.etClassName)
        createClassButton = findViewById(R.id.createClassButton)
        statusMessage = findViewById(R.id.statusMessage)

        // Set Create Class Button Listener
        createClassButton.setOnClickListener {
            val className = etClassName.text.toString().trim()
            if (className.isEmpty()) {
                Toast.makeText(this, "Class name is required", Toast.LENGTH_SHORT).show()
            } else {
                createClass(className)
            }
        }
    }

    private fun createClass(className: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response: Response<ClassCreationResponse> = api.createClass(
                    "Bearer ${getToken()}",
                    ClassCreationRequest(name = className)
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        body?.let {
                            statusMessage.text = it.message
                            statusMessage.visibility = View.VISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@CreateClassActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@CreateClassActivity,
                        "Failed to create class: ${e.message}",
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

package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class AddChildActivity : AppCompatActivity() {

    private lateinit var childNameInput: TextInputEditText
    private lateinit var gradeInput: TextInputEditText
    private lateinit var busRouteInput: TextInputEditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button

    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addchild) // Ensure this matches your layout file name

        // Initialize views
        childNameInput = findViewById(R.id.childNameInput)
        gradeInput = findViewById(R.id.gradeInput)
        busRouteInput = findViewById(R.id.busRouteInput)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)

        // Set up button listeners
        cancelButton.setOnClickListener {
            finish() // Close the activity without saving
        }

        saveButton.setOnClickListener {
            validateAndSaveChild()
        }
    }

    private fun validateAndSaveChild() {
        val childName = childNameInput.text.toString().trim()
        val grade = gradeInput.text.toString().trim()
        val busRoute = busRouteInput.text.toString().trim()

        // Validate input fields
        if (childName.isEmpty() || grade.isEmpty() || busRoute.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch token from shared preferences
        val sharedPref = getSharedPreferences("appPrefs", MODE_PRIVATE)
        val token = sharedPref.getString("jwtToken", null)

        if (token != null) {
            // Proceed to save the child's data using the API
            saveChild(childName, grade, busRoute, token)
        } else {
            Toast.makeText(this, "Token not found. Please log in again.", Toast.LENGTH_SHORT).show()
            // Optionally redirect to login screen if token is missing
        }
    }

    private fun saveChild(name: String, grade: String, busRoute: String, token: String) {
        val childData = JSONObject().apply {
            put("name", name)
            put("grade", grade)
            put("busRoute", busRoute)
        }

        apiService.addChild(childData, token, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@AddChildActivity, "Failed to add child", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AddChildActivity, "Child added successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity and go back
                    } else {
                        Toast.makeText(this@AddChildActivity, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}

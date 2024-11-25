package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileRequest

class CreateProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var studentSection: LinearLayout
    private lateinit var etGrade: EditText
    private lateinit var etClass: EditText
    private lateinit var parentSection: LinearLayout
    private lateinit var teacherSection: LinearLayout
    private lateinit var etSubject: EditText
    private lateinit var nextButton: Button

    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_create_profile)

        // Initialize Views
        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        studentSection = findViewById(R.id.studentSection)
        etGrade = findViewById(R.id.etGrade)
        parentSection = findViewById(R.id.parentSection)

        teacherSection = findViewById(R.id.teacherSection)
        etSubject = findViewById(R.id.etSubject)
        nextButton = findViewById(R.id.nextButton)

        // Get the role from Intent or SharedPreferences
        role = intent.getStringExtra("role") ?: getUserRoleFromPreferences()

        setupUIForRole(role)

        // Set Next Button Click Listener
        nextButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun getUserRoleFromPreferences(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("role", "Student") ?: "Student"
    }

    private fun setupUIForRole(role: String) {
        when (role) {
            "Student" -> studentSection.visibility = View.VISIBLE
            "Parent" -> parentSection.visibility = View.VISIBLE
            "Teacher" -> teacherSection.visibility = View.VISIBLE
        }
    }

    private fun saveProfile() {
        val name = etName.text.toString()
        val surname = etSurname.text.toString()
        val profileData = mutableMapOf<String, String>()

        profileData["name"] = name
        profileData["surname"] = surname

        when (role) {
            "Student" -> {
                profileData["grade"] = etGrade.text.toString()
               /* profileData["classId"] = etClass.text.toString()*/
            }
            "Parent" -> {
                profileData["linkedChildren"] = ""
            }
            "Teacher" -> {
                profileData["subject"] = etSubject.text.toString()
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.updateProfile(
                    "Bearer ${getToken()}",
                    UserProfileRequest(profileData)
                )
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@CreateProfileActivity,
                            "Profile saved successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToDashboard()
                    }
                } else {
                    handleError(response.message())
                }
            } catch (e: HttpException) {
                handleError(e.message())
            } catch (e: Exception) {
                handleError(e.localizedMessage ?: "An error occurred")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun handleError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@CreateProfileActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)

        finish()
    }
}

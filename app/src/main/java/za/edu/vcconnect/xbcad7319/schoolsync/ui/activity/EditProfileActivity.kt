package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassCreationRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassItem

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etSurname: EditText
    private lateinit var studentSection: LinearLayout
    private lateinit var etGrade: EditText
    private lateinit var spinnerClass: Spinner
    private lateinit var parentSection: LinearLayout
    private lateinit var etChildName: EditText
    private lateinit var teacherSection: LinearLayout
    private lateinit var etSubject: EditText
    private lateinit var saveButton: Button
    private lateinit var classList: MutableList<ClassItem> // List to store class data
    private lateinit var selectedClassId: String // Selected class ID

    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Initialize Views
        etName = findViewById(R.id.etName)
        etSurname = findViewById(R.id.etSurname)
        studentSection = findViewById(R.id.studentSection)
        etGrade = findViewById(R.id.etGrade)
        spinnerClass = findViewById(R.id.spinnerClass)
        parentSection = findViewById(R.id.parentSection)
        etChildName = findViewById(R.id.etChildName)
        teacherSection = findViewById(R.id.teacherSection)
        etSubject = findViewById(R.id.etSubject)
        saveButton = findViewById(R.id.saveButton)

        // Get user role
        role = getUserRoleFromPreferences()
        setupUIForRole(role)

        // Load data
        loadUserProfile()
        if (role == "Student") {
            fetchAllClasses()
        }

        // Set Save Button Listener
        saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun fetchAllClasses() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAllClasses("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    response.body()?.let { classes ->
                        withContext(Dispatchers.Main) {
                            populateClassDropdown(classes)
                        }
                    }
                } else {
                    handleError("Error fetching classes: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Error fetching classes")
            }
        }
    }

    private fun populateClassDropdown(classes: List<ClassItem>) {
        classList = classes.toMutableList()
        val classNames = classes.map { it.name } // Extract class names for dropdown
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, classNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerClass.adapter = adapter

        // Set default selected class if user profile has it
        spinnerClass.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedClassId = classes[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
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

    private fun loadUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val userResponse = response.body()
                    withContext(Dispatchers.Main) {
                        userResponse?.let { user ->
                            etName.setText(user.profile.name ?: "")
                            etSurname.setText(user.profile.surname ?: "")
                            if (user.role == "Student") {
                                etGrade.setText(user.profile.grade ?: "")
                                // Set default class based on profile data
                                val selectedClassIndex = classList.indexOfFirst { it.id == user.profile.classId }
                                if (selectedClassIndex != -1) {
                                    spinnerClass.setSelection(selectedClassIndex)
                                }
                            }
                        }
                    }
                } else {
                    handleError("Error fetching profile: ${response.message()}")
                }
            } catch (e: Exception) {
                handleError(e.message ?: "Error loading profile")
            }
        }
    }

    private fun saveProfile() {
        val profileData = mutableMapOf<String, String>()
        profileData["name"] = etName.text.toString()
        profileData["surname"] = etSurname.text.toString()

        if (role == "Student") {
            profileData["grade"] = etGrade.text.toString()
            profileData["classId"] = selectedClassId
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
                        Toast.makeText(this@EditProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity
                    }
                } else {
                    handleError("Error saving profile: ${response.message()}")
                }
            } catch (e: HttpException) {
                handleError(e.message ?: "Error saving profile")
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun handleError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@EditProfileActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}
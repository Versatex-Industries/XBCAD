package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ChildResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Profile
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.User
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileResponse

class AddChildrenActivity : AppCompatActivity() {

    private lateinit var spinnerStudents: Spinner
    private lateinit var btnAddChild: Button
    private lateinit var tvLinkedChildren: TextView

    private val linkedChildren = mutableListOf<String>() // Tracks current linked child IDs
    private val studentList = mutableListOf<User>() // Holds filtered student users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_children)

        // Initialize views
        spinnerStudents = findViewById(R.id.spinnerStudents)
        btnAddChild = findViewById(R.id.btnAddChild)
        tvLinkedChildren = findViewById(R.id.tvLinkedChildren)

        // Load students and user profile
        loadStudents()
        fetchUserProfile()

        // Add child button listener
        btnAddChild.setOnClickListener {
            val selectedStudent = spinnerStudents.selectedItemPosition.takeIf { it >= 0 }
                ?.let { studentList[it] }
            selectedStudent?.let { addChild(it.id) }
                ?: Toast.makeText(this, "Please select a valid student.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserProfile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val profile = response.body()?.profile
                    withContext(Dispatchers.Main) {
                        profile?.linkedChildren?.let {
                            linkedChildren.clear()
                            linkedChildren.addAll(it)
                            fetchLinkedChildrenDetails()
                        }
                    }
                } else {
                    showError("Error fetching profile: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error: ${e.message}")
            }
        }
    }

    private fun fetchLinkedChildrenDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val childDetails = linkedChildren.mapNotNull { childId ->
                    fetchChildProfile(api, childId)
                }

                withContext(Dispatchers.Main) {
                    updateChildrenUI(childDetails)
                }
            } catch (e: Exception) {
                showError("Error fetching child details: ${e.message}")
            }
        }
    }

    private suspend fun fetchChildProfile(api: ApiService, childId: String): User? {
        return try {
            val response = api.getChildProfile("Bearer ${getToken()}", childId)
            if (response.isSuccessful) {
                response.body()?.let { childResponse ->
                    User(
                        id = childResponse._id,
                        username = "",
                        email = "",
                        role = "Student",
                        profile = Profile(
                            name = childResponse.profile?.name.orEmpty(),
                            surname = childResponse.profile?.surname.orEmpty(),
                            grade = "",
                            classId = "",
                            busRouteId = "",
                            subject = "",
                            linkedChildren = mutableListOf<String>()
                        )
                    )
                }
            } else {
                showError("Error fetching child profile for ID: $childId")
                null
            }
        } catch (e: Exception) {
            showError("Error fetching child profile: ${e.message}")
            null
        }
    }

    private fun loadStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAllUsers("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val users = response.body()
                    withContext(Dispatchers.Main) {
                        studentList.clear()
                        users?.filter { it.role == "Student" }?.let { studentList.addAll(it) }
                        updateStudentSpinner()
                    }
                } else {
                    showError("Error loading students: ${response.message()}")
                }
            } catch (e: Exception) {
                showError("Error loading students: ${e.message}")
            }
        }
    }

    private fun updateStudentSpinner() {
        val studentNames = studentList.map { "${it.profile.name} ${it.profile.surname}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, studentNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStudents.adapter = adapter
    }

    private fun addChild(childId: String) {

        // Ensure the childId is not already linked
        if (linkedChildren.contains(childId)) {
            // showError("This child is already linked.")
            return
        }

        // Fetch the current profile first
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val profileResponse = api.getProfile("Bearer ${getToken()}")
                if (profileResponse.isSuccessful) {
                    val currentProfile = profileResponse.body()?.profile

                    // Ensure the linkedChildren list is mutable
                    val updatedLinkedChildren = currentProfile?.linkedChildren?.toMutableList() ?: mutableListOf()

                    // Check if the child is already linked
                    if (updatedLinkedChildren.contains(childId)) {
                        withContext(Dispatchers.Main) {
                            showError("This child is already linked.")
                        }
                        return@launch
                    }

                    // Add the new child ID to the list
                    updatedLinkedChildren.add(childId)

                    // Prepare the updated profile
                    val updatedProfile = mapOf(
                        "name" to (currentProfile?.name ?: ""),
                        "surname" to (currentProfile?.surname ?: ""),
                        "linkedChildren" to updatedLinkedChildren.joinToString(",") // Serialize list to string
                    )

                    val request = UserProfileRequest(profile = updatedProfile)

                    // Update the profile with the new child
                    val updateResponse = api.updateProfile("Bearer ${getToken()}", request)
                    handleAddChildResponse(updateResponse)
                } else {
                    withContext(Dispatchers.Main) {
                        showError("Failed to fetch current profile: ${profileResponse.message()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showError("Error adding child: ${e.message}")
                }
            }
        }
    }




    private suspend fun handleAddChildResponse(response: Response<UserProfileResponse>) {
        withContext(Dispatchers.Main) {
            if (response.isSuccessful) {
                fetchLinkedChildrenDetails() // Refresh the linked children details
                Toast.makeText(this@AddChildrenActivity, "Child added successfully!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@AddChildrenActivity, DashboardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                showError("Failed to add child: ${response.message()}")
            }
        }
    }

    private fun updateChildrenUI(childDetails: List<User>) {
        tvLinkedChildren.text = if (childDetails.isEmpty()) {
            "No children linked yet."
        } else {
            childDetails.joinToString(separator = "\n") { child ->
                "${child.profile.name} ${child.profile.surname}"
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@AddChildrenActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

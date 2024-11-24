package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.SelectRoleRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.SelectRoleResponse

class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var parentCheckbox: CheckBox
    private lateinit var teacherCheckbox: CheckBox
    private lateinit var studentCheckbox: CheckBox
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_selection)

        parentCheckbox = findViewById(R.id.checkboxParent)
        teacherCheckbox = findViewById(R.id.checkboxTeacher)
        studentCheckbox = findViewById(R.id.checkboxStudent)
        continueButton = findViewById(R.id.button)

        continueButton.setOnClickListener {
            val selectedRole = when {
                parentCheckbox.isChecked -> "Parent"
                teacherCheckbox.isChecked -> "Teacher"
                studentCheckbox.isChecked -> "Student"
                else -> {
                    Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            updateUserRole(selectedRole)
        }
    }

    private fun getToken(): String? {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", null)
    }

    private fun updateUserRole(role: String) {
        val token = getToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Authentication token not found", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.selectRole("Bearer $token", SelectRoleRequest(role))

                if (response.isSuccessful) {
                    val body = response.body()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RoleSelectionActivity,
                            body?.message ?: "Role updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        navigateToNextPage(role)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@RoleSelectionActivity,
                            "Error: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.e("RoleSelectionActivity", "Error: ${response.errorBody()?.string()}")
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RoleSelectionActivity,
                        "HTTP error: ${e.message()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@RoleSelectionActivity,
                        "An error occurred: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                Log.e("RoleSelectionActivity", "Exception: ${e.message}")
            }
        }
    }

    private fun navigateToNextPage(role: String) {
        val intent = Intent(this, CreateProfileActivity::class.java)
        intent.putExtra("role", role)
        startActivity(intent)
        finish()
    }
}

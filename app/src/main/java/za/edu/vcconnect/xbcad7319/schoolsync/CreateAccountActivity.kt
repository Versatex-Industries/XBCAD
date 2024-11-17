package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import okhttp3.*
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var backbtn: ImageButton
    private lateinit var nextButton: Button
    private lateinit var fullNameInput: TextInputEditText
    private lateinit var emailInput: TextInputEditText
    private lateinit var mobileInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    private var selectedRole: String? = null

    private val apiService = ApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.createaccount)

        // Initialize views
        backbtn = findViewById(R.id.BackButton)
        nextButton = findViewById(R.id.nextButton)
        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        mobileInput = findViewById(R.id.mobileInput)
        passwordInput = findViewById(R.id.passwordInput)

        // Get the selected role from the previous activity
        selectedRole = intent.getStringExtra("selectedRole")

        if (selectedRole.isNullOrEmpty()) {
            Toast.makeText(this, "No role selected. Please go back.", Toast.LENGTH_SHORT).show()
        }

        // Set up listeners
        backbtn.setOnClickListener { finish() }

        nextButton.setOnClickListener {
            validateInputs()
        }
    }

    private fun validateInputs() {
        val fullName = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val mobile = mobileInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        if (fullName.isEmpty() || email.isEmpty() || mobile.isEmpty() || password.isEmpty() || selectedRole.isNullOrEmpty()) {
            // Show error message
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Proceed to create account via API
        createAccount(fullName, selectedRole!!, email, mobile, password)
    }

    private fun createAccount(name: String, role: String, email: String, mobile: String, password: String) {
        apiService.register(name, role, email, password, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@CreateAccountActivity, "Failed to create account", Toast.LENGTH_SHORT).show()
                }
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@CreateAccountActivity, "Account created successfully", Toast.LENGTH_SHORT).show()

                        // Navigate to the LoginActivity
                        val intent = Intent(this@CreateAccountActivity, EnterCodeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()

                    } else {
                        val errorMessage = response.body?.string() ?: "Unknown error"
                        Toast.makeText(this@CreateAccountActivity, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

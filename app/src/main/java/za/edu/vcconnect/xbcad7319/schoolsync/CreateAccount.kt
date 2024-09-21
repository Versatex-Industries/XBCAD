package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class CreateAccount : AppCompatActivity() {


    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nextButton: Button
    private lateinit var backArrow: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.createaccount)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        fullNameInput = findViewById(R.id.fullNameInput)
        emailInput = findViewById(R.id.emailInput)
        mobileInput = findViewById(R.id.mobileInput)
        passwordInput = findViewById(R.id.passwordInput)
        nextButton = findViewById(R.id.nextButton)
        backArrow = findViewById(R.id.backArrow)
        apiService = ApiService()

        backArrow.setOnClickListener {
            finish() // Go back to the previous screen
        }

        nextButton.setOnClickListener {
            if (validateInputs()) {
                registerUser()
            }
        }
    }

    private fun validateInputs(): Boolean {
        val fullName = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val mobile = mobileInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        return when {
            fullName.isEmpty() -> {
                fullNameInput.error = "Full Name is required"
                false
            }

            email.isEmpty() -> {
                emailInput.error = "Email is required"
                false
            }

            mobile.isEmpty() -> {
                mobileInput.error = "Mobile Number is required"
                false
            }

            password.isEmpty() -> {
                passwordInput.error = "Password is required"
                false
            }

            else -> true
        }
    }

    private fun registerUser() {
        val fullName = fullNameInput.text.toString().trim()
        val email = emailInput.text.toString().trim()
        val password = passwordInput.text.toString().trim()

        // Call API to register user
        apiService.register(fullName, "rolePlaceholder", email, password, object : Callback {
            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CreateAccount,
                            "Registration successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        // Navigate to main activity or login page
                        startActivity(Intent(this@CreateAccount, MainActivity::class.java))
                    } else {
                        Toast.makeText(
                            this@CreateAccount,
                            "Registration failed: ${response.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(
                        this@CreateAccount,
                        "Failed to connect to server: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }
}


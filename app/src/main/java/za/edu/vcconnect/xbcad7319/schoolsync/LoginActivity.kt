package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import za.edu.vcconnect.xbcad7319.schoolsync.api.ApiService
import java.io.IOException

class LoginActivity : AppCompatActivity() {

    private lateinit var mobileEmailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var signUpButton: Button
    private lateinit var backArrow: ImageButton
    private lateinit var rememberMeCheckBox: CheckBox

    private val apiService = ApiService() // API service instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login) // Ensure this points to your login layout

        // Initialize the UI elements
        mobileEmailInput = findViewById(R.id.mobileEmailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        signUpButton = findViewById(R.id.signUpButton)
        backArrow = findViewById(R.id.BackButton)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox)

        // Handle login button click
        loginButton.setOnClickListener {
            val email = mobileEmailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Perform login using the API
                login(email, password)
            }
        }

        // Handle sign-up button click
        signUpButton.setOnClickListener {
            val intent = Intent(this, SelectRoleActivity::class.java)
            startActivity(intent)
        }

        // Handle back button click
        backArrow.setOnClickListener {
            finish() // Go back to the previous activity
        }
    }

    private fun login(email: String, password: String) {
        apiService.login(email, password, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@LoginActivity, "Failed to log in", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                runOnUiThread {
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        val jsonResponse = JSONObject(responseBody)
                        val token = jsonResponse.getString("token")

                        // Save the token in shared preferences
                        val sharedPref = getSharedPreferences("appPrefs", MODE_PRIVATE)
                        sharedPref.edit().putString("jwtToken", token).apply()

                        val editor = sharedPref.edit()
                        editor.putString("userEmail", email) // Store the user's email during login
                        editor.apply()

                        Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()

                        // Navigate to the dashboard or next screen
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }


}

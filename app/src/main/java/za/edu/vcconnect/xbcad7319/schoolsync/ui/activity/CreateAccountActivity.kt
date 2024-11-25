package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)

        // Set up Back Button
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Close the current activity
        }

        // Set up Next Button
        findViewById<Button>(R.id.nextButton).setOnClickListener {
            val username = findViewById<TextInputEditText>(R.id.usernameInput).text.toString()
            val email = findViewById<TextInputEditText>(R.id.emailInput).text.toString()
            val password = findViewById<TextInputEditText>(R.id.passwordInput).text.toString()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            registerUser(username, email, password)
        }
    }

    private fun registerUser(username: String, email: String, password: String) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        val requestBody = mapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )

        api.registerUser(requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateAccountActivity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    loginUser(email, password) // Automatically log the user in
                } else {
                    Toast.makeText(this@CreateAccountActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@CreateAccountActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginUser(email: String, password: String) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        val requestBody = mapOf(
            "email" to email,
            "password" to password
        )

        api.loginUser(requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    val token = response.body()?.get("token") as? String
                    if (token != null) {
                        saveToken(token)
                        navigateToRoleSelection()
                    } else {
                        Toast.makeText(this@CreateAccountActivity, "Login failed: Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CreateAccountActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@CreateAccountActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("auth_token", token).apply()
        Toast.makeText(this, "Token saved", Toast.LENGTH_SHORT).show()
    }

    private fun navigateToRoleSelection() {
        val intent = Intent(this, RoleSelectionActivity::class.java)
        startActivity(intent)
        finish()
    }
}

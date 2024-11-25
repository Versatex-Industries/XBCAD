package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class LoginActivity : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var rememberMeCheckBox: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_login)

        // Initialize views
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox)

        val loginButton = findViewById<Button>(R.id.loginButton)
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        // Back button logic
        backButton.setOnClickListener {
            finish()
        }

        // Log in button logic
        loginButton.setOnClickListener {
            val email = emailInput.text.toString()
            val password = passwordInput.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        // Sign up button logic
        signUpButton.setOnClickListener {
            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        val api = RetrofitClient.instance.create(ApiService::class.java)
        val requestBody = mapOf("email" to email, "password" to password)

        api.loginUser(requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    val token = response.body()?.get("token") as? String
                    val role = response.body()?.get("role") as? String
                    if (token != null) {
                        saveToken(token)
                        Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                        navigateToNextPage(role)
                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
    }




    private fun navigateToNextPage(role: String?) {

        if(role.isNullOrEmpty()){
            val intent = Intent(this, RoleSelectionActivity::class.java)
            startActivity(intent)
        }else{
            val intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("role", role)
            startActivity(intent)

            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putString("role", role)
            editor.apply()

        }
        finish()
    }
}

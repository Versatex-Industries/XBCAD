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

class CreateAccount : AppCompatActivity() {


    private lateinit var fullNameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var mobileInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var nextButton: Button
    private lateinit var backArrow: Button

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

        // Set listener for Back Arrow
        backArrow.setOnClickListener {
            finish() // Go back to the previous screen
        }

        // Set listener for Next Button
        nextButton.setOnClickListener {
            if (validateInputs()) {
                // Proceed to the next activity, passing user data if necessary
                Toast.makeText(this, "Proceeding to the next step...", Toast.LENGTH_SHORT).show()

                // Example intent for moving to another screen
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    // Function to validate input fields
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
}



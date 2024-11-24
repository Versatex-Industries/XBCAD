package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.edu.vcconnect.xbcad7319.schoolsync.R

class EnterCodeActivity : AppCompatActivity() {
/*
    private lateinit var backbtn: ImageButton
    private lateinit var nextButton: Button
    private lateinit var lostAccessButton: Button
    private lateinit var codeInput1: EditText
    private lateinit var codeInput2: EditText
    private lateinit var codeInput3: EditText
    private lateinit var codeInput4: EditText
    private lateinit var codeInput5: EditText
    private lateinit var codeInput6: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation_code)

        // Initialize views
        backbtn = findViewById(R.id.BackButton)
        nextButton = findViewById(R.id.nextButton)
        lostAccessButton = findViewById(R.id.lostAccessButton)
        codeInput1 = findViewById(R.id.codeInput1)
        codeInput2 = findViewById(R.id.codeInput2)
        codeInput3 = findViewById(R.id.codeInput3)
        codeInput4 = findViewById(R.id.codeInput4)
        codeInput5 = findViewById(R.id.codeInput5)
        codeInput6 = findViewById(R.id.codeInput6)

        // Back button action
        backbtn.setOnClickListener { finish() }

        // Next button action
        nextButton.setOnClickListener {
            val verificationCode = getVerificationCode()
            if (verificationCode.length == 6) {
                verifyCode(verificationCode)
            } else {
                Toast.makeText(this, "Please enter a 6-digit code", Toast.LENGTH_SHORT).show()
            }
        }

        // Lost access to email button action
        lostAccessButton.setOnClickListener {
            Toast.makeText(this, "Redirecting to recovery options", Toast.LENGTH_SHORT).show()
            // Implement logic to handle lost access to email, possibly opening another activity
        }
    }

    // Function to concatenate the code entered in the 6 input fields
    private fun getVerificationCode(): String {
        return codeInput1.text.toString() +
                codeInput2.text.toString() +
                codeInput3.text.toString() +
                codeInput4.text.toString() +
                codeInput5.text.toString() +
                codeInput6.text.toString()
    }

    // Function to verify the code (you can replace this with the actual API call)
    private fun verifyCode(code: String) {
        // TODO: Call your API to verify the code
        Toast.makeText(this, "Verifying code: $code", Toast.LENGTH_SHORT).show()

        // Dummy check (replace with actual verification)
        if (code == "123456") {
            Toast.makeText(this, "Code verified! Navigating to the login page", Toast.LENGTH_SHORT).show()
            // Navigate to the next activity after verification
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Invalid code. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }*/
}

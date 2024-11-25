package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import za.edu.vcconnect.xbcad7319.schoolsync.R

class SettingsActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var editProfileButton: Button
    private lateinit var messagesButton: Button
    private lateinit var changeLanguageButton: Button
    private lateinit var helpSupportButton: Button
    private lateinit var logoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize UI components
        backButton = findViewById(R.id.bckbutton)
        editProfileButton = findViewById(R.id.editProfileButton)
        messagesButton = findViewById(R.id.messagesButton)
        changeLanguageButton = findViewById(R.id.changeLanguageButton)
        helpSupportButton = findViewById(R.id.btn_help_support)
        logoutButton = findViewById(R.id.logoutButton)

        // Set click listeners
        backButton.setOnClickListener {
            finish() // Close settings and go back to the previous screen
        }

        editProfileButton.setOnClickListener {
            val intent = Intent(this@SettingsActivity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        messagesButton.setOnClickListener {
            val intent = Intent(this@SettingsActivity, MessagesActivity::class.java)
            startActivity(intent)
        }

        changeLanguageButton.setOnClickListener {
            val intent = Intent(this, ChangeLanguageActivity::class.java)
            startActivity(intent)
        }

        helpSupportButton.setOnClickListener {
            val intent = Intent(this, HelpAndSupportActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            // Clear user session and redirect to login
            val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
            sharedPreferences.edit().clear().apply() // Clear stored preferences

            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear activity stack
            startActivity(intent)
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
        }
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        val backButton = findViewById<ImageButton>(R.id.bckbutton)
        val editProfileButton = findViewById<Button>(R.id.editProfileButton)
        val messagesButton = findViewById<Button>(R.id.messagesButton)
        val changeLanguageButton = findViewById<Button>(R.id.changeLanguageButton)
        val logoutButton = findViewById<Button>(R.id.logoutButton)
        val Helpbutton = findViewById<Button>(R.id.btn_help_support)




        // Handle Edit Profile button click
        editProfileButton.setOnClickListener {
            // Navigate to Edit Profile screen
            val intent = Intent(this, UserProfile::class.java)
            startActivity(intent)
        }

        backButton.setOnClickListener {
            backButton.setOnClickListener {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            }
        }

        // Handle Messages button click
        messagesButton.setOnClickListener {
            // Navigate to Messages screen
            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        // Handle Change Language button click
        changeLanguageButton.setOnClickListener {
            // Show a Toast or open a language selection dialog
            Toast.makeText(this, "Change Language clicked", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        // Handle Logout button click
        logoutButton.setOnClickListener {
            // Perform logout logic
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
            // Redirect to Login screen or exit app
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        Helpbutton.setOnClickListener {
            // Show a Toast or open a language selection dialog
            Toast.makeText(this, "Help Button", Toast.LENGTH_SHORT).show()

            val intent = Intent(this, HelpandSupport::class.java)
            startActivity(intent)
        }
    }
}


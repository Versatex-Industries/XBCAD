package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import za.edu.vcconnect.xbcad7319.schoolsync.R

class HelpAndSupportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_and_support)

        val btnCallSupport: Button = findViewById(R.id.btnCallSupport)
        val btnEmailSupport: Button = findViewById(R.id.btnEmailSupport)

        // Call Support Button
        btnCallSupport.setOnClickListener {
            val phoneNumber = "+1234567890"
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }

        // Email Support Button
        btnEmailSupport.setOnClickListener {
            val emailAddress = "support@schoolsync.com" // Replace with your support email address
            val subject = "Support Request"
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:$emailAddress")
                putExtra(Intent.EXTRA_SUBJECT, subject)
            }
            startActivity(Intent.createChooser(intent, "Send Email"))
        }
    }
}

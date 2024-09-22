package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Redirect to LoginActivity after a delay of 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Optionally call finish() if you don't want the user to go back to MainActivity.
        }, 2000) // 2000 milliseconds = 2 seconds
    }
}


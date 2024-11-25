package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import za.edu.vcconnect.xbcad7319.schoolsync.ui.activity.LoginActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        applySavedLanguage()
        setContentView(R.layout.activity_main)

        // Redirect to LoginActivity after a delay of 2 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Optionally call finish() if you don't want the user to go back to MainActivity.
        }, 2000) // 2000 milliseconds = 2 seconds
    }

    private fun applySavedLanguage() {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language_code", "en") ?: "en"

        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}


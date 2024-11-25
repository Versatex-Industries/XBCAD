package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import java.util.*

class ChangeLanguageActivity : AppCompatActivity() {

    private lateinit var languageRadioGroup: RadioGroup
    private lateinit var saveLanguageButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_change_language)

        // Initialize UI components
        languageRadioGroup = findViewById(R.id.languageRadioGroup)
        saveLanguageButton = findViewById(R.id.saveLanguageButton)

        // Set up save button click listener
        saveLanguageButton.setOnClickListener {
            val selectedLanguage = when (languageRadioGroup.checkedRadioButtonId) {
                R.id.radioEnglish -> "en"   // English locale
                R.id.radioAfrikaans -> "af" // Afrikaans locale
                R.id.radioZulu -> "zu"      // Zulu locale
                else -> null
            }

            if (selectedLanguage != null) {
                updateLanguagePreference(selectedLanguage)
                applyLanguage(selectedLanguage)
            } else {
                Toast.makeText(this, "Please select a language", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateLanguagePreference(language: String) {
        // Save the language preference locally
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("selected_language", language).apply()

        // Optional: Update language preference in backend
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.updateLanguage(
                    "Bearer ${getToken()}",
                    mapOf("language" to language)
                )
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@ChangeLanguageActivity,
                            "Language updated to ${getLanguageName(language)}",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@ChangeLanguageActivity,
                            "Failed to update language: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ChangeLanguageActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun applyLanguage(language: String) {
        setAppLocale(language)
        val intent = Intent(this, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private fun getLanguageName(languageCode: String): String {
        return when (languageCode) {
            "en" -> "English"
            "af" -> "Afrikaans"
            "zu" -> "Zulu"
            else -> "Unknown"
        }
    }

    private fun setAppLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }
}

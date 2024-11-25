package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class SendNotificationActivity : AppCompatActivity() {

    private lateinit var typeInput: EditText
    private lateinit var contentInput: EditText
    private lateinit var sendNotificationButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_send_notification)

        typeInput = findViewById(R.id.typeInput)
        contentInput = findViewById(R.id.contentInput)
        sendNotificationButton = findViewById(R.id.sendNotificationButton)

        sendNotificationButton.setOnClickListener {
            val type = typeInput.text.toString()
            val content = contentInput.text.toString()

            if (type.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                sendGlobalNotification(type, content)
            }
        }
    }

    private fun sendGlobalNotification(type: String, content: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.sendGlobalNotification(
                    "Bearer ${getToken()}",
                    mapOf("type" to type, "content" to content)
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@SendNotificationActivity,
                            "Global notification sent successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            this@SendNotificationActivity,
                            "Failed to send notification: ${response.message()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SendNotificationActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}

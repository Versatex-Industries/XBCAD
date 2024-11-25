package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Notification
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.NotificationAdapter

class ViewNotificationsActivity : AppCompatActivity() {

    private lateinit var unreadRecyclerView: RecyclerView
    private lateinit var readRecyclerView: RecyclerView

    private val unreadNotifications = mutableListOf<Notification>()
    private val readNotifications = mutableListOf<Notification>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_view_notifications)

        unreadRecyclerView = findViewById(R.id.unreadRecyclerView)
        readRecyclerView = findViewById(R.id.readRecyclerView)

        unreadRecyclerView.layoutManager = LinearLayoutManager(this)
        readRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchNotifications()
    }

    private fun fetchNotifications() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAllNotifications("Bearer ${getToken()}")

                if (response.isSuccessful) {
                    val notifications = response.body() ?: emptyList()
                    unreadNotifications.clear()
                    readNotifications.clear()

                    notifications.forEach { notification ->
                        if (notification.read) {
                            readNotifications.add(notification)
                        } else {
                            unreadNotifications.add(notification)
                        }
                    }

                    markNotificationsAsRead(unreadNotifications.map { it._id })

                    withContext(Dispatchers.Main) {
                        unreadRecyclerView.adapter = NotificationAdapter(unreadNotifications)
                        readRecyclerView.adapter = NotificationAdapter(readNotifications)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@ViewNotificationsActivity,
                            "Failed to fetch notifications",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@ViewNotificationsActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun markNotificationsAsRead(notificationIds: List<String>) {
        CoroutineScope(Dispatchers.IO).launch {
            notificationIds.forEach { notificationId ->
                try {
                    val api = RetrofitClient.instance.create(ApiService::class.java)
                    api.markNotificationAsRead(
                        "Bearer ${getToken()}",
                        notificationId,
                        mapOf("read" to true)
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}

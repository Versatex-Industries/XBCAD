package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.LinkedChild

class ChildSelectorActivity : AppCompatActivity() {

    private lateinit var childListView: ListView
    private val children = mutableListOf<Pair<String, String>>() // Pair<ChildId, ChildName>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_selector)

        childListView = findViewById(R.id.childListView)

        loadChildren()

        // Handle list item clicks
        childListView.setOnItemClickListener { _, _, position, _ ->
            val selectedChild = children[position]
            val intent = Intent(this, ViewAttendanceActivity::class.java)
            intent.putExtra("childId", selectedChild.first)
            startActivity(intent)
        }
    }

    private fun loadChildren() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getProfile("Bearer ${getToken()}")
                if (response.isSuccessful) {
                    val linkedChildrenIds = response.body()?.profile?.linkedChildren ?: emptyList()
                    val childrenWithProfiles = mutableListOf<Pair<String, String>>()

                    // Fetch child profiles for each ID
                    linkedChildrenIds.forEach { childId ->
                        val childResponse = api.getChildProfile("Bearer ${getToken()}", childId)
                        if (childResponse.isSuccessful) {
                            val childProfile = childResponse.body()?.profile
                            val name = "${childProfile?.name ?: "Unknown"} ${childProfile?.surname ?: "Unknown"}"
                            childrenWithProfiles.add(Pair(childId, name))
                        }
                    }

                    withContext(Dispatchers.Main) {
                        children.clear()
                        children.addAll(childrenWithProfiles)
                        val adapter = ArrayAdapter(
                            this@ChildSelectorActivity,
                            android.R.layout.simple_list_item_1,
                            children.map { it.second }
                        )
                        childListView.adapter = adapter
                    }
                } else {
                    showError("Failed to load children: ${response.message()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Error loading children: ${e.message}")
            }
        }
    }


    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }

    private suspend fun showError(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(this@ChildSelectorActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}

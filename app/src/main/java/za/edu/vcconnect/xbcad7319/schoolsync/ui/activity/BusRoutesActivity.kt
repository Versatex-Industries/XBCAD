package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
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
import za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter.BusRoutesAdapter

class BusRoutesActivity : AppCompatActivity() {

    private lateinit var busRoutesRecyclerView: RecyclerView
    private lateinit var adapter: BusRoutesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_bus_routes)

        busRoutesRecyclerView = findViewById(R.id.busRoutesRecyclerView)
        busRoutesRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BusRoutesAdapter(emptyList()) { busRoute ->
            val intent = Intent(this, BusRouteDetailsActivity::class.java)
            intent.putExtra("routeId", busRoute._id)
            intent.putExtra("routeName", busRoute.routeName)
            startActivity(intent)
        }
        busRoutesRecyclerView.adapter = adapter

        fetchBusRoutes()
    }

    private fun fetchBusRoutes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getAllRoutes("Bearer ${getToken()}")

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val routes = response.body() ?: emptyList()
                        adapter.updateData(routes)
                    } else {
                        Toast.makeText(this@BusRoutesActivity, "Failed to load routes", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@BusRoutesActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}

package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.BusRouteDetails
import java.net.URL

class BusRouteDetailsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var routeId: String? = null
    private var liveLocationMarker: Marker? = null
    private val handler = Handler(Looper.getMainLooper())
    private val apiKey: String = "AIzaSyBxZdirk-nVzH8doilV1kjOxnEYNRC4NQg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bus_route_details)

        routeId = intent.getStringExtra("routeId")

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onPause() {
        super.onPause()
        stopLiveLocationUpdates()
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        checkPermissions()
        fetchRouteDetails()
        startLiveLocationUpdates()
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
            return
        }
        googleMap.isMyLocationEnabled = true
    }

    private fun fetchRouteDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getRouteDetails("Bearer ${getToken()}", routeId ?: "")

                if (response.isSuccessful) {
                    response.body()?.let { route ->
                        drawRoute(route)
                    }
                }
            } catch (e: Exception) {
                Log.e("RouteError", "Error fetching route: ${e.message}")
            }
        }
    }

    private fun drawRoute(route: BusRouteDetails) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val polylineOptions = PolylineOptions().color(resources.getColor(R.color.black)).width(8f)

                for (i in 0 until route.stops.size - 1) {
                    val origin = route.stops[i].location.coordinates
                    val destination = route.stops[i + 1].location.coordinates
                    val originLatLng = "${origin[1]},${origin[0]}"
                    val destinationLatLng = "${destination[1]},${destination[0]}"

                    val directionsUrl =
                        "https://maps.googleapis.com/maps/api/directions/json?origin=$originLatLng&destination=$destinationLatLng&key=$apiKey"
                    val response = URL(directionsUrl).readText()
                    val decodedPolyline = parseDirections(response)
                    polylineOptions.addAll(decodedPolyline)
                }

                withContext(Dispatchers.Main) {
                    googleMap.addPolyline(polylineOptions)

                    // Add markers for stops
                    route.stops.forEach { stop ->
                        val latLng = LatLng(stop.location.coordinates[1], stop.location.coordinates[0])
                        googleMap.addMarker(MarkerOptions().position(latLng).title(stop.stopName))
                    }

                    // Move camera to first stop
                    val firstStop = route.stops.first().location.coordinates
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(firstStop[1], firstStop[0]), 12f))
                }
            } catch (e: Exception) {
                Log.e("DrawRouteError", "Error drawing route: ${e.message}")
            }
        }
    }
    private var liveLocationRunnable: Runnable? = null

    private fun startLiveLocationUpdates() {
        liveLocationRunnable = object : Runnable {
            override fun run() {
                updateLiveLocation()
                handler.postDelayed(this, 5000) // Update every 5 seconds
            }
        }
        liveLocationRunnable?.let { handler.post(it) }
    }

    private fun stopLiveLocationUpdates() {
        liveLocationRunnable?.let { handler.removeCallbacks(it) }
        liveLocationRunnable = null // Clear the reference
    }

    private fun updateLiveLocation() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response = api.getLiveLocation("Bearer ${getToken()}", routeId ?: "")

                if (response.isSuccessful) {
                    val liveLocation = response.body()?.liveLocation
                    Log.d("LiveLocationResponse", "Coordinates: ${liveLocation?.coordinates}")

                    liveLocation?.coordinates?.let { coordinates ->
                        if (coordinates.size == 2) {
                            val latLng = LatLng(coordinates[1], coordinates[0])
                            runOnUiThread {
                                liveLocationMarker?.remove()
                                liveLocationMarker = googleMap.addMarker(
                                    MarkerOptions()
                                        .position(latLng)
                                        .title("Live Bus Location")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                )
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            }
                        } else {
                            Log.e("LiveLocationError", "Invalid coordinates array size: $coordinates")
                        }
                    } ?: Log.e("LiveLocationError", "LiveLocation is null")
                } else {
                    Log.e("LiveLocationError", "API response unsuccessful: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("LiveLocationException", "Error: ${e.message}")
            }
        }
    }


    private fun parseDirections(response: String): List<LatLng> {
        val jsonObject = JSONObject(response)
        val polylinePoints = mutableListOf<LatLng>()
        val routes = jsonObject.getJSONArray("routes")
        if (routes.length() > 0) {
            val legs = routes.getJSONObject(0).getJSONArray("legs")
            val steps = legs.getJSONObject(0).getJSONArray("steps")
            for (i in 0 until steps.length()) {
                val polyline = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
                polylinePoints.addAll(decodePolyline(polyline))
            }
        }
        return polylinePoints
    }

    private fun decodePolyline(encoded: String): List<LatLng> {
        val polyline = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            polyline.add(LatLng(lat.toDouble() / 1E5, lng.toDouble() / 1E5))
        }
        return polyline
    }

    private fun getToken(): String {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }
}

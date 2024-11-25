package za.edu.vcconnect.xbcad7319.schoolsync.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import za.edu.vcconnect.xbcad7319.schoolsync.R

class LocationPickerActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_location_picker)

        val confirmButton: Button = findViewById(R.id.confirmButton)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        confirmButton.setOnClickListener {
            if (selectedLocation != null) {
                val intent = Intent()
                intent.putExtra("latitude", selectedLocation!!.latitude)
                intent.putExtra("longitude", selectedLocation!!.longitude)
                setResult(RESULT_OK, intent)
                finish()
            } else {
                // Show an error message to the user
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set initial location (e.g., Pretoria, South Africa)
        val initialLocation = LatLng(-25.7479, 28.2293)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10f))

        // Add a marker when the user taps on the map
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
            selectedLocation = latLng
        }
    }
}

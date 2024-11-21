package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewClasses : AppCompatActivity() {

    private lateinit var classesRecyclerView: RecyclerView
    private lateinit var backButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_classes) // Use your layout file name

        // Initialize views
        classesRecyclerView = findViewById(R.id.classesRecyclerView)
        backButton = findViewById(R.id.imageButton)

        // Set up RecyclerView
        val classesList = getDummyClasses() // Replace with real data fetching logic
        val adapter = ClassesAdapter(classesList)
        classesRecyclerView.layoutManager = LinearLayoutManager(this)
        classesRecyclerView.adapter = adapter

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close this activity and go back to the previous screen
        }
    }

    // Dummy data for the list of classes
    private fun getDummyClasses(): List<String> {
        return listOf(
            "Mathematics",
            "Science",
            "History",
            "Geography",
            "English",
            "Physical Education",
            "Art",
            "Music"
        )
    }
}

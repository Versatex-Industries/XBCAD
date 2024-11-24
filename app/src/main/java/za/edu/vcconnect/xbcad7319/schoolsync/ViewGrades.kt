package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewGrades : AppCompatActivity() {
    private lateinit var gradesRecyclerView: RecyclerView
    private lateinit var backButton: ImageButton
    private lateinit var gradesAdapter: GradesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_grades) // Ensure this matches your XML filename

        // Initialize views
        gradesRecyclerView = findViewById(R.id.gradesRecyclerView)
        backButton = findViewById(R.id.backButton)

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close the activity and go back
        }

        // Set up RecyclerView
        gradesRecyclerView.layoutManager = LinearLayoutManager(this)
        gradesAdapter = GradesAdapter(getGradesData())
        gradesRecyclerView.adapter = gradesAdapter
    }

    // Dummy data for grades (replace this with real data fetching logic)
    private fun getGradesData(): List<GradeItem> {
        return listOf(
            GradeItem("John Doe", "10", "Math", "A", "Midterm", 85),
            GradeItem("Jane Smith", "10", "Science", "A", "Midterm", 90),
            GradeItem("Mark Taylor", "10", "History", "B", "Assignment", 75),
            GradeItem("Emily Davis", "10", "English", "A", "Test", 88)
        )
    }
}
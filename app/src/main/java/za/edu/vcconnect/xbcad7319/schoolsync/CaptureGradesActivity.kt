package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class CaptureGradesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture_grades)

        val etStudentName = findViewById<EditText>(R.id.etStudentName)
        val etGrade = findViewById<EditText>(R.id.etGrade)
        val saveGradeButton = findViewById<Button>(R.id.saveGradeButton)

        saveGradeButton.setOnClickListener { v: View? ->
            val studentName = etStudentName.text.toString()
            val grade = etGrade.text.toString()
            if (studentName.isEmpty() || grade.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Save logic (e.g., store in database)
                Toast.makeText(
                    this,
                    "Grade saved for $studentName",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

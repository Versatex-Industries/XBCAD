package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class CreateUserProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_user_profile)

        // Global Sections
        val etName = findViewById<EditText>(R.id.etName)
        val etSurname = findViewById<EditText>(R.id.etSurname)

        // Role-Based Sections
        val studentSection = findViewById<LinearLayout>(R.id.studentSection)
        val parentSection = findViewById<LinearLayout>(R.id.parentSection)
        val teacherSection = findViewById<LinearLayout>(R.id.teacherSection)

        // Next Button
        val nextButton = findViewById<Button>(R.id.nextButton)

        // Retrieve user role from Intent or Database
        val userRole = intent.getStringExtra("USER_ROLE")

        // Show the appropriate section based on the role
        when (userRole) {
            "Student" -> studentSection.visibility = View.VISIBLE
            "Parent" -> parentSection.visibility = View.VISIBLE
            "Teacher" -> teacherSection.visibility = View.VISIBLE
        }

        // Next Button Navigation
        nextButton.setOnClickListener {
            val intent = Intent(this@CreateUserProfileActivity, DashboardActivity::class.java)

            // Pass common data to the next activity
            intent.putExtra("Name", etName.text.toString())
            intent.putExtra("Surname", etSurname.text.toString())

            // Pass role-specific data to the next activity
            when (userRole) {
                "Student" -> {
                    intent.putExtra("Grade", findViewById<EditText>(R.id.etGrade).text.toString())
                    intent.putExtra("Age", findViewById<EditText>(R.id.etAge).text.toString())
                    intent.putExtra("Class", findViewById<EditText>(R.id.etClass).text.toString())
                }
                "Parent" -> {
                    intent.putExtra("ChildName", findViewById<EditText>(R.id.etChildName).text.toString())
                    intent.putExtra("ChildGrade", findViewById<EditText>(R.id.etChildGrade).text.toString())
                }
                "Teacher" -> {
                    intent.putExtra("Subject", findViewById<EditText>(R.id.etSubject).text.toString())
                    intent.putExtra("Class", findViewById<EditText>(R.id.etClassTeacher).text.toString())
                }
            }

            // Start the next activity
            startActivity(intent)
        }
    }
}
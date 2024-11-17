package za.edu.vcconnect.xbcad7319.schoolsync

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class AddChild : AppCompatActivity() {
    private lateinit var childNameInput: EditText
    private lateinit var gradeInput: EditText
    private lateinit var busRouteInput: EditText
    private lateinit var cancelButton: Button
    private lateinit var saveButton: Button
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addchild)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        childNameInput = findViewById(R.id.childNameInput)
        gradeInput = findViewById(R.id.gradeInput)
        busRouteInput = findViewById(R.id.busRouteInput)
        cancelButton = findViewById(R.id.cancelButton)
        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.BackButton)

        backButton.setOnClickListener {
            finish() // Go back to the previous screen
        }

        // Set listener for Cancel Button
        cancelButton.setOnClickListener {
            // Optionally clear the input fields or go back to the previous screen
            finish() // Close the activity
        }

        // Set listener for Save Button
        saveButton.setOnClickListener {
            if (validateInputs()) {
                // Optionally save the child's information locally or prepare for future API integration
                Toast.makeText(this, "Child's information saved locally.", Toast.LENGTH_SHORT).show()

                // You could also redirect to another activity, depending on your app's flow
            }
        }
    }

    // Function to validate input fields
    private fun validateInputs(): Boolean {
        val childName = childNameInput.text.toString().trim()
        val grade = gradeInput.text.toString().trim()
        val busRoute = busRouteInput.text.toString().trim()

        return when {
            childName.isEmpty() -> {
                childNameInput.error = "Child's name is required"
                false
            }
            grade.isEmpty() -> {
                gradeInput.error = "Grade is required"
                false
            }
            busRoute.isEmpty() -> {
                busRouteInput.error = "Bus route is required"
                false
            }
            else -> true
        }
    }
}
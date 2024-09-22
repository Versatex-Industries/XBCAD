package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectRoleActivity : AppCompatActivity() {

    private lateinit var checkboxParent: CheckBox
    private lateinit var checkboxTeacher: CheckBox
    private lateinit var checkboxStudent: CheckBox
    private lateinit var checkboxDonor: CheckBox
    private lateinit var checkboxVolunteer: CheckBox
    private lateinit var checkboxAdministrator: CheckBox
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roleselection)

        // Initialize the checkboxes
        checkboxParent = findViewById(R.id.checkboxParent)
        checkboxTeacher = findViewById(R.id.checkboxTeacher)
        checkboxStudent = findViewById(R.id.checkboxStudent)
        checkboxDonor = findViewById(R.id.checkboxDonor)
        checkboxVolunteer = findViewById(R.id.checkboxVolunteer)
        checkboxAdministrator = findViewById(R.id.checkboxAdministrator)
        continueButton = findViewById(R.id.button)

        // Ensure only one checkbox can be selected at a time
        setupSingleSelection()

        // Set listener for continue button
        continueButton.setOnClickListener {
            // Get selected role
            val selectedRole = getSelectedRole()

            if (selectedRole.isNullOrEmpty()) {
                Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show()
            } else {
                // Pass the selected role to the next activity (registration page)
                val intent = Intent(this, CreateAccountActivity::class.java)
                intent.putExtra("selectedRole", selectedRole)
                startActivity(intent)
            }
        }
    }

    // Ensure only one checkbox is checked at a time
    private fun setupSingleSelection() {
        val checkboxes = listOf(checkboxParent, checkboxTeacher, checkboxStudent, checkboxDonor, checkboxVolunteer, checkboxAdministrator)

        for (checkbox in checkboxes) {
            checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    // Uncheck all other checkboxes
                    checkboxes.filter { it != buttonView }.forEach { it.isChecked = false }
                }
            }
        }
    }

    // Get the selected role from the checkboxes
    private fun getSelectedRole(): String? {
        return when {
            checkboxParent.isChecked -> "Parent"
            checkboxTeacher.isChecked -> "Teacher"
            checkboxStudent.isChecked -> "Student"
            checkboxDonor.isChecked -> "Donor"
            checkboxVolunteer.isChecked -> "Volunteer"
            checkboxAdministrator.isChecked -> "Administrator"
            else -> null
        }
    }
}

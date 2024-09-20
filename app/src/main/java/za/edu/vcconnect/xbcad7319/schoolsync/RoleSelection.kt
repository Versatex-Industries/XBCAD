package za.edu.vcconnect.xbcad7319.schoolsync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RoleSelection : AppCompatActivity() {


    private lateinit var parent: CheckBox
    private lateinit var teacher: CheckBox
    private lateinit var student: CheckBox
    private lateinit var donor: CheckBox
    private lateinit var volunteer: CheckBox
    private lateinit var administrator: CheckBox
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roleselection)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize checkboxes
        parent = findViewById(R.id.checkboxParent)
        teacher = findViewById(R.id.checkboxTeacher)
        student = findViewById(R.id.checkboxStudent)
        donor = findViewById(R.id.checkboxDonor)
        volunteer = findViewById(R.id.checkboxVolunteer)
        administrator = findViewById(R.id.checkboxAdministrator)

        // Initialize continue button
        continueButton = findViewById(R.id.continueButton)

        // Handle button click
        continueButton.setOnClickListener {
            val selectedRoles = buildSelectedRoles()
            Toast.makeText(this, selectedRoles, Toast.LENGTH_SHORT).show()

            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
    }

    private fun buildSelectedRoles(): String {
        return StringBuilder("Selected Roles: ").apply {
            if (parent.isChecked) append("Parent ")
            if (teacher.isChecked) append("Teacher ")
            if (student.isChecked) append("Student ")
            if (donor.isChecked) append("Donor ")
            if (volunteer.isChecked) append("Volunteer ")
            if (administrator.isChecked) append("Administrator ")
        }.toString()
    }
}

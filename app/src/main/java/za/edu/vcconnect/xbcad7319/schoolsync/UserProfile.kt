package za.edu.vcconnect.xbcad7319.schoolsync


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class UserProfile : AppCompatActivity() {

    private lateinit var profileImage: ImageView
    private lateinit var changeProfilePictureButton: Button
    private lateinit var nameInput: EditText
    private lateinit var emailInput: EditText
    private lateinit var phoneInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var updateButton: Button

    private val pickimagerequest = 1 // Request code for selecting an image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_profile)

        // Initialize views
        profileImage = findViewById(R.id.profileImage)
        changeProfilePictureButton = findViewById(R.id.changeProfilePictureButton)
        nameInput = findViewById(R.id.nameInput)
        emailInput = findViewById(R.id.emailInput)
        phoneInput = findViewById(R.id.phoneInput)
        passwordInput = findViewById(R.id.passwordInput)
        updateButton = findViewById(R.id.updateButton)

        // Handle profile picture change
        changeProfilePictureButton.setOnClickListener {
            openImagePicker()
        }

        // Handle profile update
        updateButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (validateInputs(name, email, phone, password)) {
                updateProfile(name, email, phone, password)
            }
        }
    }

    // Open image picker for selecting a new profile picture
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, pickimagerequest)
    }

    // Handle the result of the image picker
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickimagerequest && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            profileImage.setImageURI(imageUri) // Set the selected image to the ImageView
            Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show()
        }
    }

    // Validate user inputs
    private fun validateInputs(name: String, email: String, phone: String, password: String): Boolean {
        return when {
            name.isEmpty() -> {
                showToast("Name is required")
                false
            }
            email.isEmpty() -> {
                showToast("Email is required")
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showToast("Invalid email address")
                false
            }
            phone.isEmpty() -> {
                showToast("Phone number is required")
                false
            }
            phone.length < 10 -> {
                showToast("Phone number must be at least 10 digits")
                false
            }
            password.isEmpty() -> {
                showToast("Password is required")
                false
            }
            password.length < 6 -> {
                showToast("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }

    // Update the user's profile (mock implementation)
    private fun updateProfile(name: String, email: String, phone: String, password: String) {
        // Add your backend API or database logic here
        // For this example, we just show a success toast
        showToast("Profile updated successfully!")
    }

    // Show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

        }
    }

package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class StudentResponse(
    val _id: String, // Unique ID for the student
    val username: String, // Username of the student
    val email: String, // Email of the student
    val profile: Profile // Nested profile data
)
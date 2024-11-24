package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class GradeResponse(
    val _id: String,          // Unique grade ID
    val subject: String,      // Subject name
    val grade: String,        // Grade/Mark
    val remarks: String?,     // Additional remarks
    val createdAt: String     // Timestamp when grade was added
)

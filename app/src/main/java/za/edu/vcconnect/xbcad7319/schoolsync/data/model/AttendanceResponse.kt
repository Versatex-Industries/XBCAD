package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class AttendanceResponse(
    val _id: String,          // Unique attendance ID
    val date: String,         // Date of the attendance record
    val status: String,       // Status (e.g., "Present", "Absent")
    val remarks: String?,     // Additional remarks
    val classId: String       // ID of the class
)

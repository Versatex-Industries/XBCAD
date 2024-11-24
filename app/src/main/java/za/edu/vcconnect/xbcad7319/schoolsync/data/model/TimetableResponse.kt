package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class TimetableResponse(
    val _id: String,        // Unique ID of the timetable entry
    val classId: String,    // ID of the associated class
    val day: String,        // Day of the week (e.g., "Monday", "Tuesday")
    val period: String,     // Period (e.g., "08:00 - 09:00")
    val subject: String,    // Subject being taught
    val teacherName: String // Teacher's name
)

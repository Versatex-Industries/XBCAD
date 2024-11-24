package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class Event(
    val _id: String,
    val eventName: String,
    val details: String,
    val createdBy: String,
    val participants: List<String>,
    val date: String,
    val location: Location
)
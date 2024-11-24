package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class EventCreateRequest(
    val eventName: String,
    val details: String,
    val date: String,
    val location: Location
)
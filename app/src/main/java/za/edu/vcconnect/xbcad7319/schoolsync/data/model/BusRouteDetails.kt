package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class BusRouteDetails(
    val _id: String,
    val routeName: String,
    val stops: List<Stop>,
    val liveLocation: LiveLocation,
    val scheduleId: String,
    val createdAt: String,
    val updatedAt: String
)
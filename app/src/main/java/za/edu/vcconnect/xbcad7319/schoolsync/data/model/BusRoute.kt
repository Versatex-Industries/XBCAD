package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class BusRoute(
    val _id: String,
    val routeName: String,
    val stops: List<Stop>,
    val liveLocation: LiveLocation
)
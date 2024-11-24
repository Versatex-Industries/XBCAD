package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class Location(
    val type: String,
    val coordinates: List<Double> // [longitude, latitude]
)
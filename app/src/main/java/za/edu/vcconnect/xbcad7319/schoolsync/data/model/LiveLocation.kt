package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class LiveLocation(
    val type: String,
    val coordinates: List<Double> // [longitude, latitude]
)
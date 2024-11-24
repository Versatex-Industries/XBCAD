package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class Notification(
    val _id: String,
    val userId: String,
    val type: String,
    val content: String,
    val read: Boolean,
    val createdAt: String
)

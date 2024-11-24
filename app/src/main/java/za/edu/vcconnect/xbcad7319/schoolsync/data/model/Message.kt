package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class Message(
    val senderId: String,
    val recipientId: String,
    val content: String,
    val timestamp: String
)


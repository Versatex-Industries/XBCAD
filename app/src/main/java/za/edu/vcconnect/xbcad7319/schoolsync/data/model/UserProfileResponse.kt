package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class UserProfileResponse(
    val profile: Profile,
    val _id: String,
    val username: String,
    val email: String,
    val deviceTokens: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val __v: Int,
    val role: String
)



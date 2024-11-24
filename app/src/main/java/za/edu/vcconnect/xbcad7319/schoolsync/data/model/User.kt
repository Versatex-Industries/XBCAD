package za.edu.vcconnect.xbcad7319.schoolsync.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String,          // Map "_id" from the API response to "id"
    val username: String,                           // Username of the user
    val email: String,                              // Email of the user
    val role: String,                               // Role of the user (e.g., Teacher, Student)
    val profile: Profile                            // Profile details of the user
)

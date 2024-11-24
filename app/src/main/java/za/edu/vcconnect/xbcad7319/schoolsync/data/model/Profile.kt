package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class Profile(
    val name: String?,
    val surname: String?,
    val grade: String?, // Student-specific
    val classId: String?, // Student-specific (converted from ObjectId)
    val busRouteId: String?, // Student-specific (converted from ObjectId)
    val subject: String?, // Teacher-specific
    val linkedChildren: List<String> // Parent-specific
)
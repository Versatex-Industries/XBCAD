package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class ParentProfile(
    val name: String?,
    val surname: String?,
    val linkedChildren: List<String> // Updated to reflect array of IDs
)
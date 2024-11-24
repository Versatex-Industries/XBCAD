package za.edu.vcconnect.xbcad7319.schoolsync.data.model

data class ClassResponse(
    val _id: String,
    val name: String,
    val students: List<StudentResponse>
)

package za.edu.vcconnect.xbcad7319.schoolsync.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.AttendanceResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.BusRoute
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.BusRouteDetails
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ChildResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassCreationRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassCreationResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassItem
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Conversation
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Event
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.EventCreateRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.EventCreateResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.GradeResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.JoinEventResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.LiveLocation
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.LiveLocationResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Message
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Notification
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.SelectRoleRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.SelectRoleResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.StudentResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.TimetableResponse
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.User
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileRequest
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileResponse

interface ApiService {
    // Authentication
    @POST("auth/register")
    fun registerUser(@Body requestBody: Map<String, String>): Call<Map<String, Any>>

    @POST("auth/login")
    fun loginUser(@Body requestBody: Map<String, String>): Call<Map<String, Any>>

    @POST("users/select-role")
    suspend fun selectRole(
        @Header("Authorization") authHeader: String,
        @Body request: SelectRoleRequest
    ): Response<SelectRoleResponse>

    // User Profile - TODO REMOVE
    @GET("users/profile")
    fun getUserProfile(@Header("Authorization") token: String): Call<Map<String, Any>>

    @GET("users/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<UserProfileResponse>

    @PUT("users/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body profileRequest: UserProfileRequest
    ): Response<UserProfileResponse>

    @PATCH("users/profile")
    fun updateUserProfile(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, @JvmSuppressWildcards Any>
    ): Call<Map<String, Any>>

    // Teachers
    @POST("teachers/create-class")
    suspend fun createClass(
        @Header("Authorization") token: String,
        @Body classRequest: ClassCreationRequest
    ): Response<ClassCreationResponse>


    // Add students to a class
    @POST("teachers/classes/{classId}/students")
    suspend fun addStudentsToClass(
        @Header("Authorization") authToken: String,
        @Path("classId") classId: String,
        @Body studentIds: HashMap<String, List<String>>
    ): Response<Void>

    // Get all students without a class
    @GET("teachers/students-without-class")
    suspend fun getStudentsWithoutClass(
        @Header("Authorization") token: String
    ): Response<List<User>>

    @GET("teachers/classes")
    suspend fun getClasses(
        @Header("Authorization") token: String
    ): Response<List<ClassResponse>>

    @GET("teachers/classes/{classId}/students")
    suspend fun getStudentsInClass(
        @Header("Authorization") authToken: String,
        @Path("classId") classId: String
    ): Response<List<StudentResponse>>
    // Fetch all available classes

    @GET("students/classes")
    suspend fun getAllClasses(
        @Header("Authorization") token: String
    ): Response<List<ClassItem>>

    @POST("notifications") // Replace with your actual endpoint
    suspend fun sendGlobalNotification(
        @Header("Authorization") token: String,
        @Body payload: Map<String, String>
    ): Response<Void>

    @POST("teachers/grades")
    suspend fun captureGrade(
        @Header("Authorization") authToken: String,
        @Body gradeData: HashMap<String, String>
    ): Response<Void>

    @GET("teachers/classes/students")
    suspend fun getStudentsFromClasses(
        @Header("Authorization") authToken: String
    ): Response<List<User>>

    @POST("teachers/attendance")
    suspend fun markAttendance(
        @Header("Authorization") authToken: String,
        @Body attendanceData: Map<String, String>
    ): Response<Void>

    @POST("teachers/timetable")
    suspend fun addTimetableEntry(
        @Header("Authorization") authToken: String,
        @Body payload: Map<String, String>
    ): Response<Void>

    // Timetable
    @GET("timetable/{classId}")
    suspend fun getTimetableForClass(
        @Header("Authorization") authToken: String,
        @Path("classId") classId: String
    ): Response<List<TimetableResponse>>


    @GET("students/attendance/{childId}")
    suspend fun getAttendanceForStudent(
        @Header("Authorization") authToken: String,
        @Path("childId") childId: String
    ): Response<List<AttendanceResponse>>


    @GET("students/{childId}")
    suspend fun getChildProfile(
        @Header("Authorization") authToken: String,
        @Path("childId") childId: String
    ): Response<ChildResponse>

    @PUT("settings/language")
    suspend fun updateLanguage(
        @Header("Authorization") authToken: String,
        @Body language: Map<String, String>
    ): Response<Unit>



    @GET("students/grades")
    suspend fun getGrades(@Header("Authorization") authToken: String): Response<List<GradeResponse>>


    // Notifications
    @GET("notifications")
    fun getNotifications(@Header("Authorization") token: String): Call<List<Map<String, Any>>>

    // Get all notifications for the logged-in user
    @GET("notifications")
    suspend fun getAllNotifications(
        @Header("Authorization") token: String
    ): Response<List<Notification>>

    // Mark a specific notification as read
    @PATCH("notifications/{notificationId}")
    suspend fun markNotificationAsRead(
        @Header("Authorization") token: String,
        @Path("notificationId") notificationId: String,
        @Body readStatus: Map<String, Boolean> // e.g., { "read": true }
    ): Response<Notification>

    // Get all bus routes
    @GET("buses/routes")
    suspend fun getAllRoutes(
        @Header("Authorization") token: String
    ): Response<List<BusRoute>>

    // Get details of a specific bus route
    @GET("buses/routes/{routeId}")
    suspend fun getRouteDetails(
        @Header("Authorization") token: String,
        @Path("routeId") routeId: String
    ): Response<BusRouteDetails>

    @GET("buses/location/{routeId}")
    suspend fun getLiveLocation(
        @Header("Authorization") token: String,
        @Path("routeId") routeId: String
    ): Response<LiveLocationResponse>


    @GET("events")
    suspend fun getEvents(
        @Header("Authorization") token: String
    ): Response<List<Event>>

    @POST("events")
    suspend fun createEvent(
        @Header("Authorization") token: String,
        @Body event: EventCreateRequest
    ): Response<EventCreateResponse>

    @POST("events/join/{eventId}")
    suspend fun joinEvent(
        @Header("Authorization") token: String,
        @Path("eventId") eventId: String
    ): Response<JoinEventResponse>

    @GET("messages/users")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<User>>


    // Get all conversations for the logged-in user
    @GET("messages")
    suspend fun getAllConversations(
        @Header("Authorization") token: String
    ): Response<List<Conversation>>

    // Get messages with a specific contact
    @GET("messages/{contactId}")
    suspend fun getMessagesWithContact(
        @Header("Authorization") token: String,
        @Path("contactId") contactId: String
    ): Response<List<Message>>

    // Send a message to a specific contact
    @POST("messages/{contactId}")
    suspend fun sendMessage(
        @Header("Authorization") token: String,
        @Path("contactId") contactId: String,
        @Body message: Message
    ): Response<Message>
}

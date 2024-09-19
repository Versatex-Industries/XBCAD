package za.edu.vcconnect.xbcad7319.schoolsync.api
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException

class ApiService {

    private val client = OkHttpClient()

    // Base URL for the API
    private val BASE_URL = "http://127.0.0.1:3000" // Replace with your actual API URL

    // 1. User Authentication Routes

    // Register a new user
    fun register(name: String, role: String, email: String, password: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("name", name)
            put("role", role)
            put("email", email)
            put("password", password)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/auth/register")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    // Login an existing user
    fun login(email: String, password: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("email", email)
            put("password", password)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/auth/login")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    // 2. Dashboard (with JWT authorization)

    fun getDashboard(token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/dashboard")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    // 3. Bus Tracking Routes

    fun getBusTracking(token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/bus-tracking")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun getBusSchedule(busId: String, token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/bus-tracking/schedule/$busId")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun checkinBus(busId: String, studentId: String, checkinTime: String, token: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("busId", busId)
            put("studentId", studentId)
            put("checkinTime", checkinTime)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/bus-tracking/checkin")
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    // 4. Donation Management Routes

    fun getDonations(token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/donations")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun postDonation(donationData: JSONObject, token: String, callback: Callback) {
        val body =
            donationData.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/donations")
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun getDonationHistory(userId: String, token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/donations/history/$userId")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    // 5. Community Engagement & Support Routes

    fun getVolunteerOpportunities(token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/community/volunteer-opportunities")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun signUpForVolunteer(opportunityId: String, token: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("opportunityId", opportunityId)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/community/sign-up-volunteer")
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun sendMessage(toUserId: String, message: String, token: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("toUserId", toUserId)
            put("message", message)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/community/message")
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    // 6. Resources (Education & Health) Routes

    fun getEducationResources(token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/resources/education")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun postHealthTracking(studentId: String, healthData: JSONObject, token: String, callback: Callback) {
        val requestBody = JSONObject().apply {
            put("studentId", studentId)
            put("data", healthData)
        }
        val body =
            requestBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder()
            .url("$BASE_URL/resources/health-tracking")
            .header("Authorization", "Bearer $token")
            .post(body)
            .build()
        client.newCall(request).enqueue(callback)
    }

    fun getHealthTracking(studentId: String, token: String, callback: Callback) {
        val request = Request.Builder()
            .url("$BASE_URL/resources/health-tracking/$studentId")
            .header("Authorization", "Bearer $token")
            .get()
            .build()
        client.newCall(request).enqueue(callback)
    }
}

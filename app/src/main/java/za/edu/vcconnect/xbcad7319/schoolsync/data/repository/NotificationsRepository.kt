package za.edu.vcconnect.xbcad7319.schoolsync.data.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class NotificationsRepository {
    /*private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun getNotifications(
        token: String,
        onSuccess: (List<Map<String, Any>>) -> Unit,
        onError: (String) -> Unit
    ) {
        api.getNotifications("Bearer $token").enqueue(object : Callback<List<Map<String, Any>>> {
            override fun onResponse(call: Call<List<Map<String, Any>>>, response: Response<List<Map<String, Any>>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Map<String, Any>>>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }

    fun sendNotification(
        token: String,
        userId: String,
        type: String,
        content: String,
        onSuccess: (Map<String, Any>) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf(
            "userId" to userId,
            "type" to type,
            "content" to content
        )
        api.sendNotification("Bearer $token", requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }

    fun markNotificationAsRead(
        token: String,
        notificationId: String,
        onSuccess: (Map<String, Any>) -> Unit,
        onError: (String) -> Unit
    ) {
        api.markNotificationAsRead("Bearer $token", notificationId).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }*/
}

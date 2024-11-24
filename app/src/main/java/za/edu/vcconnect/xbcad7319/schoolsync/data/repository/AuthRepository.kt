package za.edu.vcconnect.xbcad7319.schoolsync.data.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class AuthRepository {
    private val api = RetrofitClient.instance.create(ApiService::class.java)

    fun registerUser(
        username: String,
        email: String,
        password: String,
        onSuccess: (Map<String, Any>) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )
        api.registerUser(requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, Any>>,
                response: Response<Map<String, Any>>
            ) {
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

    /*fun loginUser(
        email: String,
        password: String,
        onSuccess: (Map<String, String>) -> Unit,
        onError: (String) -> Unit
    ) {
        val requestBody = mapOf(
            "email" to email,
            "password" to password
        )
        api.loginUser(requestBody).enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(
                call: Call<Map<String, String>>,
                response: Response<Map<String, String>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let { onSuccess(it) }
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Map<String, String>>, t: Throwable) {
                onError("Failure: ${t.message}")
            }
        })
    }*/
}

package za.edu.vcconnect.xbcad7319.schoolsync.data.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient

class TeachersRepository {
    /* private val api = RetrofitClient.instance.create(ApiService::class.java)

     fun createClass(
         token: String,
         name: String,
         onSuccess: (Map<String, Any>) -> Unit,
         onError: (String) -> Unit
     ) {
         val requestBody = mapOf("name" to name)
         api.createClass("Bearer $token", requestBody).enqueue(object : Callback<Map<String, Any>> {
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

     fun addStudentsToClass(
         token: String,
         classId: String,
         studentIds: List<String>,
         onSuccess: (Map<String, Any>) -> Unit,
         onError: (String) -> Unit
     ) {
         val requestBody = mapOf("studentIds" to studentIds)
         api.addStudentsToClass("Bearer $token", classId, requestBody).enqueue(object : Callback<Map<String, Any>> {
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

     fun getAllClasses(
         token: String,
         onSuccess: (List<Map<String, Any>>) -> Unit,
         onError: (String) -> Unit
     ) {
         api.getAllClasses("Bearer $token").enqueue(object : Callback<List<Map<String, Any>>> {
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
     }*/
}

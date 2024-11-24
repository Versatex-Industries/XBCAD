package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.ApiService
import za.edu.vcconnect.xbcad7319.schoolsync.data.api.RetrofitClient
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Event
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.UserProfileResponse

class EventsAdapter(
    private val events: List<Event>,
    private val onJoinButtonClick: (Event) -> Unit
) : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var userId: String? = null

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventName: TextView = itemView.findViewById(R.id.eventName)
        val eventDetails: TextView = itemView.findViewById(R.id.eventDetails)
        val joinButton: Button = itemView.findViewById(R.id.viewDetailsButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.eventName
        holder.eventDetails.text = event.details

        // Fetch User ID if not already fetched
        if (userId == null) {
            fetchUserId(holder.itemView) { fetchedUserId ->
                userId = fetchedUserId
                updateJoinButton(holder, event)
            }
        } else {
            updateJoinButton(holder, event)
        }
    }

    private fun updateJoinButton(holder: EventViewHolder, event: Event) {
        if (event.participants.contains(userId)) {
            holder.joinButton.text = "Already Joined"
            holder.joinButton.isEnabled = false
        } else {
            holder.joinButton.text = "Join Event"
            holder.joinButton.isEnabled = true
            holder.joinButton.setOnClickListener { onJoinButtonClick(event) }
        }
    }

    private fun fetchUserId(view: View, onUserIdFetched: (String?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val api = RetrofitClient.instance.create(ApiService::class.java)
                val response: Response<UserProfileResponse> = api.getProfile("Bearer ${getToken(view)}")
                if (response.isSuccessful) {
                    val userId = response.body()?._id
                    withContext(Dispatchers.Main) {
                        onUserIdFetched(userId)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onUserIdFetched(null)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onUserIdFetched(null)
                }
            }
        }
    }

    private fun getToken(view: View): String {
        val sharedPreferences = view.context.getSharedPreferences("AppPrefs", android.content.Context.MODE_PRIVATE)
        return sharedPreferences.getString("auth_token", "") ?: ""
    }


    override fun getItemCount(): Int = events.size
}

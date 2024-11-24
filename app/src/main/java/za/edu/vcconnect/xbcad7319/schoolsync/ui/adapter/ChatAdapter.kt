package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Message
import java.text.SimpleDateFormat
import java.util.*

class ChatAdapter(
    private val messages: MutableList<Message>,
    private var currentUserId: String
) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageContent)
        val messageTimestamp: TextView = itemView.findViewById(R.id.messageTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layout = if (viewType == VIEW_TYPE_SENT) {
            R.layout.item_message_sent
        } else {
            R.layout.item_message_received
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]

        // Set message content
        holder.messageText.text = message.content

        // Format and set the timestamp
        holder.messageTimestamp.text = formatTimestamp(message.timestamp)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]

        // Fallback in case currentUserId is empty
        if (currentUserId.isEmpty()) {
            println("Warning: currentUserId is empty!")
        }

        val viewType = if (message.senderId == currentUserId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
        println("Message: ${message.content}, SenderId: ${message.senderId}, CurrentUserId: $currentUserId, ViewType: $viewType")
        return viewType
    }

    override fun getItemCount(): Int = messages.size

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }

    fun updateCurrentUserId(newUserId: String) {
        currentUserId = newUserId
        notifyDataSetChanged() // Refresh view to rebind messages
    }

    private fun formatTimestamp(timestamp: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val formatter = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
            val date = parser.parse(timestamp)
            date?.let { formatter.format(it) } ?: "Invalid Time"
        } catch (e: Exception) {
            "Invalid Time"
        }
    }

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }
}

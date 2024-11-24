package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.Conversation
import java.text.SimpleDateFormat
import java.util.*

class ConversationsAdapter(
    private val conversations: List<Conversation>,
    private val currentUserId: String,
    private val onConversationClick: (Conversation) -> Unit
) : RecyclerView.Adapter<ConversationsAdapter.ConversationViewHolder>() {

    inner class ConversationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactName: TextView = itemView.findViewById(R.id.contactName)
        val lastMessage: TextView = itemView.findViewById(R.id.lastMessage)
        val lastMessageTime: TextView = itemView.findViewById(R.id.lastMessageTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conversation, parent, false)
        return ConversationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        val conversation = conversations[position]

        // Ensure the contact name is the other participant in the conversation
        holder.contactName.text = conversation.contactName ?: "Unknown"

        // Display the last message content
        holder.lastMessage.text = conversation.lastMessage ?: "No messages yet"

        // Format and set the last message timestamp
        holder.lastMessageTime.text = formatTimestamp(conversation.lastMessageTime)

        // Set click listener to open the chat
        holder.itemView.setOnClickListener { onConversationClick(conversation) }
    }

    override fun getItemCount(): Int = conversations.size

    private fun formatTimestamp(timestamp: String?): String {
        return if (!timestamp.isNullOrEmpty()) {
            try {
                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val formatter = SimpleDateFormat("h:mm a, MMM d", Locale.getDefault())
                val date = parser.parse(timestamp)
                date?.let { formatter.format(it) } ?: "Invalid Time"
            } catch (e: Exception) {
                "Invalid Time"
            }
        } else {
            "Unknown"
        }
    }
}

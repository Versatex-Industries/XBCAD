import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.Message
import za.edu.vcconnect.xbcad7319.schoolsync.R

class MessagesAdapter(private val messagesList: List<Message>) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val messageContent: TextView = view.findViewById(R.id.messageContent)
        val timestamp: TextView = view.findViewById(R.id.messageTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.messageitem, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messagesList[position]
        holder.messageContent.text = message.messageContent
        holder.timestamp.text = message.timestamp
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }
}

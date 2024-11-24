package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.TimetableResponse

class TimetableAdapter(
    private val timetableEntries: List<TimetableResponse>
) : RecyclerView.Adapter<TimetableAdapter.TimetableViewHolder>() {

    inner class TimetableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val periodText: TextView = itemView.findViewById(R.id.periodText)
        val subjectText: TextView = itemView.findViewById(R.id.subjectText)
        val teacherText: TextView = itemView.findViewById(R.id.teacherText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimetableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timetable, parent, false)
        return TimetableViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimetableViewHolder, position: Int) {
        val entry = timetableEntries[position]
        holder.dayText.text = entry.day
        holder.periodText.text = entry.period
        holder.subjectText.text = entry.subject
        holder.teacherText.text = entry.teacherName.ifEmpty { "N/A" }
    }

    override fun getItemCount(): Int = timetableEntries.size
}

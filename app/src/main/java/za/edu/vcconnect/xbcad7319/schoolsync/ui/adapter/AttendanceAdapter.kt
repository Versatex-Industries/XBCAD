package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.AttendanceResponse
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class AttendanceAdapter(private val attendanceList: List<AttendanceResponse>) :
    RecyclerView.Adapter<AttendanceAdapter.AttendanceViewHolder>() {

    class AttendanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateText: TextView = itemView.findViewById(R.id.dateText)
        val statusText: TextView = itemView.findViewById(R.id.statusText)
        val remarksText: TextView = itemView.findViewById(R.id.remarksText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_attendance, parent, false)
        return AttendanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AttendanceViewHolder, position: Int) {
        val attendance = attendanceList[position]

        val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputDateFormat.timeZone = TimeZone.getTimeZone("UTC") // Parse as UTC time

        val outputDateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) // Desired format
        outputDateFormat.timeZone = TimeZone.getDefault() // Convert to local time zone

        val formattedDate = try {
            val parsedDate = inputDateFormat.parse(attendance.date)
            outputDateFormat.format(parsedDate)
        } catch (e: Exception) {
            e.printStackTrace()
            attendance.date
        }

        holder.dateText.text = formattedDate
        holder.statusText.text = attendance.status
        holder.remarksText.text = attendance.remarks ?: "No remarks"
    }


    override fun getItemCount(): Int = attendanceList.size
}

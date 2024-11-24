package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.GradeResponse

class GradeAdapter(private val grades: List<GradeResponse>) :
    RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    inner class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectText: TextView = itemView.findViewById(R.id.subjectText)
        val gradeText: TextView = itemView.findViewById(R.id.gradeText)
        val remarksText: TextView = itemView.findViewById(R.id.remarksText)
        val timestampText: TextView = itemView.findViewById(R.id.timestampText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_grade, parent, false)
        return GradeViewHolder(view)
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val grade = grades[position]
        holder.subjectText.text = grade.subject
        holder.gradeText.text = grade.grade
        holder.remarksText.text = grade.remarks ?: "No remarks"
        holder.timestampText.text = grade.createdAt
    }

    override fun getItemCount(): Int = grades.size
}

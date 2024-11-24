package za.edu.vcconnect.xbcad7319.schoolsync

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class GradeItem(
    val studentName: String,
    val grade: String,
    val subject: String,
    val className: String,
    val testOrAssignment: String,
    val mark: Int
)

class GradesAdapter(private val gradesList: List<GradeItem>) :
    RecyclerView.Adapter<GradesAdapter.GradesViewHolder>() {

    class GradesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentNameTextView: TextView = itemView.findViewById(R.id.studentNameTextView)
        val gradeTextView: TextView = itemView.findViewById(R.id.gradeTextView)
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val classNameTextView: TextView = itemView.findViewById(R.id.classNameTextView)
        val testOrAssignmentTextView: TextView = itemView.findViewById(R.id.testOrAssignmentTextView)
        val markTextView: TextView = itemView.findViewById(R.id.markTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_grades_adapter, parent, false)
        return GradesViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: GradesViewHolder, position: Int) {
        val gradeItem = gradesList[position]
        holder.studentNameTextView.text = "Student: ${gradeItem.studentName}"
        holder.gradeTextView.text = "Grade: ${gradeItem.grade}"
        holder.subjectTextView.text = "Subject: ${gradeItem.subject}"
        holder.classNameTextView.text = "Class: ${gradeItem.className}"
        holder.testOrAssignmentTextView.text = "Test/Assignment: ${gradeItem.testOrAssignment}"
        holder.markTextView.text = "Mark: ${gradeItem.mark}"
    }

    override fun getItemCount(): Int = gradesList.size
}

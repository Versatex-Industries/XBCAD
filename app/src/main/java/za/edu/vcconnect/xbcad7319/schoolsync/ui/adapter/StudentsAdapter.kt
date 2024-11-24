package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.User

class StudentsAdapter(
    private val students: List<User>,
    private val onSelectionChange: (List<String>) -> Unit
) : RecyclerView.Adapter<StudentsAdapter.StudentViewHolder>() {

    private val selectedStudentIds = mutableSetOf<String>()

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.studentName)
        val checkBox: CheckBox = itemView.findViewById(R.id.studentCheckBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.studentName.text = "${student.profile.name} ${student.profile.surname}"
        holder.checkBox.isChecked = selectedStudentIds.contains(student.id)

        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedStudentIds.add(student.id)
            } else {
                selectedStudentIds.remove(student.id)
            }
            onSelectionChange(selectedStudentIds.toList())
        }
    }

    override fun getItemCount(): Int = students.size
}

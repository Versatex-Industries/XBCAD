package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.ClassResponse

class ClassesAdapter(
    private val classes: List<ClassResponse>,
    private val onClassClick: (ClassResponse) -> Unit
) : RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>() {

    // ViewHolder for each class item
    inner class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val className: TextView = itemView.findViewById(R.id.className)
        val studentCount: TextView = itemView.findViewById(R.id.studentCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_class, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classItem = classes[position]
        holder.className.text = classItem.name
        holder.studentCount.text = "Students: ${classItem.students.size}"
        holder.itemView.setOnClickListener { onClassClick(classItem) }
    }

    override fun getItemCount(): Int = classes.size
}

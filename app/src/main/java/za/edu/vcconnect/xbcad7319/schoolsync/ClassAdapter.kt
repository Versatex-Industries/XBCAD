package za.edu.vcconnect.xbcad7319.schoolsync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassesAdapter(private val classesList: List<String>) :
    RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_class_adapter, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val className = classesList[position]
        holder.classTextView.text = className
    }

    override fun getItemCount(): Int = classesList.size

    class ClassViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val classTextView: TextView = itemView.findViewById(R.id.classTextView)
    }
}

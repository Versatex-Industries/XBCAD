package za.edu.vcconnect.xbcad7319.schoolsync.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.edu.vcconnect.xbcad7319.schoolsync.R
import za.edu.vcconnect.xbcad7319.schoolsync.data.model.BusRoute

class BusRoutesAdapter(
    private var routes: List<BusRoute>,
    private val onClick: (BusRoute) -> Unit
) : RecyclerView.Adapter<BusRoutesAdapter.BusRouteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusRouteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bus_route, parent, false)
        return BusRouteViewHolder(view)
    }

    override fun onBindViewHolder(holder: BusRouteViewHolder, position: Int) {
        val route = routes[position]
        holder.bind(route)
        holder.itemView.setOnClickListener { onClick(route) }
    }

    override fun getItemCount(): Int = routes.size

    fun updateData(newRoutes: List<BusRoute>) {
        routes = newRoutes
        notifyDataSetChanged()
    }

    class BusRouteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val routeName: TextView = view.findViewById(R.id.routeName)

        fun bind(route: BusRoute) {
            routeName.text = route.routeName
        }
    }
}

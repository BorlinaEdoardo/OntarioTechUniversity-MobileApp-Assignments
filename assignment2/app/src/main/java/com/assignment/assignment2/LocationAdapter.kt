package com.assignment.assignment2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.assignment2.R
import com.assignment.assignment2.database.Location

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationVH>() {

    private val all = mutableListOf<Location>()
    private val visible = mutableListOf<Location>()

    fun submit(list: List<Location>) {
        all.clear()
        all.addAll(list)
        filter("")
    }

    fun filter(query: String) {
        val q = query.trim().lowercase()
        visible.clear()
        if (q.isEmpty()) {
            visible.addAll(all)
        } else {
            visible.addAll(all.filter { it.address.lowercase().contains(q) })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return LocationVH(v)
    }

    override fun getItemCount(): Int = visible.size

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        holder.bind(visible[position])
    }

    class LocationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val card: CardView = itemView as CardView
        private val id: TextView = itemView.findViewById(R.id.idTextView)
        private val address: TextView = itemView.findViewById(R.id.addressEditText)
        private val latitude: TextView = itemView.findViewById(R.id.latitudeEditText)
        private val longitude: TextView = itemView.findViewById(R.id.longitudeEditText)

        fun bind(location: Location) {
            id.text = location.id.toString()
            address.text = location.address
            latitude.text = location.latitude.toString()
            longitude.text = location.longitude.toString()


            card.setOnClickListener {
                // TODO: Navigate to detail screen
            }
        }
    }
}
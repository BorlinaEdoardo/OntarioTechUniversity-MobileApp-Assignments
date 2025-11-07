package com.assignment.assignment2


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.assignment2.R
import com.assignment.assignment2.database.DatabaseHelper
import com.assignment.assignment2.database.Location

class LocationAdapter : RecyclerView.Adapter<LocationAdapter.LocationVH>() {

    private val all = mutableListOf<Location>()
    private val visible = mutableListOf<Location>()
    private lateinit var databaseHelper: DatabaseHelper

    fun setDatabaseHelper(helper: DatabaseHelper) {
        this.databaseHelper = helper
    }

    fun submit(list: List<Location>) {
        all.clear()
        all.addAll(list)
        filter("")
    }

    fun edit(location: Location) {
        databaseHelper.updateLocation(location)
        val index = all.indexOfFirst { it.id == location.id }
        if (index != -1) {
            all[index] = location
        }
        filter("")
    }

    fun delete(location: Location) {
        databaseHelper.deleteLocation(location.id!!)
        all.remove(location)
        visible.remove(location)
        notifyDataSetChanged()
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
        val vh = LocationVH(v)
        vh.setDatabaseHelper(databaseHelper)
        return vh
    }

    override fun getItemCount(): Int = visible.size

    override fun onBindViewHolder(holder: LocationVH, position: Int) {
        holder.bind(visible[position])
    }

    class LocationVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private lateinit var databaseHelper: DatabaseHelper

        private val card: CardView = itemView as CardView
        private val id: TextView = itemView.findViewById(R.id.idTextView)
        private val address: TextView = itemView.findViewById(R.id.addressEditText)
        private val latitude: TextView = itemView.findViewById(R.id.latitudeEditText)
        private val longitude: TextView = itemView.findViewById(R.id.longitudeEditText)

        private val editButton: Button = itemView.findViewById(R.id.editButton)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun setDatabaseHelper(helper: DatabaseHelper) {
            this.databaseHelper = helper
        }

        fun bind(location: Location) {
            id.text = "Location #${location.id}"
            address.text = location.address
            latitude.text = location.latitude.toString()
            longitude.text = location.longitude.toString()


            card.setOnClickListener {
                // TODO: Navigate to detail screen
            }

            editButton.setOnClickListener{
                val adapter = bindingAdapter as LocationAdapter
                adapter.edit(Location(
                    id = location.id,
                    address = address.text.toString(),
                    latitude = latitude.text.toString().toDoubleOrNull() ?: 0.0,
                    longitude = longitude.text.toString().toDoubleOrNull() ?: 0.0
                ))
            }

            deleteButton.setOnClickListener{
                val adapter = bindingAdapter as LocationAdapter
                adapter.delete(location)
            }
        }
    }
}
package com.example.fastfood.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.fastfood.model.RestaurantResponse
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfood.Details
import com.example.fastfood.R

import com.example.fastfood.model.Restaurant

class RestaurantAdapter(private val itemList: List<Restaurant>, context: Context) :
    RecyclerView.Adapter<RestaurantAdapter.ViewHolder>(){

    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.restaurant_item, parent, false)
        return RestaurantAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        Log.d("NotificationFrag", itemList.size.toString())

        holder.nameRestaurant.text = item.name
        holder.rakingRestaurant.text = item.rating.toString()
        holder.peopleRestaurant.text = item.user_ratings_total.toString()

        if (item.opening_hours != null) {
            if (item.opening_hours.open_now == true) {
                holder.statusRestaurant.text = "Abierto"
                holder.statusRestaurant.setTextColor(ContextCompat.getColor(context, R.color.color_abierto))
            } else {
                holder.statusRestaurant.text = "Cerrado"
                holder.statusRestaurant.setTextColor(ContextCompat.getColor(context, R.color.color_cerrado))
            }
        } else {
            holder.statusRestaurant.text = "Horario desconocido"
            holder.statusRestaurant.setTextColor(ContextCompat.getColor(context, R.color.gray))

        }

        val context = holder.itemView.context

        holder.button_details_Restaurant.setOnClickListener{
            var i = Intent(context,Details::class.java)

            i.putExtra("name", item.name)
            if (item.opening_hours == null) {
                i.putExtra("opening_hours", "Horario desconocido")
            } else {
                i.putExtra("opening_hours", item.opening_hours.open_now.toString())
            }
            i.putExtra("photos", item.photos[0].photo_reference)
            i.putExtra("vicinity", item.vicinity)
            i.putExtra("user_ratings_total", item.user_ratings_total)
            i.putExtra("rating",item.rating)
            i.putExtra("reference",item.reference)

            context.startActivity(i)

        }

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {
        var nameRestaurant = view.findViewById<TextView>(R.id.name)
        var rakingRestaurant = view.findViewById<TextView>(R.id.ranking)
        var peopleRestaurant = view.findViewById<TextView>(R.id.people)
        var statusRestaurant = view.findViewById<TextView>(R.id.status)
        var button_details_Restaurant = view.findViewById<ImageButton>(R.id.image_seemore)
    }

}

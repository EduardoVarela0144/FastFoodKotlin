package com.example.fastfood

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fastfood.adapter.RestaurantAdapter
import com.example.fastfood.model.RestaurantDirection
import com.example.fastfood.model.RestaurantResponse
import com.example.fastfood.services.RestaurantApiService
import com.example.fastfood.utils.AppConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Details : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var retrofit : Retrofit
    private lateinit var apiService: RestaurantApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val imageView = findViewById<ImageView>(R.id.image_restaurant)

        val photos = intent.getStringExtra("photos")

        val imageUrl =
            "${AppConstants.LOCATION_BASE_URL}photo?maxwidth=400&photoreference=${photos}&key=${AppConstants.API_KEY}"

        Glide.with(this)
            .load(imageUrl)
            .placeholder(R.drawable.not_found)
            .error(R.drawable.not_found)
            .into(imageView)

        val name = intent.getStringExtra("name")
        val openingHours = intent.getStringExtra("opening_hours",)


        val vicinity = intent.getStringExtra("vicinity")
        val userRatingsTotal = intent.getIntExtra("user_ratings_total", 0)
        val rating = intent.getDoubleExtra("rating", 0.0)
        val reference = intent.getStringExtra("reference")

        val nameTextView = findViewById<TextView>(R.id.name_details)
        nameTextView.text = name

        val vicinityTextView = findViewById<TextView>(R.id.vicinity_details)
        vicinityTextView.text = vicinity

        val userratingstotalTextView = findViewById<TextView>(R.id.people)
        userratingstotalTextView.text = userRatingsTotal.toString()

        val ratingsTotalTextView = findViewById<TextView>(R.id.ranking)
        ratingsTotalTextView.text = rating.toString()

        val openingTextView = findViewById<TextView>(R.id.status)

        val context = this

        if (openingHours != null) {
            if (openingHours == "true") {
                openingTextView.text = "Abierto"
                openingTextView.setTextColor(ContextCompat.getColor(context, R.color.color_abierto))
            } else if(openingHours == "false") {
                openingTextView.text = "Cerrado"
                openingTextView.setTextColor(ContextCompat.getColor(context, R.color.color_cerrado))

            }
            else {
                openingTextView.text = "Horario desconocido"
                openingTextView.setTextColor(ContextCompat.getColor(context, R.color.gray))

            }
        }

        retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RestaurantApiService::class.java)

        getDirection(reference.toString())



    }

    private fun getDirection(reference:String){
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getRestaurantDirection(reference,"formatted_address",AppConstants.API_KEY)
                .enqueue(object : Callback<RestaurantDirection> {
                    override fun onResponse(
                        call: Call<RestaurantDirection>,
                        response: Response<RestaurantDirection>
                    ) {
                        //Si hay respuesta
                        if(response.isSuccessful){
                            Log.d("Formateado", response.body().toString())

                            val formattedAddress = response.body()?.result?.formatted_address

                            val directionTextView = findViewById<TextView>(R.id.direction_details)
                            directionTextView.text = formattedAddress


                        }else{
                            Toast.makeText(this@Details,
                                "Hubo un error",
                                Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RestaurantDirection>, t: Throwable) {
                        Log.d("DashboardFrag", "onFailure ${t.message}")
                    }

                })
        }
    }
}
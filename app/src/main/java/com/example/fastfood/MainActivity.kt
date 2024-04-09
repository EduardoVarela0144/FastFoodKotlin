package com.example.fastfood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fastfood.adapter.RestaurantAdapter
import com.example.fastfood.model.Restaurant
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
import androidx.lifecycle.lifecycleScope
import com.example.fastfood.services.LocationService


class MainActivity : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var apiService: RestaurantApiService
    private val locationService: LocationService = LocationService()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retrofit = Retrofit.Builder()
            .baseUrl(AppConstants.LOCATION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RestaurantApiService::class.java)


        lifecycleScope.launch {
            val result = locationService.getLocation(this@MainActivity)
            if (result != null) {
                Toast.makeText(
                    this@MainActivity,
                    "Latitud: ${result.latitude} Longitud: ${result.longitude}", Toast.LENGTH_LONG
                ).show()

                getRestaurants(result.latitude.toString(),result.longitude.toString());

            }

        }

    }


    private fun getRestaurants(lat:String,lng:String) {
        CoroutineScope(Dispatchers.IO).launch {
            apiService.getRestaurantData(
                "${lat},${lng}",
                "1000",
                "restaurant",
                AppConstants.API_KEY
            )
                .enqueue(object : Callback<RestaurantResponse> {
                    override fun onResponse(
                        call: Call<RestaurantResponse>,
                        response: Response<RestaurantResponse>
                    ) {
                        //Si hay respuesta
                        if (response.isSuccessful) {
                            Log.d("NotificationFrag", response.body().toString())

                            val recyclerView = findViewById<RecyclerView>(R.id.lista_restaurantes)

                            val adapter = response.body()
                                ?.let { RestaurantAdapter(it.results, this@MainActivity) }

                            recyclerView.adapter = adapter
                            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Hubo un error",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<RestaurantResponse>, t: Throwable) {
                        Log.d("DashboardFrag", "onFailure ${t.message}")
                    }

                })
        }
    }


}
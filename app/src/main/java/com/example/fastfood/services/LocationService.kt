package com.example.fastfood.services

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.suspendCancellableCoroutine
import com.google.android.gms.location.LocationServices


class LocationService {
    @SuppressLint("ServiceCast")
    suspend fun getLocation(context: Context): Location?{
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        val isUserLocationPermissionsGranted = true
        val locationManager  = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGpsEnabled =
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )

        if(!isGpsEnabled || !isUserLocationPermissionsGranted){
            return null
        }

        return suspendCancellableCoroutine {
                cont -> if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return@suspendCancellableCoroutine
        }
            fusedLocationProviderClient.lastLocation.apply{
                if(isComplete){
                    if(isSuccessful){
                        cont.resume(result){}
                    }
                    else{
                        cont.resume(null){}
                    }
                    return@suspendCancellableCoroutine

                }
                addOnSuccessListener {
                    cont.resume(it){}
                }
                addOnFailureListener{
                    cont.resume(null){}
                }
                addOnCanceledListener { cont.resume(null){} }
            }
        }
    }
}
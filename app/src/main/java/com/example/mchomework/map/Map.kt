package com.example.mchomework.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.GnssAntennaInfo
import android.location.GpsStatus
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.example.mchomework.Graph
import com.example.mchomework.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.CancellationToken
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*

@Composable
fun MyMap(
    navController: NavController,
    fusedLocationClient: FusedLocationProviderClient,
    activity: Activity
): LatLng? {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val position = rememberSaveable { mutableStateOf( LatLng(65.056, 25.450) ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView( { mapView } ) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true


                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(position.value, 15f)
                )
                Log.d("tag", "hei")
                val color = Color.HSVToColor(50, floatArrayOf(265F, 87F, 98F))
                val options = CircleOptions()
                    .radius(100.0)
                    .fillColor(color)
                    .strokeColor(color)

                map.clear()
                map.addCircle(
                    options.center(position.value)
                )


                map.setOnMapLongClickListener { latlng ->
                    position.value = latlng
                }
                if (ActivityCompat.checkSelfPermission(
                        Graph.appContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        Graph.appContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION),
                        1)
                }
                map.isMyLocationEnabled = true
                map.setOnMyLocationButtonClickListener {

                    if (ActivityCompat.checkSelfPermission(
                            Graph.appContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            Graph.appContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            activity,
                            arrayOf(
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION),
                            1)
                    }
                    val lastLocation = fusedLocationClient.lastLocation
                    while (!lastLocation.isComplete) {
                    }
                    val location = LatLng(
                        lastLocation.result.latitude,
                        lastLocation.result.longitude
                    )
                    position.value = location
                    false
                }
            }
        }
    }
    return position.value
}

fun requestPermissions(activity: Activity) {

}
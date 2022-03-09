package com.example.mchomework.map

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.util.*

@Composable
fun MyMap(
    navController: NavController
): LatLng? {
    val mapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val markerPosition = rememberSaveable { mutableStateOf( null as LatLng? ) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AndroidView( { mapView } ) { mapView ->
            coroutineScope.launch {
                val map = mapView.awaitMap()
                map.uiSettings.isZoomControlsEnabled = true
                val location = LatLng(65.056, 25.450)

                map.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 15f)
                )

                var marker: Marker? = null

                map.setOnMapLongClickListener { latlng ->
                    marker?.remove()
                    marker = map.addMarker(
                        MarkerOptions().position(latlng)
                    )
                    markerPosition.value = marker?.position
                }
            }
        }
    }
    return markerPosition.value
}

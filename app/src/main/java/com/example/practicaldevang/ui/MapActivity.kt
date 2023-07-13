package com.example.practicaldevang.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.practicaldevang.R
import com.example.practicaldevang.data.DataLoc
import com.example.practicaldevang.databinding.ActivityMapBinding
import com.example.practicaldevang.viewModel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.gson.Gson


class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var type: String? = ""
    private lateinit var binding: ActivityMapBinding
    private lateinit var mMap: GoogleMap
    private lateinit var viewModal: LocationViewModel
    private var place: Place? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initModel()
        initMap()
    }

    private fun initData() {
        type = intent.getStringExtra("type")
        if (type.equals("Edit")) {
            // on below line we are setting data to edit text.
            val data = intent.getStringExtra("data")
            val dataLoc = Gson().fromJson(
                data,
                DataLoc::class.java
            )
            val latLng = LatLng(dataLoc.lat.toDouble(), dataLoc.lon.toDouble())
            mMap.clear()
            mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Marker in ${dataLoc.title}")
            )
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    latLng, 14f
                )
            )
            binding.txtPlaces.text = dataLoc.address
            binding.txtSave.text = "Update"
            binding.txtSave.visibility = View.VISIBLE
            binding.appCompatTextView.visibility = View.INVISIBLE
        } else {
            binding.txtSave.text = "Save"
        }
    }

    private fun initModel() {
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(LocationViewModel::class.java)

        binding.txtSave.setOnClickListener {
            if (place != null) {
                if (type?.equals("Edit") == true) {
                    viewModal.updateLocationData(
                        DataLoc(
                            place?.name!!.toString(),
                            place?.address!!.toString(),
                            place?.latLng!!.latitude.toString(),
                            place?.latLng!!.longitude.toString()
                        )
                    )
                } else {
                    viewModal.addLocationData(
                        DataLoc(
                            place?.name!!.toString(),
                            place?.address!!.toString(),
                            place?.latLng!!.latitude.toString(),
                            place?.latLng!!.longitude.toString()
                        )
                    )
                }
                onBackPressed()

            }
        }
    }


    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this@MapActivity)
        Places.initialize(this@MapActivity, getString(R.string.google_maps_key))
        val fields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        binding.txtPlaces.setOnClickListener {
            val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this)
            startAutocomplete.launch(intent)
        }
    }

    private val startAutocomplete =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val intent = result.data
                if (intent != null) {
                    val placeSearch = Autocomplete.getPlaceFromIntent(intent)
                    Log.e("Tag", placeSearch.toString())
                    Log.i(
                        "TAG_LOC", "Place: ${placeSearch.name}, ${placeSearch.id}"
                    )
                    if (placeSearch.latLng != null) {
                        mMap.clear()
                        mMap.addMarker(
                            MarkerOptions()
                                .position(placeSearch.latLng!!)
                                .title("Marker in ${placeSearch.name}")
                        )
                        mMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                placeSearch.latLng!!, 14f
                            )
                        )
                        binding.txtPlaces.text = placeSearch.address
                        place = placeSearch
                        binding.appCompatTextView.visibility = View.VISIBLE
                        binding.txtSave.visibility = View.VISIBLE
                    } else {
                        Log.i("TAG_LOC", "unable to add marker..")
                        binding.txtPlaces.text = getString(R.string.search_place_name)
                    }

                }
            } else if (result.resultCode == Activity.RESULT_CANCELED) {
                // The user canceled the operation.
                Log.i("TAG_LOC", "User canceled autocomplete")
            }
        }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        }
        initData()
    }
}
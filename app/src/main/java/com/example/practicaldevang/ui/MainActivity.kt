package com.example.practicaldevang.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practicaldevang.adapter.LocClickDeleteInterface
import com.example.practicaldevang.adapter.LocClickInterface
import com.example.practicaldevang.adapter.LocationListAdapter
import com.example.practicaldevang.data.DataLoc
import com.example.practicaldevang.databinding.ActivityMainBinding
import com.example.practicaldevang.viewModel.LocationViewModel
import com.google.gson.Gson

class MainActivity : AppCompatActivity(), LocClickDeleteInterface, LocClickInterface {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModal: LocationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }


    private fun initView() {
        binding.btnAddLocation.setOnClickListener {
            startActivity(Intent(this@MainActivity, MapActivity::class.java))
        }
        binding.recyclerData.layoutManager = LinearLayoutManager(this)

        // on below line we are initializing our adapter class.
        val locationListAdapter = LocationListAdapter(this, this)

        // on below line we are setting
        // adapter to our recycler view.
        binding.recyclerData.adapter = locationListAdapter

        // on below line we are
        // initializing our view modal.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(LocationViewModel::class.java)

        viewModal.allLocations.observe(this, Observer { list ->
            list?.let {
                // on below line we are updating our list.
                if (list.isEmpty()) {
                    binding.txtNoData.visibility = View.VISIBLE
                    binding.recyclerData.visibility = View.INVISIBLE
                } else {
                    binding.txtNoData.visibility = View.INVISIBLE
                    binding.recyclerData.visibility = View.VISIBLE
                }
                locationListAdapter.updateList(it)
            }
        })
    }

    override fun onDeleteIconClick(dataLoc: DataLoc) {
        viewModal.deleteLocationData(dataLoc)
        Toast.makeText(this, "${dataLoc.title} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onLocClick(dataLoc: DataLoc) {
        val gson = Gson()
        val intent = Intent(this@MainActivity, MapActivity::class.java)
        intent.putExtra("data", gson.toJson(dataLoc))
        intent.putExtra("type", "Edit")
        startActivity(intent)
    }
}
package com.example.blockassessmentsurvey

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class GpsLocationActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Location"
        private const val ADDR_STRING = "address"
    }

    // UI elements
    private lateinit var addrText: EditText
    private lateinit var getLocationButton: Button
    private lateinit var submitButton : Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {

        /*
        Required call through to Activity.onCreate()
        Restore any saved instance state, if necessary
        */
        super.onCreate(savedInstanceState)

        // Set content view
        setContentView(R.layout.activity_get_gps_location)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        // Initialize UI elements
        addrText = findViewById(R.id.location)
        getLocationButton = findViewById(R.id.mapButton)
        submitButton = findViewById(R.id.submitButton)


        // Link UI elements to actions in code
        getLocationButton.setOnClickListener { loadAddress() }
        submitButton.setOnClickListener { submit() }

    }

    private fun submit() {
        if(addrText.text.isBlank()) {
            Toast.makeText(applicationContext, "Please enter valid address to continue.", Toast.LENGTH_LONG).show()
            return
        }
        // Check if real address entered
        val addressString = addrText.text.toString()
        //val addressList = Geocoder(this).getFromLocationName(addressString, 3)
        if(addressString.isEmpty()){
            Toast.makeText(applicationContext, "Please enter valid address to continue.", Toast.LENGTH_LONG).show()
        } else {
            //Toast.makeText(applicationContext, "Thanks!", Toast.LENGTH_LONG).show()
            val data = Intent()
            data.putExtra(ADDR_STRING, addressString)
            setResult(Activity.RESULT_OK, data)
            finish()
        }
    }


    private fun loadAddress() {
        Log.i(TAG, "loadAddress() called")


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            Log.i(TAG, "Permission denied")
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : android.location.Location? ->
                    // Got last known location. In some rare situations this can be null.
                    val latitude = location!!.latitude.toString()
                    val longitude = location!!.longitude.toString()
                    Log.i(TAG, "Latitude: $latitude")
                    Log.i(TAG, "Longitude: $longitude")
                    val addressList = Geocoder(this).getFromLocation(latitude.toDouble(), longitude.toDouble(), 3)
                    val address = addressList[0].getAddressLine(0)
                    Log.i(TAG, "Address: $address")
                    addrText.setText(address)

                    // Create Intent object for starting Google Maps application
                    /*val geoIntent = Intent(
                        Intent.ACTION_VIEW, Uri
                            .parse("geo:$latitude, $longitude"))

                    if (packageManager.resolveActivity(geoIntent, 0) != null) {
                        // Use the Intent to start Google Maps application using Activity.startActivity()
                        startActivity(geoIntent)
                    }*/

                }
        } catch (e: Exception) {
            // Log any error messages to LogCat using Log.e()
            Log.e(TAG, e.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        Log.i(TAG, "onRequestPermissionsResult called")
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    loadAddress()

                } else {

                    Log.i(TAG, "Address not loaded --- Permission was not granted")

                }
                return
            }
        }// other 'case' lines to check for other
        // permissions this app might request
    }


    override fun onStart() {
        super.onStart()
        Log.i(TAG, "The activity is visible and about to be started.")
    }


    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "The activity is visible and about to be restarted.")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "The activity is visible and has focus (it is now \"resumed\")")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,
            "Another activity is taking focus (this activity is about to be \"paused\")")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "The activity is no longer visible (it is now \"stopped\")")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "The activity is about to be destroyed.")
    }
    }

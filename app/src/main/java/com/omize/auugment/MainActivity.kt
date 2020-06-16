package com.omize.auugment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val PERMISSION_REQUEST = 10

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var locationManager: LocationManager
    private var netallowed = false
    private var gpsallowed = false
    private var gpsloca: Location? = null
    private var netloca: Location? = null

    private lateinit var mMap: GoogleMap
    private var posix = 0.0
    private var posiy = 0.0
    private var permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        setContentView(R.layout.activity_maps)
        if (!checkPermission(permissions)) {
            requestPermissions(permissions, PERMISSION_REQUEST)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        findViewById<FloatingActionButton>(R.id.fabLocate).setOnClickListener {
            launchAR()
            //getLocation()
            //updatePosition(posix, posiy)
        }

        findViewById<FloatingActionButton>(R.id.fabInventory).setOnClickListener {
            launchInventory()
        }

        findViewById<FloatingActionButton>(R.id.fabSettings).setOnClickListener {
            launchSettings()
        }

        Toast.makeText(this, "Loading...", Toast.LENGTH_SHORT).show()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        posix = 49.200218
        posiy = 16.607893
        val loca = LatLng(posix, posiy)
        val circle = mMap.addCircle(
            CircleOptions()
                .center(loca)
                .radius(5.0)
                .fillColor(Color.BLUE)
        )
        mMap.addMarker(MarkerOptions().position(loca).title("BETA TADY JSI"))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loca, 13.0f))
    }

    private fun updatePosition(coordinateX: Double, coordinateY: Double, camscale: Float = 18.0f) {
        posix = coordinateX
        posiy = coordinateY
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(posix, posiy), camscale))
    }

    fun launchAR() {
        val intent = Intent(this, ArActivity::class.java)
        startActivity(intent)
    }
    fun launchInventory() {
        val intent = Intent(this, InvActivity::class.java)
        startActivity(intent)
    }
    fun launchSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        gpsallowed = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        netallowed = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gpsallowed || netallowed) {
            if (gpsallowed) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0F, object: LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                gpsloca = location
                                posiy = gpsloca!!.latitude
                                posix = gpsloca!!.longitude
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String?) {}
                        override fun onProviderDisabled(provider: String?) {}
                    }
                )

                val gpslocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (gpslocation != null) gpsloca = gpslocation
            }

            if (netallowed) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0F, object: LocationListener {
                        override fun onLocationChanged(location: Location?) {
                            if (location != null) {
                                netloca = location
                                posiy = netloca!!.latitude
                                posix = netloca!!.longitude
                            }
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String?) {}
                        override fun onProviderDisabled(provider: String?) {}
                    }
                )

                val netlocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (netlocation != null) netloca = netlocation
            }
        }
    }

    private fun checkPermission(permissionArray: Array<String>): Boolean {
        for (i in permissionArray.indices) {
            if (checkCallingOrSelfPermission(permissionArray[i]) == PackageManager.PERMISSION_DENIED) return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST) {
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    val again = shouldShowRequestPermissionRationale(permissions[i])
                    if (!again) {
                        Toast.makeText(this, "Je nutné povolit v nastavení potřebná oprávnění", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Oprávnění neudělena", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
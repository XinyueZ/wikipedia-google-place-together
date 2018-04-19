package immowelt.ht

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import immowelt.ht.api.Service
import immowelt.ht.geo.GeoResult
import okhttp3.OkHttpClient
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MapsActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleApiClient.OnConnectionFailedListener {

    private lateinit var map: GoogleMap
    private lateinit var service: Service
    private lateinit var client: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAll()
        buildClient()
        buildService()
    }

    private fun startGoogleMap(mapFragment: SupportMapFragment) {
        mapFragment.getMapAsync(this)
    }

    private fun buildService() {
        val r = Retrofit.Builder().baseUrl("https://de.wikipedia.org/w/")
            .client(
                OkHttpClient().newBuilder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(15, TimeUnit.SECONDS)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
        service = r.create(Service::class.java)
    }

    private fun buildClient() {
        client = GoogleApiClient.Builder(this)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API)
            .addApi(LocationServices.API)
            .enableAutoManage(this, this)
            .build()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    @AfterPermissionGranted(RC_LOCATION)
    fun startAll() {
        if (EasyPermissions.hasPermissions(this, ACCESS_FINE_LOCATION)) {
            setContentView(R.layout.activity_maps)
            startGoogleMap(
                supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "Open location permission",
                RC_LOCATION,
                ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.isMyLocationEnabled = true
        map.setOnCameraIdleListener {
            askGoogle()
            if (map.myLocation != null)
                askWikipedia(500, LatLng(map.myLocation.latitude, map.myLocation.longitude))
        }
    }

    @SuppressLint("MissingPermission")
    private fun askGoogle() {
        val result = Places.PlaceDetectionApi.getCurrentPlace(client, null)
        result.setResultCallback { likelyPlaces ->
            for (placeLikelihood in likelyPlaces) {
                Log.i(
                    TAG, String.format(
                        "Place '%s' has likelihood: %g",
                        placeLikelihood.place.name,
                        placeLikelihood.likelihood
                    )
                )
                map.addMarker(
                    MarkerOptions().position(placeLikelihood.place.latLng).title(
                        placeLikelihood.place.name.toString()
                    ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                )
            }

            likelyPlaces.release()
        }
    }

    private fun askWikipedia(radius: Int = 500, latLng: LatLng) {
        service.getWikipediaGeoSearch(
            radius,
            String.format("%s|%s", latLng.latitude.toString(), latLng.longitude.toString())
        ).enqueue(object : Callback<GeoResult> {
            override fun onResponse(call: Call<GeoResult>?, response: Response<GeoResult>?) {
                response?.body()?.apply {
                    query.geosearches.forEach {
                        map.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude)).title(
                                it.title
                            ).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<GeoResult>?, t: Throwable?) {
            }
        })
    }

    companion object {
        private val TAG by lazy { MapsActivity.javaClass.simpleName }
        private const val RC_LOCATION = 987
    }

}

package immowelt.ht.api

import immowelt.ht.geo.GeoResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service {
    @GET("api.php?format=json&action=query&list=geosearch&gslimit=max")
    fun getWikipediaGeoSearch(@Query("gsradius") radius: Int, @Query("gscoord") latLng: String): Call<GeoResult>
}
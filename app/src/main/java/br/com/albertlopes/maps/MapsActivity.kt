package br.com.albertlopes.maps

import android.graphics.Color
import android.location.Geocoder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.setOnMapClickListener {

            addMarcador(it, getAdress(it))

        }

        mMap.setOnMapLongClickListener {


            addMarcador(it, getAdress(it))

        }

        addFiaps()



    }

    private fun addMarcador(latLong: LatLng, titulo: String) {

        mMap.addMarker(MarkerOptions()
                .position(latLong)
                .snippet(titulo)
        )

    }

    private fun addFiaps() {

        val fiapPaulista = LatLng(-23.563844, -46.652431)
        val fiapAclimacao = LatLng(-23.592924, -46.685355)
        val fiapVilaOlimpia = LatLng(-23.595050, -46.685355)

        val textSnipt = "Clique aqui para remover o ponto"

        val bitMap = BitmapDescriptorFactory.fromResource(R.drawable.boy)

        mMap.addMarker(MarkerOptions()
                .icon(bitMap)
                .snippet(textSnipt)
                .position(fiapPaulista)
                .title(getAdress(fiapPaulista)))

        mMap.addMarker(MarkerOptions()
                .icon(bitMap)
                .snippet(textSnipt)
                .position(fiapAclimacao)
                .title(getAdress(fiapAclimacao)))

        mMap.addMarker(MarkerOptions()
                .icon(bitMap)
                .snippet(textSnipt)
                .position(fiapVilaOlimpia)
                .title(getAdress(fiapVilaOlimpia)))


        val circulo =  CircleOptions()

        circulo.radius(200.0)
        circulo.visible(true)
        circulo.fillColor(Color.argb(50,255,0,255))
        circulo.strokeColor(Color.argb(255,255,0,255))
        circulo.center(fiapPaulista)

        mMap.addCircle(circulo)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(fiapPaulista, 16f))

    }

    private fun getAdress(latlong: LatLng): String {

        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val endereco = geocoder.getFromLocation(latlong.latitude, latlong.longitude, 1)

        return "${endereco[0].thoroughfare}, ${endereco[0].subThoroughfare} " +
                "${endereco[0].subLocality}, ${endereco[0].locality} - " +
                "${endereco[0].postalCode}"


    }
}

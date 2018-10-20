package br.com.albertlopes.maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.*
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.Toast
import br.com.albertlopes.maps.utils.PermissaoUtils

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

    val permissoesLocalizacao = listOf(Manifest.permission.ACCESS_FINE_LOCATION)

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        PermissaoUtils.validaPermissao(permissoesLocalizacao.toTypedArray(), this, 1)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for (resposta in grantResults) {
            if (resposta == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(applicationContext, "Sem permissao sem acesso", Toast.LENGTH_LONG).show()
            } else {

                onRequestLocationUpdate()

            }
        }
    }


    private fun onRequestLocationUpdate() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            var locationManager = getSystemService(Context.LOCATION_SERVICE)
                    as LocationManager

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)

        }
    }

    private fun initLocationListener() {

        locationListener = object : LocationListener {

            override fun onLocationChanged(location: Location?) {

                val minhaPosicao = LatLng(location?.latitude!!, location?.longitude!!)
                addMarcador(minhaPosicao, "posicao no maps")
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(minhaPosicao, 12f))

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap

        mMap.setOnMapClickListener {

          //  addMarcador(it, getAdress(it))

        }

        mMap.setOnMapLongClickListener {


          //  addMarcador(it, getAdress(it))

        }

       // addFiaps()

        initLocationListener()
        onRequestLocationUpdate()

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


        val circulo = CircleOptions()

        circulo.radius(200.0)
        circulo.visible(true)
        circulo.fillColor(Color.argb(50, 255, 0, 255))
        circulo.strokeColor(Color.argb(255, 255, 0, 255))
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

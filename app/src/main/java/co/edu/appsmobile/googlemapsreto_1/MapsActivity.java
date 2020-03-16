package co.edu.appsmobile.googlemapsreto_1;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.SphericalUtil;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationProvider.OnLocationReceivedListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private TextView text;
    private Marker me,act;
    private FloatingActionButton fbtn;
    private Geocoder geocoder;

    private LocationProvider gpsProvider;
    private LocationProvider networkProvider;

    private double minAccuracy = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        geocoder = new Geocoder(this, Locale.getDefault());

        text = findViewById(R.id.tv_map);
        fbtn = findViewById(R.id.fbtn_map);


        fbtn.setOnClickListener(
                (v) -> {
                    if(me !=null) {
                        Intent i = new Intent(this, AddMarker.class);
                        Marcador m = new Marcador ( me.getTitle());
                        i.putExtra("marcador", m);
                        startActivityForResult(i, 11);
                    }

                }

        );



    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager manager = (LocationManager)getSystemService(LOCATION_SERVICE);

        //UBICAR MARCADOR EN POSICION INCIAL
        setInitialPos( manager.getLastKnownLocation(LocationManager.GPS_PROVIDER) );

        //PROVIDER DE NETWORK
        this.networkProvider = new LocationProvider();
        networkProvider.setListener(this);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500,2, networkProvider);

        //PROVIDER DE GPS
        this.gpsProvider = new LocationProvider();
        gpsProvider.setListener(this);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500,2, gpsProvider);



    }

    //Metodo para ubicar el marcador en la posición inicial
    public void setInitialPos(Location lastKnownLocation){
        LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
        me = mMap.addMarker(new MarkerOptions().position(latLng));
    }

    //En este metodo recibimos los locations de ambos providers
    @Override
    public void OnLocationReceived(Location location) {
        if(location.getAccuracy() <= minAccuracy){

            minAccuracy = location.getAccuracy();
            text.setText("Accuracy: "+location.getAccuracy());
            act.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(act.getPosition(), 18));

        }
    }







    @Override
    public void onMapClick(LatLng latLng) {

        List<Address> dir;

        try {
            dir= geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // 1 representa la cantidad de resultados a obtener
            String address = dir.get(0).getAddressLine(0);

            if(me != null){
                me.remove();
            }
            me = mMap.addMarker(new MarkerOptions() .position( new LatLng(
                    latLng.latitude, latLng.longitude)).title(address));

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

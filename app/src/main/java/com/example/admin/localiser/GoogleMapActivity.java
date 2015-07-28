package com.example.admin.localiser;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.os.SystemClock.sleep;


public class GoogleMapActivity extends FragmentActivity implements GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLongClickListener,
        LocationListener {
    private GoogleMap googleMap;
    private final Map<String, Marker> markers = new HashMap<String, Marker>();
    private final Map<String, Circle> circles = new HashMap<String, Circle>();
    List<Address> addresses;
    EditText editText;
    String name;
    TextView dl;
    TextView szer;
    TextView dostawca;
    String najlepszyDostawca;
    LocationManager lm;
    Criteria kr;
    Location loc;
    Marker marker2;

    public void refresh(){
        najlepszyDostawca = lm.getBestProvider(kr, true);
        loc = lm.getLastKnownLocation(najlepszyDostawca);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        dl = (TextView) findViewById(R.id.textView4);
        szer = (TextView) findViewById(R.id.textView3);
        dostawca = (TextView) findViewById(R.id.textView3);
        editText = (EditText) findViewById(R.id.editText2);
        kr = new Criteria();
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean networkLocationEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        final WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        checkNetworkConnection(wifi);

    }

    public void checkNetworkConnection(WifiManager wifi){
        if(wifi.isWifiEnabled()){
            ifNetworkEnable();
        }
        else if(!wifi.isWifiEnabled()) {
            ifNetworkDisable();
        }
    }

    public void ifNetworkEnable(){
        refresh();
        lm.requestLocationUpdates(najlepszyDostawca, 1000, 1, this);
        szer.setText("Current latitude: " + loc.getLatitude());
        dl.setText("Current longitude: " + loc.getLongitude());

        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        googleMap = fm.getMap();
        setup();
    }

    public void ifNetworkDisable(){
        final WifiManager wifi = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 0);
                        if(wifi.isWifiEnabled()){
                            ifNetworkEnable();
                        }
                        else if(!wifi.isWifiEnabled()){
                            ifNetworkDisable();
                        }
                        else
                            break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure to enable wifi?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).setTitle("Disabled WiFi").show();
    }

    public void setup(){
        //display zoom map
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 14.0f));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        // Enabling MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMapClickListener(this);
        googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onLocationChanged(Location location) {
        refresh();
        Toast.makeText(this, "Location changed: Lat: " + loc.getLatitude() + " Lng: "
                        + loc.getLongitude(), Toast.LENGTH_LONG).show();
        dl.setText("Current longitude: " + loc.getLongitude());
        szer.setText("Current latitude: " + loc.getLatitude());
       // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 10);
       // googleMap.animateCamera(cameraUpdate);
       // lm.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapClick(LatLng point) {
        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String add = "";
        try {
            List<Address> addresses = geoCoder.getFromLocation(point.latitude, point.longitude,1);

            if (addresses.size() > 0)
            {
                for (int i=0; i<addresses.get(0).getMaxAddressLineIndex(); i++)
                    add += addresses.get(0).getAddressLine(i) + "\n";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        marker2 = googleMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title(add)
                        .snippet("Address")
        );

        marker2.showInfoWindow();
        //add circle
        final Circle circle2;
        CircleOptions circle = new CircleOptions();
        circle.center(point).fillColor(Color.argb(100,204,255,204)).strokeColor(Color.rgb(0,194,78)).strokeWidth(5).radius(40);
        circle2 = googleMap.addCircle(circle);
        markers.put(name, marker2);
        circles.put(name, circle2);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                if (markers.containsKey(name) && circles.containsKey(name)) {
                    //      Object catchName = markers.get(name);
                    //      Object catchName2 = circles.get(name);
                    if (marker != null && circle2 != null) {
                        marker.remove();
                        markers.remove(name);
                        circle2.remove();
                        circles.remove(name);
                    }
                }

            }
        });
    }

    @Override
    public void onMapLongClick(LatLng point) {
        googleMap.clear();

    }

    public void searchLocation(View view) {
        String result = "";
        String add = "";
        String locationStr = editText.getText().toString();
        try {
            Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
            addresses = geoCoder.getFromLocationName(locationStr, 1);
            for (Address address : addresses) {
                for (int i = 0, j = address.getMaxAddressLineIndex(); i <= j; i++) {
                    result += address.getAddressLine(i) + "\n";
                }

                geoCoder.getFromLocation(address.getLatitude(), address.getLongitude(),1);
                LatLng position = new LatLng(address.getLatitude(), address.getLongitude());
                CameraUpdate cameraPosition = CameraUpdateFactory.newLatLng(position);
                googleMap.animateCamera(cameraPosition);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

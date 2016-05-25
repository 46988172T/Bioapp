package com.goleogo.bioapp;

import android.location.Criteria;
import android.location.LocationListener;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    static MapView mapView = null;
    public static Location location;
    double latitude, longitude;
    Marker marker;

    public static Icon iconAudio;
    public static Icon iconText;
    public static Icon iconVideo;
    public static Icon iconPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabLocation = (FloatingActionButton) findViewById(R.id.fabLocation);
        fabLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Hacia tu posición", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                myPosition();
            }
        });

        FloatingActionButton fabAdd = (FloatingActionButton) findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Añade un dato interesante!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*Intent i = new Intent(getBaseContext(), AddInfo.class);
                startActivity(i);*/
                showPopupMenu(view, R.menu.add_content); //ver abajo.
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /** Creamos un mapview con algunas propiedades */
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.setAccessToken("pk.eyJ1IjoibGVvc3NzIiwiYSI6ImNpbDE1ejlnbzAwOW53MW00cDhuZnZpNmoifQ.AZeOgG-6oR8zDNeOKDzXXA");
        mapView.setStyleUrl(Style.MAPBOX_STREETS);
        createIcons();
        myPosition();
        getNotes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.cuenta) {
            Intent i = new Intent(getBaseContext(), SocialActivity.class);
            startActivity(i);
        } else if (id == R.id.social) {

        } else if (id == R.id.lista) {
            Intent i = new Intent(getBaseContext(), ListActivity.class);
            startActivity(i);
        } else if (id == R.id.salir) {
            Intent i = new Intent(getBaseContext(), FacebookLogout.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //creamos un menu popup para elegir el tipo de dato que vamos a colgar
    private void showPopupMenu(View view, int menu) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.getMenuInflater().inflate(menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MainActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();
                //depende de lo que seleccionemos nos lleva a un intent diferente
                if (item.getTitle().equals("Vídeo")) {
                    Intent videoIntent = new Intent(getBaseContext(), VideoActivity.class);
                    startActivity(videoIntent);
                } else if (item.getTitle().equals("Foto")) {
                    Intent photoIntent = new Intent(getBaseContext(), PhotoActivity.class);
                    startActivity(photoIntent);
                } else if (item.getTitle().equals("Texto")) {
                    Intent textIntent = new Intent(getBaseContext(), TextActivity.class);
                    startActivity(textIntent);
                } else if (item.getTitle().equals("Audio")) {
                    Intent audioIntent = new Intent(getBaseContext(), AudioActivity.class);
                    startActivity(audioIntent);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    public void openMic() {
        final int ACTIVITY_RECORD_SOUND = 1;
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        startActivityForResult(intent, ACTIVITY_RECORD_SOUND);
    }

    public void getNotes() {

        Firebase ref = new Firebase("https://bioappleo.firebaseio.com/notes/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    Note noteSnapshot = postSnapshot.getValue(Note.class);
                    addNotesToMap(noteSnapshot);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void myPosition() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 20, this);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        longitude = location.getLongitude();
        latitude = location.getLatitude();

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))     // Poblenou
                .zoom(20)                                    // zoom
                .build();

        mapView.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mIconDrawable = ContextCompat.getDrawable(this, R.drawable.male);
        Icon yo = mIconFactory.fromDrawable(mIconDrawable);

        mapView.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(Login.user.getName())
                .snippet("Eres tú")
                .icon(yo));
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub
        this.location = location;

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub

    }

    public void addNotesToMap(final Note noteSnapshot){

        if(noteSnapshot.getType().equals("audio")){

            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(noteSnapshot.getLatitude(), noteSnapshot.getLongitude()))
                    .title(noteSnapshot.getTitle())
                    .snippet(noteSnapshot.getDescription())
                    .icon(iconAudio));

        } else if(noteSnapshot.getType().equals("texto")){
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(noteSnapshot.getLatitude(), noteSnapshot.getLongitude()))
                    .title(noteSnapshot.getTitle())
                    .snippet(noteSnapshot.getDescription())
                    .icon(iconText));
        } else if(noteSnapshot.getType().equals("video")){
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(noteSnapshot.getLatitude(), noteSnapshot.getLongitude()))
                    .title(noteSnapshot.getTitle())
                    .snippet(noteSnapshot.getDescription())
                    .icon(iconVideo));
        } else if(noteSnapshot.getType().equals("foto")){
            mapView.addMarker(new MarkerOptions()
                    .position(new LatLng(noteSnapshot.getLatitude(), noteSnapshot.getLongitude()))
                    .title(noteSnapshot.getTitle())
                    .snippet(noteSnapshot.getDescription())
                    .icon(iconPhoto));
        }
    }

    public void createIcons(){
        IconFactory mIconFactory = IconFactory.getInstance(this);
        Drawable mIconDrawable = ContextCompat.getDrawable(this, R.drawable.audio);
        iconAudio = mIconFactory.fromDrawable(mIconDrawable);

        IconFactory mIconFactory2 = IconFactory.getInstance(this);
        Drawable mIconDrawable2 = ContextCompat.getDrawable(this, R.drawable.text);
        iconText = mIconFactory2.fromDrawable(mIconDrawable2);

        IconFactory mIconFactory3 = IconFactory.getInstance(this);
        Drawable mIconDrawable3 = ContextCompat.getDrawable(this, R.drawable.video);
        iconVideo= mIconFactory3.fromDrawable(mIconDrawable3);

        IconFactory mIconFactory4 = IconFactory.getInstance(this);
        Drawable mIconDrawable4 = ContextCompat.getDrawable(this, R.drawable.photo);
        iconPhoto = mIconFactory4.fromDrawable(mIconDrawable4);

    }

}

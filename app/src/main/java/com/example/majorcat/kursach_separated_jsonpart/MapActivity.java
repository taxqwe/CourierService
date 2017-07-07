package com.example.majorcat.kursach_separated_jsonpart;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.routing.MapQuestRoadManager;
import org.osmdroid.bonuspack.routing.Road;
import org.osmdroid.bonuspack.routing.RoadManager;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.R.attr.bitmap;

public class MapActivity extends AppCompatActivity {

    MyTask mt;

    MapView map;
    IMapController mapController;

    ImageView centerOnMe;
    ImageView centerOffice;
    ImageView centerOnDelivery;

    GeoPoint officePoint;
    GeoPoint deliveryAddressPoint;

    Marker officeMarker;
    Marker deliveryAddressMarker;

    MyLocationNewOverlay myLocationOverlay;
    Polyline roadOverlay;

    private ProgressBar spinner;
    BottomNavigationView bottomNavigationView;

    Intent intentMap;
    String senderPhone;
    String recipientPhone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Configuration.getInstance().load(getApplicationContext(), PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_map);
        if ( ActivityCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {

            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },
                    1);
        }

        intentMap = getIntent();

        bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        spinner = (ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        mt = new MyTask();
        mt.execute();

    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            map = (MapView) findViewById(R.id.map);
            map.setVisibility(View.GONE);
            map.setTileSource(TileSourceFactory.MAPNIK);
            //map.setBuiltInZoomControls(true);
            map.setMultiTouchControls(true);
            mapController = map.getController();
            myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(MapActivity.this), map);
            myLocationOverlay.enableMyLocation();
            Bitmap myLocationDrawable = BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.mylocationmarker);
            myLocationOverlay.setPersonIcon(myLocationDrawable);
            myLocationOverlay.setDrawAccuracyEnabled(true);
            myLocationOverlay.runOnFirstFix( new Runnable() {
                @Override
                public void run() {
                    mapController.animateTo(myLocationOverlay.getMyLocation());
                }
            }
            );
            map.getOverlays().add(myLocationOverlay);


        }

        @Override
        protected Void doInBackground(Void... params) {

            officePoint = geopointFromAdress("Москва, Галушкина 11");
            deliveryAddressPoint = geopointFromAdress(intentMap.getStringExtra("address"));
            senderPhone = intentMap.getStringExtra("senderphone");
            recipientPhone = intentMap.getStringExtra("recipientphone");

            RoadManager roadManager = new MapQuestRoadManager("MnSAn6Q0ye9CxoGPDQGVLbDo5W5KL9qX");
            roadManager.addRequestOption("routeType=fastest");

            ArrayList<GeoPoint> waypoints = new ArrayList<GeoPoint>(); //точки маршрута
            waypoints.add(officePoint);
            waypoints.add(deliveryAddressPoint);
            Road road = roadManager.getRoad(waypoints); //строит дорогу по всем точкам
            roadOverlay = RoadManager.buildRoadOverlay(road);
            roadOverlay.setColor(Color.RED);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            map.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.GONE);

            mapController.setZoom(15);
            mapController.setCenter(officePoint);

            officeMarker = new Marker(map);
            officeMarker.setIcon(getResources().getDrawable(R.drawable.officemarker));
            officeMarker.setTitle("Office address");
            officeMarker.setPosition(officePoint);
            officeMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            deliveryAddressMarker = new Marker(map);
            deliveryAddressMarker.setIcon(getResources().getDrawable(R.drawable.deliverymarker));
            deliveryAddressMarker.setTitle("Delivery address");
            deliveryAddressMarker.setPosition(deliveryAddressPoint);
            deliveryAddressMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            map.getOverlays().add(officeMarker);
            map.getOverlays().add(deliveryAddressMarker);
            map.getOverlays().add(roadOverlay);

            centerOnMe = (ImageView) findViewById(R.id.centerOnMe);
            centerOnMe.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.mylocationnew));
            centerOnMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapController.animateTo(myLocationOverlay.getMyLocation());
                    Toast.makeText(MapActivity.this, "Центрировать на мне", Toast.LENGTH_SHORT).show();
                }
            });

            centerOffice = (ImageView) findViewById(R.id.centerOnOffice);
            centerOffice.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.officemarker));
            centerOffice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapController.animateTo(officePoint);
                    Toast.makeText(MapActivity.this, "Центрировать на офисе", Toast.LENGTH_SHORT).show();
                }
            });


            centerOnDelivery = (ImageView) findViewById(R.id.centerOnDelivery);
            Bitmap src = BitmapFactory.decodeResource(getResources(), R.drawable.deliverymarker);
            RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(getResources(),src);
            //drawable.setCircular(true);

            centerOnDelivery.setImageDrawable(drawable);

            centerOnDelivery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapController.animateTo(deliveryAddressPoint);
                    Toast.makeText(MapActivity.this, "Центрировать на цели", Toast.LENGTH_SHORT).show();
                }
            });

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_delivered:
                                     new MaterialDialog.Builder(MapActivity.this)
                                            .content("Отметить заказ как выполненный?")
                                            .positiveText("Да")
                                            .negativeText("Нет")
                                            .show();
                                    break;
                                case R.id.action_call:
                                    new MaterialDialog.Builder(MapActivity.this)
                                            .items(new String[] {"Отправителю", "Получателю", "В офис"})
                                            .itemsCallback(new MaterialDialog.ListCallback() {
                                                @Override
                                                public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                                    switch (which){
                                                        case 0:
                                                            callIntent.setData(Uri.parse("tel:" + senderPhone));
                                                            break;
                                                        case 1:
                                                            callIntent.setData(Uri.parse("tel:" + recipientPhone));
                                                            break;
                                                        case 2:
                                                            callIntent.setData(Uri.parse("tel:2"));
                                                            break;
                                                    }

                                                    startActivity(callIntent);
                                                }
                                            })
                                            .show();
                                    break;

                            }
                            return true;
                        }
                    });
        }
    }

    private GeoPoint geopointFromAdress(String locationName){
        Geocoder geoCoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        double latitude = 0;
        double longitude = 0;
        try {
            List<Address> address = geoCoder.getFromLocationName(locationName, 1);
            latitude = address.get(0).getLatitude();
            longitude = address.get(0).getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new GeoPoint(latitude, longitude);
    }


}

package com.mob.mobapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.mob.mobapp.R;
import com.mob.mobapp.pojos.Center;
import com.mob.mobapp.presenters.CenterViewPresenter;
import com.mob.mobapp.utils.Permissions;
import com.mob.mobapp.utils.SystemWorker;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.runtime.image.ImageProvider;

import java.util.ArrayList;


public class MapViewActivity extends AppCompatActivity implements ScreenView {

    private String uName;
    private String uPhone;
    private MapView yaMapView;
    private Map yaMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Point point = null;
    private UserLocationLayer userLocationLayer;
    private MapObjectCollection pointCollection;
    private Point currentPosition;
    private Double placeLon = null;
    private Double placeLat = null;
    private String pointDescStr = null;
    private Integer place_cId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            uName = extras.getString("userName");
            uPhone = extras.getString("userPhone");
            if (extras.containsKey("lon") &&
                    extras.containsKey("lat") &&
                    extras.containsKey("pointDescription") &&
                    extras.containsKey("cId")) {
                placeLon = extras.getDouble("lon");
                placeLat = extras.getDouble("lat");
                pointDescStr = extras.getString("pointDescription");
                place_cId = extras.getInt("cId");
            }
        }

        setContentView(R.layout.activity_map_view);
        Window window = getWindow();
        window.setStatusBarColor(getColor(R.color.white));
        SystemWorker.getInstance().changeStatusBarContrastStyle(window, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        yaMapView = findViewById(R.id.yaMapView);
        yaMap = yaMapView.getMapWindow().getMap();
        yaMap.setMapStyle("sd");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Permissions.verifyLocationPermissions(this);
            //  return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    currentPosition = new Point(location.getLatitude(), location.getLongitude());
                    CameraPosition cameraPosition = new CameraPosition();
                    yaMap.move(new CameraPosition(currentPosition, 15.0f, 0.0f, 40.0f),
                            new Animation(Animation.Type.SMOOTH, 2),
                            null);

                    MapKit mapKit = MapKitFactory.getInstance();
                    mapKit.resetLocationManagerToDefault();
                    userLocationLayer = mapKit.createUserLocationLayer(yaMapView.getMapWindow());
                    userLocationLayer.setVisible(true);
                    userLocationLayer.setHeadingEnabled(true);
                }
            }
        });

        CenterViewPresenter mapViewPresenter = new CenterViewPresenter(this);
        mapViewPresenter.setUserName(uName);
        mapViewPresenter.setUserPhone(uPhone);
        mapViewPresenter.loadData();
    }

    private void addPlaceMark(Point point, String description, Boolean accentMark) {
        PlacemarkMapObject placemark = pointCollection.addPlacemark(point);
        placemark.setOpacity(0.7f);
        if (!accentMark) {
            placemark.setIcon(ImageProvider.fromBitmap(SystemWorker.drawableToBitmap(getDrawable(R.drawable.placemarkicon))));
        } else {
            placemark.setIcon(ImageProvider.fromBitmap(SystemWorker.drawableToBitmap(getDrawable(R.drawable.placemark_accent))), new IconStyle().setScale(1.7F));
        }
        placemark.addTapListener(new MapObjectTapListener() {
            @Override
            public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.constraintMain),
                        description,
                        8000);

                snackbar.setAction("Подробнее", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open description of point
                        // snackbar.dismiss();
                    }
                });
                snackbar.show();
                //  Toast.makeText(MapViewActivity.this, description, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapKitFactory.getInstance().onStart();
        yaMapView.onStart();
    }

    @Override
    protected void onStop() {
        yaMapView.onStop();
        MapKitFactory.getInstance().onStop();
        super.onStop();
    }

    @Override
    public void showData(Object object) {
        ArrayList<Center> centers = (ArrayList<Center>) object;
        pointCollection = yaMap.getMapObjects().addCollection();

        for (Center c : centers) {
            if (c.getcId() != place_cId)
                addPlaceMark(new Point(c.getLat(), c.getLon()), c.getAddress() + " " + c.getHours(), false);
        }

        // add place mark if user go from centers list
        if (placeLat != null && placeLon != null && pointDescStr != null)
            addPlaceMark(new Point(placeLat, placeLon), pointDescStr, true);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
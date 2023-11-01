package com.mob.mobapp.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mob.mobapp.R;
import com.mob.mobapp.utils.Permissions;
import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.Map;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.MapType;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;


public class MapViewActivity extends AppCompatActivity {


    private MapView yaMapView;
    private Map yaMap;
    private FusedLocationProviderClient fusedLocationClient;

    private Point point = null;

    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map_view);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        yaMapView = findViewById(R.id.yaMapView);
        yaMap = yaMapView.getMapWindow().getMap();
     //   yaMap.setMapType(MapType.HYBRID);
        yaMap.setMapStyle("sd");



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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


        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            point = new Point(location.getLatitude(), location.getLongitude());
                            CameraPosition cameraPosition = new CameraPosition();
                            yaMap.move(new CameraPosition(point, 16.0f, 150.0f, 40.0f),
                                    new Animation(Animation.Type.SMOOTH, 2),
                                    null);


                            MapKit mapKit = MapKitFactory.getInstance();
                            mapKit.resetLocationManagerToDefault();
                            userLocationLayer = mapKit.createUserLocationLayer(yaMapView.getMapWindow());
                            userLocationLayer.setVisible(true);
                            userLocationLayer.setHeadingEnabled(true);

                            userLocationLayer.setObjectListener(new UserLocationObjectListener() {
                                @Override
                                public void onObjectAdded(@NonNull UserLocationView userLocationView) {
                                    userLocationLayer.setAnchor(
                                            new PointF((float)(yaMapView.getWidth() * 0.5), (float)(yaMapView.getHeight() * 0.5)),
                                            new PointF((float)(yaMapView.getWidth() * 0.5), (float)(yaMapView.getHeight() * 0.83)));

                                    userLocationView.getArrow().setIcon(ImageProvider.fromResource(
                                            getApplicationContext(), R.drawable.my_location_24));

                                    CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();

                                    pinIcon.setIcon(
                                            "icon",
                                            ImageProvider.fromResource(getApplicationContext(), R.drawable.my_location_24),
                                            new IconStyle().setAnchor(new PointF(0f, 0f))
                                                    .setRotationType(RotationType.ROTATE)
                                                    .setZIndex(0f)
                                                    .setScale(1f)
                                    );

                                    pinIcon.setIcon(
                                            "pin",
                                            ImageProvider.fromResource(getApplicationContext(), R.drawable.my_location_24),
                                            new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                                                    .setRotationType(RotationType.ROTATE)
                                                    .setZIndex(1f)
                                                    .setScale(0.5f)
                                    );

                                    userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
                                }

                                @Override
                                public void onObjectRemoved(@NonNull UserLocationView userLocationView) {

                                }

                                @Override
                                public void onObjectUpdated(@NonNull UserLocationView userLocationView, @NonNull ObjectEvent objectEvent) {

                                }
                            });



//                            ImageProvider resourceBackedImage = ImageProvider.fromResource(getApplicationContext(),
//                                    R.drawable.my_location_24);
//                            PlacemarkMapObject placemark = yaMap.getMapObjects().addPlacemark(
//                                    new Point(location.getLatitude(), location.getLongitude()), resourceBackedImage);
//                            placemark.addTapListener(new MapObjectTapListener() {
//                                @Override
//                                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
//                                    return false;
//                                }
//                            });

                        }
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


}
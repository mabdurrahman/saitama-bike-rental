/*
 * Copyright (c) Mahmoud Abdurrahman 2017. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mabdurrahman.crossover.exercise.ui.places;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.canelmas.let.AskPermission;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mabdurrahman.crossover.exercise.MobileApplication;
import com.mabdurrahman.crossover.exercise.R;
import com.mabdurrahman.crossover.exercise.adapter.MapInfoWindowAdapter;
import com.mabdurrahman.crossover.exercise.core.data.network.model.Place;
import com.mabdurrahman.crossover.exercise.core.ui.places.container.PlacesContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.map.PlacesMapContract;
import com.mabdurrahman.crossover.exercise.core.ui.places.map.PlacesMapPresenter;
import com.mabdurrahman.crossover.exercise.ui.base.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import icepick.State;

/**
 * Created by Mahmoud Abdurrahman (ma.abdurrahman@gmail.com) on 1/18/17.
 */
public class PlacesMapFragment extends BaseFragment implements PlacesMapContract.View, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String ARG_PLACES = "ARG_PLACES";

    /**
     * The desired interval for Location Updates. Inexact. Updates may be more or less frequent.
     */
    private static final long LOCATION_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active Location Updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_LOCATION_INTERVAL_IN_MILLISECONDS = LOCATION_INTERVAL_IN_MILLISECONDS / 2;

    @State
    protected ArrayList<Place> places = new ArrayList<>();

    @State
    protected boolean mapReady = false;
    @State
    protected LocationRequest locationRequest;
    @State
    protected boolean requestingLocationUpdates = true;
    @State
    protected LatLng locationLatLng;
    @State
    protected LatLngBounds placesBounds;

    protected Map<Marker, Place> placeMarkerMap = new HashMap<>();

    private WeakReference<PlacesContract.View> placesView;
    private PlacesMapPresenter presenter;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;

    private GoogleApiClient googleApiClient;

    public static PlacesMapFragment newInstance(ArrayList<Place> places) {
        PlacesMapFragment fragment = new PlacesMapFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PLACES, places);

        fragment.setArguments(args);
        return fragment;
    }

    public PlacesMapFragment() {}

    @Override
    protected int getContentResource() {
        return R.layout.fragment_places_map;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof PlacesContract.View) {
            placesView = new WeakReference<>((PlacesContract.View) context);
        }
    }

    @Override
    protected void onLaunch() {
        super.onLaunch();

        places = getArguments().getParcelableArrayList(ARG_PLACES);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new PlacesMapPresenter();
        presenter.attachView(this);

        googleApiClient = new GoogleApiClient.Builder(MobileApplication.getInstance())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create Location Request
        createLocationRequest();
    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (googleApiClient.isConnected()) {
            // Start listening to Location Updates if required
            startLocationUpdates(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient.isConnected()) {
            // Force stopping Location Updates to save battery, and don't disconnect the GoogleApiClient object
            // Moreover, we don't touch requestingLocationUpdates flag state for upcoming onResume() call
            stopLocationUpdates(true);
        }
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();

        googleApiClient.unregisterConnectionCallbacks(this);
        googleApiClient.unregisterConnectionFailedListener(this);

        super.onDestroy();
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();

        // Sets the desired interval for active Location Updates
        locationRequest.setInterval(LOCATION_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active Location Updates
        locationRequest.setFastestInterval(FASTEST_LOCATION_INTERVAL_IN_MILLISECONDS);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Requests Location Updates from the FusedLocationApi.
     */
    @AskPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    private void startLocationUpdates(boolean force) {
        // If no force starting and no request for automatically starting the
        // service we suppress the call
        if (!force && !requestingLocationUpdates) return;

        requestingLocationUpdates = true;

        // Request Location Updates if Location Permission granted.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        adjustMapMarkers(false);
    }

    /**
     * Removes Location Updates from the FusedLocationApi.
     */
    private void stopLocationUpdates(boolean force) {
        // If no force stopping and we don't already listening to Location Updates
        // we suppress the call
        if (!force && !requestingLocationUpdates) return;

        // If nor force stopping, we request not to start the service
        // automatically on the future
        if (!force) {
            requestingLocationUpdates = false;
        }
        // It is a good practice to remove Location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent Location Updates
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    @Override
    public void onMapReady(GoogleMap gMap) {
        googleMap = gMap;

        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                // Force start listening to Location Updates
                startLocationUpdates(true);

                return false;
            }
        });
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setInfoWindowAdapter(new MapInfoWindowAdapter(getActivity()));

        // Enable My Location layer if Location Permission granted.
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        }

        mapReady = true;

        // Cannot zoom to bounds until the map has a size.
        final View mapView = getChildFragmentManager().findFragmentById(R.id.map).getView();
        if (mapView != null && mapView.getViewTreeObserver() != null && mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation") // We use the new method when supported
                @SuppressLint("NewApi") // We check which build version we are using.
                @Override
                public void onGlobalLayout() {

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                    // Adjust active Marker, force center the map over the Marker
                    adjustMapMarkers(true);
                }
            });
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (locationLatLng == null) {
            // Get Last Known Location if Location Permission granted.
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

                if (lastKnownLocation != null) {
                    // Set active Location to Last Known Location
                    locationLatLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                    // Adjust active Marker, force center the map over the Marker
                    adjustMapMarkers(true);
                }
            }
        }

        // Start listening to Location Updates if required
        startLocationUpdates(false);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        googleApiClient.connect();
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());

        Toast.makeText(getActivity(), R.string.error_check_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        if (!requestingLocationUpdates) return;

        // Set active Location to new user Location
        locationLatLng = new LatLng(newLocation.getLatitude(), newLocation.getLongitude());

        // Adjust active Marker, don't force centering the map over the Marker to avoid
        // annoying the user
        adjustMapMarkers(false);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (!marker.isInfoWindowShown()) {
            marker.showInfoWindow();
        } else {
            marker.hideInfoWindow();
        }

        presenter.onItemClicked(placeMarkerMap.get(marker));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 14));

        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f", marker.getPosition().latitude, marker.getPosition().longitude);
        Uri intentUri = Uri.parse(uri);
        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        intent.setPackage("com.google.android.apps.maps");

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                Intent unrestrictedIntent = new Intent(Intent.ACTION_VIEW, intentUri);
                startActivity(unrestrictedIntent);
            } catch (ActivityNotFoundException innerEx) {
                Toast.makeText(getActivity(), "Please install a maps application", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void adjustMapMarkers(boolean forceAnimateCamera) {
        if (googleMap == null) return;

        if (placeMarkerMap.size() == 0) {
            forceAnimateCamera = true;

            for (Place place : places) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(place.getName());
                markerOptions.snippet(place.getLocation().toString());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher));
                markerOptions.anchor(0.5f, 1.0f);
                markerOptions.position(new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude()));

                Marker marker = googleMap.addMarker(markerOptions);

                placeMarkerMap.put(marker, place);
            }
        }

        if (forceAnimateCamera) {
            if (locationLatLng != null) {
                // First: Animate to Current Location with proper zoom level
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 14));
            }

            placesBounds = generatePlacesBounds();

            // Second: Animate to show all Places and optionally the Current Location if desired
            if (placesBounds != null) {

                try {
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(placesBounds, 0));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adjustMapMarkers(true);
                        }
                    }, 500);
                }
            }
        }
    }

    private LatLngBounds generatePlacesBounds() {
        LatLngBounds.Builder placesBoundsBuilder = new LatLngBounds.Builder();

        /* Uncomment code below to include user's current location into the generated boundaries
         *
         * if (locationLatLng != null) {
         *   placesBoundsBuilder.include(new LatLng(locationLatLng.latitude, locationLatLng.longitude));
         * }
         */

        if (places != null) {
            for (Place place : places) {
                placesBoundsBuilder.include(new LatLng(place.getLocation().getLatitude(), place.getLocation().getLongitude()));
            }
        }

        placesBounds = placesBoundsBuilder.build();

        return placesBounds;
    }

    @Override
    public void showPikeRentalConfirmation(Place place) {
        if (placesView != null && placesView.get() != null) {
            placesView.get().showPikeRentalConfirmation(place);
        }
    }

}

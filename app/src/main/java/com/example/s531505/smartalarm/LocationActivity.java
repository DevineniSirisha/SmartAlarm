package com.example.s531505.smartalarm;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.s531505.smartalarm.dbhelper.DatabaseHelper;
import com.example.s531505.smartalarm.dbhelper.model.SmartAlarm;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int PLACE_PICKER_REQUEST = 1;
    EditText edtAlarmName, edtAlarmNotes, edtRadiusInMtr, edtDestination;
    ImageView imgDestination;
    Button btnSave, btnReset, btnCancel;
    private DatabaseHelper db;
    SmartAlarm smartAlarm;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final long UPDATE_INTERVAL = 1 * 1000;
    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;
    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    double lat = 0, lng = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        db = new DatabaseHelper(this);

        initializeView();

        recreateView();
        buildGoogleApiClient();
    }

    public void checkPermissionLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(getResources().getString(R.string.open_location_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setCancelable(false);
            dialog.show();
        }
    }

    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i("TAG", "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_main),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.okay, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(LocationActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i("TAG", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(LocationActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i("TAG", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("TAG", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted. Kick off the process of building and connecting
                // GoogleApiClient.
                buildGoogleApiClient();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                Snackbar.make(
                        findViewById(R.id.activity_main),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }


    private void buildGoogleApiClient() {
        if (mGoogleApiClient != null) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();

    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Sets the maximum time when batched location updates are delivered. Updates may be
        // delivered sooner than this interval.
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i("TAG", "GoogleApiClient connected");
        requestLocationUpdates();
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationUpdatesBroadcastReceiver.class);
        intent.setAction(LocationUpdatesBroadcastReceiver.ACTION_PROCESS_UPDATES);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onConnectionSuspended(int i) {
        final String text = "Connection suspended";
        Log.w("TAG", text + ": Error code: " + i);
        showSnackbar("Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        final String text = "Exception while connecting to Google Play services";
        Log.w("TAG", text + ": " + connectionResult.getErrorMessage());
        showSnackbar(text);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(LocationResultHelper.KEY_LOCATION_UPDATES_RESULT)) {
//            mLocationUpdatesResultView.setText(LocationResultHelper.getSavedLocationResult(this));
            Log.e(" my alarm ", LocationResultHelper.getSavedLocationResult(this));
        } else if (s.equals(LocationRequestHelper.KEY_LOCATION_UPDATES_REQUESTED)) {
//            updateButtonsState(LocationRequestHelper.getRequesting(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        Log.e(" my alarm ", LocationResultHelper.getSavedLocationResult(this));
        if (!checkPermissions()) {
            requestPermissions();
        }

        checkPermissionLocation();
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.activity_main);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Handles the Request Updates button and requests start of location updates.
     */
    public void requestLocationUpdates() {
        try {
            Log.i("TAG", "Starting location updates");
            LocationRequestHelper.setRequesting(this, true);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            LocationRequestHelper.setRequesting(this, false);
            e.printStackTrace();
        }
    }

    /**
     * Handles the Remove Updates button, and requests removal of location updates.
     */
    public void removeLocationUpdates() {
        Log.i("TAG", "Removing location updates");
        LocationRequestHelper.setRequesting(this, false);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
                getPendingIntent());
    }

    public void resetView() {
        if (smartAlarm != null) db.deleteSmartAlarm(smartAlarm);
        smartAlarm = null;
        removeLocationUpdates();
        populateView();
    }

    public void recreateView() {
        smartAlarm = db.getFirst();
        populateView();
    }

    public void resetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_reset_message)
                .setTitle(R.string.dialog_reset_title);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                resetView();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void cancelDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_cancel_message)
                .setTitle(R.string.dialog_cancel_title);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                recreateView();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.dialog_save_message)
                .setTitle(R.string.app_name);
        builder.setNegativeButton(R.string.okay, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void initializeView() {
        edtAlarmName = findViewById(R.id.edtAlarmName);
        edtAlarmNotes = findViewById(R.id.edtAlarmNotes);
        edtRadiusInMtr = findViewById(R.id.edtRadiusInMtr);
        edtDestination = findViewById(R.id.edtDestination);
        imgDestination = findViewById(R.id.imgDestination);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        btnCancel = findViewById(R.id.btnCancel);

        btnSave.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        edtDestination.setOnClickListener(this);
    }

    public void populateView() {
        if (smartAlarm != null) {
            edtDestination.setText(smartAlarm.getDestination());
            edtRadiusInMtr.setText(smartAlarm.getRadius() + "");
            edtAlarmNotes.setText(smartAlarm.getNotes());
            edtAlarmName.setText(smartAlarm.getName());
            lat = smartAlarm.getLatitude();
            lng = smartAlarm.getLongitude();
        } else {
            edtDestination.setText("");
            edtRadiusInMtr.setText("");
            edtAlarmNotes.setText("");
            edtAlarmName.setText("");
            lat = 0;
            lng = 0;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnCancel:
                cancelDialog();
                break;
            case R.id.btnReset:
                resetDialog();
                break;
            case R.id.btnSave:
                if (checkValidation()) {
                    String name = edtAlarmName.getText().toString().trim();
                    String notes = edtAlarmNotes.getText().toString().trim();
                    String radius = edtRadiusInMtr.getText().toString().trim();
                    String destination = edtDestination.getText().toString().trim();
                    createOrUpdateAlarm(name, notes, destination, Integer.parseInt(radius), lat, lng);
                }
                break;
            case R.id.edtDestination:
                openPlacePicker();
                break;
        }
    }

    public boolean checkValidation() {
        boolean flag = true;
        String msg = "";
        String name = edtAlarmName.getText().toString().trim();
        String notes = edtAlarmNotes.getText().toString().trim();
        String radius = edtRadiusInMtr.getText().toString().trim();
        String destination = edtDestination.getText().toString().trim();

        if (name.isEmpty()) {
            flag = false;
            msg = getString(R.string.enter_name);
        } else if (notes.isEmpty()) {
            flag = false;
            msg = getString(R.string.enter_notes);
        } else if (radius.isEmpty()) {
            flag = false;
            msg = getString(R.string.enter_radius);
        } else if (destination.isEmpty()) {
            flag = false;
            msg = getString(R.string.enter_destination);
        }

        if (!flag) {
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        }
        return flag;
    }

    private void createOrUpdateAlarm(String alarmName, String alarmNotes, String destination, int radius, double lat, double lng) {
        if (smartAlarm != null) {
            smartAlarm.setName(alarmName);
            smartAlarm.setNotes(alarmNotes);
            smartAlarm.setDestination(destination);
            smartAlarm.setRadius(radius);
            smartAlarm.setLatitude(lat);
            smartAlarm.setLongitude(lng);
            db.updateSmartAlarm(smartAlarm);
        } else {
            long id = db.insertSmartAlarm(alarmName, alarmNotes, destination, radius, lat, lng);
            smartAlarm = db.getSmartAlarm(id);
        }
        populateView();
        saveDialog();
    }

    public void openPlacePicker() {
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
//            final CharSequence name = place.getName();
//            final CharSequence address = place.getAddress();
//            String attributions = (String) place.getAttributions();
//            if (attributions == null) {
//                attributions = "";
//            }
            if (smartAlarm != null) {
                smartAlarm.setDestination(place.getAddress().toString());
                smartAlarm.setLatitude(place.getLatLng().latitude);
                smartAlarm.setLongitude(place.getLatLng().longitude);
            }
            edtDestination.setText(place.getAddress().toString());
            lat = place.getLatLng().latitude;
            lng = place.getLatLng().longitude;

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}

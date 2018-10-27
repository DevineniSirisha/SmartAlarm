package com.example.s531505.smartalarm;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import static android.content.Context.LOCATION_SERVICE;

public class LatAndLong implements LocationListener {

    Context c;
    public LatAndLong(Context c) {
        super();
        this.c = c;
    }

    public Location getLocationOfUser(){
        if (ContextCompat.checkSelfPermission( c, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try{
            LocationManager lockM = (LocationManager) c.getSystemService(LOCATION_SERVICE);
            //checking whether location is enabled
            boolean isGPSEnabled = lockM.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (isGPSEnabled){
                lockM.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000,10,this);
                Location locate = lockM.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                return locate;
            }else{
                Log.e("sec","errpr");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public void onLocationChanged(Location location) {

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
}

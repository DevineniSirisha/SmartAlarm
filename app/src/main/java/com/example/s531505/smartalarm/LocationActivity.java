package com.example.s531505.smartalarm;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity implements ResetDialog.ResetDialogInterface, ConfirmDialog.ConfirmDialogInterface {
    private LocationManager locationManager;
    private LocationListener locationListner;
      double lat=0.0,lon=0.0;
      double dist=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
       // Log.d("this is sample ", "hhi");
        //ActivityCompat.requestPermissions(LocationActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              //  Log.d("Location is ", location.toString());
               // Toast.makeText(getApplicationContext(),"location is "+location.getLatitude()+","+location.getLongitude(),Toast.LENGTH_SHORT).show();
                double lat1=location.getLatitude();
                double lon1=location.getLongitude();
                checkloc(lat1,lon1);

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
        };
        if (Build.VERSION.SDK_INT < 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListner);
        } else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListner);

            }
        }

        //menu places
        final ImageButton mapbt=findViewById(R.id.SearchMap);
        mapbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popMenu=new PopupMenu(LocationActivity.this,mapbt);
                popMenu.getMenuInflater().inflate(R.menu.places_menu,popMenu.getMenu());
                popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle()=="Walmart"){
                            //40.3295107,-94.8725826
                            lat=40.38226911165174;
                            lon=-94.8645645255088;
                        }
                        else{
                            //40.3295107,-94.8725826
                            lat=40.33364220000001;
                            lon=-94.87191659999996;
                        }
                        //Toast.makeText(getApplicationContext(),""+item.getTitle(),Toast.LENGTH_LONG).show();
                        TextView tv=findViewById(R.id.DestTV);
                        tv.setText(item.getTitle());
                        return true;
                    }
                });
                popMenu.show();

            }
        });





    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                ==PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListner);

            }
        }


        }





    public void checkloc(double lat1,double lon1){
        //walmart lat and lon 40.330026,-94.872303
//        double lat1=40.330026;
//        double lon1=-94.872303;
//        double lat2=Float.parseFloat(loc.substring(0,loc.indexOf(",")));
//        double lon2=Float.parseFloat(loc.substring(loc.indexOf(",")+1,loc.length()));

       Toast.makeText(getApplicationContext(),"lat is "+lat1 +" lon is "+lon1,Toast.LENGTH_LONG).show();
        double theta = lon1 - lon;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if(dist>=500){

            //Toast.makeText(getApplicationContext(),"alarmed",Toast.LENGTH_LONG).show();
            Intent resultIntent=new Intent(this, LocationActivity.class);
            PendingIntent resultPending=PendingIntent.getActivity(this,1,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(getApplicationContext(),"channel1")
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle("ALert")
                    .setContentText("You are near to walmart")
                    .setAutoCancel(true)
                    .setContentIntent(resultPending);
            notificationBuilder.setDefaults(
                    Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
            NotificationManagerCompat notificationManager=NotificationManagerCompat.from(LocationActivity.this);
            notificationManager.notify(1,notificationBuilder.build());




        }
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    //    public void onClick(View v) {
//        LatAndLong gt = new LatAndLong(getApplicationContext());
//        Location l = gt.getLocationOfUser();
//        if( l == null){
//            Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
//        }else {
//            double lat = l.getLatitude();
//            double lon = l.getLongitude();
//            Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
//        }
//    }
    public void Savebt(View v){
        EditText et1=findViewById(R.id.AlarmNameET);
        EditText et3=findViewById(R.id.RadiusET);
        TextView tv1=findViewById(R.id.DestTV);
        //Log.d("itshdhda",et1.getText().toString());

        if(et1.getText().toString()==null||et1.getText().toString()==" "||et3.getText().toString()==null||et3.getText().toString()==" "
              ||tv1.getText().toString()=="Your destination"||tv1.getText().toString()==null ){
            Toast.makeText(getApplicationContext(),"Please enter all the details",Toast.LENGTH_LONG).show();

        }
       else{
            TextView NotesDesc = findViewById(R.id.AlarmNameET);
            String text = NotesDesc.getText().toString();
            if (!text.isEmpty()) {
                Intent intent1 = new Intent(this,StartActivity.class);
                intent1.putExtra("Notes1", text);
                startActivity(intent1);
            }

            else {

                Toast.makeText(getApplicationContext(),"Please enter all the details",Toast.LENGTH_LONG).show();
            }
        }



    }


    public void Resetbtn(View v){
        ResetDialog resetDialog = new ResetDialog();
        resetDialog.show(getSupportFragmentManager(), "reset location activity");
    }
    public void cancelbtn(View v){
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.show(getSupportFragmentManager(), "cancel btn location activity");

    }
    public void Searchbtn(View v){
        String data="https://www.google.com/maps";
        Intent searchIntent=new Intent(Intent.ACTION_VIEW);
        searchIntent.setData(Uri.parse(data));
        startActivity(searchIntent);
    }

    @Override
    public void reset() {
        EditText et1=findViewById(R.id.AlarmNameET);
        EditText et2=findViewById(R.id.AlarmDesET);
        EditText et3=findViewById(R.id.RadiusET);
        TextView et4=findViewById(R.id.DestTV);
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }

    @Override
    public void cancel() {
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
    }
}

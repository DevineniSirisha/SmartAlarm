package com.example.s531505.smartalarm;

import android.Manifest;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ActivityCompat.requestPermissions(LocationActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 123);
    }

    public void onClick(View v) {
        LatAndLong gt = new LatAndLong(getApplicationContext());
        Location l = gt.getLocationOfUser();
        if( l == null){
            Toast.makeText(getApplicationContext(),"GPS unable to get Value",Toast.LENGTH_SHORT).show();
        }else {
            double lat = l.getLatitude();
            double lon = l.getLongitude();
            Toast.makeText(getApplicationContext(),"GPS Lat = "+lat+"\n lon = "+lon,Toast.LENGTH_SHORT).show();
        }
    }
    public void Savebtn(View v){

            TextView NotesDesc = findViewById(R.id.AlarmNameET);
            String text = NotesDesc.getText().toString();
        if(!text.isEmpty()) {
            Intent intent1 = new Intent(this, AlertActivity.class);
            intent1.putExtra("Notes1", text);
            startActivity(intent1);
        }
        else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
        }
    }
    public void Resetbtn(View v){
        EditText et1=findViewById(R.id.AlarmNameET);
        EditText et2=findViewById(R.id.AlarmDesET);
        EditText et3=findViewById(R.id.RadiusET);
        EditText et4=findViewById(R.id.DestET);
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
    }
    public void cancelbtn(View v){
        Intent intent=new Intent(this,StartActivity.class);
        startActivity(intent);
    }
    public void Searchbtn(View v){
        String data="https://www.google.com/maps";
        Intent searchIntent=new Intent(Intent.ACTION_VIEW);
        searchIntent.setData(Uri.parse(data));
        startActivity(searchIntent);
    }
}

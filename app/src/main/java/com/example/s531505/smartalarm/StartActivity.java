package com.example.s531505.smartalarm;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity implements BackGrDialog.ColorCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the
//        action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public void wakeup(View v){
        Intent intent=new Intent(this,WakeupActivity.class);
        String s="Set Wake-up alarm";
        intent.putExtra("msg",s);
        startActivityForResult(intent,1);
        Toast.makeText(StartActivity.this, "You are in Wake-up Activity!",
                Toast.LENGTH_LONG).show();
    }
    public void location(View v){
        Intent intent=new Intent(this,LocationActivity.class);
        String s="Set location alarm";
        intent.putExtra("msg",s);
        startActivityForResult(intent,1);
        Toast.makeText(StartActivity.this, "You are in Location Activity!",
                Toast.LENGTH_LONG).show();
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.wake_up:
                Intent intent=new Intent(this,WakeupActivity.class);
                String s="Set Wake-up alarm";
                intent.putExtra("msg",s);
                startActivityForResult(intent,1);
                Toast.makeText(StartActivity.this, "You are in Wake-up Activity!",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.location:
                Intent init=new Intent(this,LocationActivity.class);
                String st="Set location alarm";
                init.putExtra("msg",st);
                startActivityForResult(init,1);
                Toast.makeText(StartActivity.this, "You are in Location Activity!",
                        Toast.LENGTH_LONG).show();
                return true;


        }
     return super.onOptionsItemSelected(item);
    }

public void change(View v){
    BackGrDialog colorD = new BackGrDialog();
    colorD.show(getSupportFragmentManager(),"TAG");
}
    @Override
    public void setBackgroundImage(int imageChoice) {
            ConstraintLayout bg=findViewById(R.id.bg);
            
            switch(imageChoice){
                case 0:

                    bg.setBackgroundResource(R.drawable.bg1);
                    break;
                case 1:
                    bg.setBackgroundResource(R.drawable.bgr);
                    break;
                case 2:
                    bg.setBackgroundResource(R.drawable.bg3);
                    break;
                case 3:
                    bg.setBackgroundResource(R.drawable.bg4);
                    break;
                default:
                    bg.setBackgroundResource(R.drawable.bg);
                    break;
            }
            Toast.makeText(StartActivity.this,
                    "You have changed background", Toast.LENGTH_LONG).show();
    }
}

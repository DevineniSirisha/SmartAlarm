package com.example.s531505.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AlertActivity extends AppCompatActivity  implements SensorEventListener{
TextView tv_steps;
SensorManager sensorManager;
boolean running=false;
Button stop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
stop=findViewById(R.id.stop);
        Intent wakeUp=getIntent();
        String wakeUpText=wakeUp.getStringExtra("Notes");
        TextView textView_Alert=findViewById(R.id.textView_Alert);
        textView_Alert.setText(wakeUpText);

        Intent location=getIntent();
        String locationText=location.getStringExtra("Notes1");
        TextView location_Alert=findViewById(R.id.textView_Alert);
        location_Alert.setText(locationText);
    stop.setVisibility(View.GONE);
        tv_steps=findViewById(R.id.textViewalert4);
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        running=true;
        Sensor countSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor!=null){
            sensorManager.registerListener(this,countSensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(this,"SENSOR NOT FOUND",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        running=false;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(running) {
            tv_steps.setText(String.valueOf(event.values[0]));
            int i=Integer.parseInt(tv_steps.getText().toString());
            if (i>=20){
                stop.setVisibility(View.VISIBLE);
            }
        }
        }
        public void stop(View v){
        Intent ini=new Intent(this,StartActivity.class);
        startActivity(ini);
        }
    }




package com.example.s531505.smartalarm;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AlertActivity extends AppCompatActivity  implements SensorEventListener, StepCounter{
    private TextView textView;
    private StepDetector detector;
    private SensorManager manager;
    private Sensor accelarator;
    private static final String steps = "Total Steps: ";
    private int stepCount;
    TextView tvSteps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        Intent wakeUp=getIntent();
        String wakeUpText=wakeUp.getStringExtra("Notes");
        TextView textView_Alert=findViewById(R.id.textView_Alert);
        textView_Alert.setText(wakeUpText);

//        Intent wakeUp=getIntent();
//        String wakeUpText=wakeUp.getStringExtra("Notes");
//        TextView textView_Alert=findViewById(R.id.textView_Alert);
//        textView_Alert.setText(wakeUpText);
//        Log.d("dfdtest", wakeUpText);
//        Log.d("print", textView_Alert.getText().toString());
//
//
        Intent location=getIntent();
        String locationText=location.getStringExtra("Notes1");
        TextView location_Alert=findViewById(R.id.textView_Alert);
        location_Alert.setText(locationText);
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelarator = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        detector = new StepDetector();
        detector.registerListener(this);
        tvSteps = (TextView) findViewById(R.id.textViewalert4);
        Button btnStart = (Button) findViewById(R.id.btn_start);
       Button btnStop = (Button) findViewById(R.id.btn_stop);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                stepCount = 0;
                manager.registerListener(AlertActivity.this, accelarator, SensorManager.SENSOR_DELAY_FASTEST);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.unregisterListener(AlertActivity.this);
            }
        });
if(stepCount>20){
    Intent intent=new Intent(this,StartActivity.class);
    startActivity(intent);
}
else if(stepCount==20){
    Intent intent=new Intent(this,StartActivity.class);
    startActivity(intent);
}
else{
    TextView tv4=findViewById(R.id.textViewalert4);
    tv4.setText(stepCount+"/20" );
    TextView tv=findViewById(R.id.textview_Alert2);
    tv.setText("You need to walk "+(20-stepCount)+" more to switch off the alarm" );
}
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }
    @Override
    public void step(long timeNs) {
        stepCount++;
        tvSteps.setText(stepCount);
    }

}

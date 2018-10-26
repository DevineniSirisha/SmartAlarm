package com.example.s531505.smartalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WakeupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    EditText time;
    AlarmManager alarm_manager;
    EditText notesDesc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);

        Button tbutton = (Button) findViewById(R.id.timebutton);
        tbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.DialogFragment timePicker=new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
    }

    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        time=(EditText) findViewById(R.id.setWakeUpTime);
        time.setText(i+":"+i1);
    }

public void save(View v){
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    String format = simpleDateFormat.format(new Date());
    Log.d( "Current Time: " , format);

    for(int i=1; i>0;i++) {
        if(format.equals(time.getText().toString())) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        }
    }
}
    public void cancel(View v){
        Intent intent1=new Intent(this,StartActivity.class);
        startActivity(intent1);
    }

}

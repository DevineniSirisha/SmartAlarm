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
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WakeupActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

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
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
    }

    public void onTimeSet(TimePicker timePicker, int i, int i1) {


        String status = "AM";

        if (i > 11) {

            status = "PM";
        }


        int hour_of_12_hour_format;

        if (i > 11) {


            hour_of_12_hour_format = i - 12;
        } else {
            hour_of_12_hour_format = i;
        }


        time = (EditText) findViewById(R.id.setWakeUpTime);

        time.setText(hour_of_12_hour_format + ":" + i1 + " " + status);
    }




public void save(View v){
    time = findViewById(R.id.setWakeUpTime);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
    String format = simpleDateFormat.format(new Date());
    Log.d( "Current Time: " , format);
if (time.getText().toString().isEmpty()) {
    time.setError("error");
    } else {
    final String timet = time.getText().toString();

<<<<<<< HEAD
        if(timet.equals(time.getText().toString())) {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
            TextView NotesDesc=findViewById(R.id.NotesDesc);
            String text=NotesDesc.getText().toString();
            Intent intent1=new Intent(this,AlertActivity.class);
            intent1.putExtra("Notes",text);
            startActivity(intent1);
        }
        else{
            Intent intent1=new Intent(this,StartActivity.class);
            startActivity(intent1);
        }
=======
    if (timet.equals(time.getText().toString())) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();

        Intent intent1 = new Intent(this, AlertActivity.class);
        startActivity(intent1);
    }
}
>>>>>>> 8775eb5951996568affb4560c9774ba16fc24620
}
    public void cancel(View v){
        Intent intent1=new Intent(this,StartActivity.class);
        startActivity(intent1);
    }

    public void resetFunction(View v){
        EditText wk=(EditText) findViewById(R.id.setWakeUpTime);
        wk.setText("");
        EditText sc=(EditText) findViewById(R.id.StepCount);
        sc.setText("");
        EditText nd=(EditText) findViewById(R.id.NotesDesc);
        nd.setText("");
    }



}

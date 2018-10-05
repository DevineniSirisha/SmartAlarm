package com.example.s531505.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WakeupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wakeup);
    }
public void save(View v){
      Intent intent=new Intent(this,StartActivity.class);
      startActivity(intent);

}
    public void cancel(View v){
        Intent intent1=new Intent(this,StartActivity.class);
        startActivity(intent1);
    }

}

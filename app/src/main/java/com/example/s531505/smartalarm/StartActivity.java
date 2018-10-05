package com.example.s531505.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

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
}

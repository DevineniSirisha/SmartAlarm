package com.example.s531505.smartalarm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the
//        action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.about:
                Intent intent=new Intent(this,AboutActivity.class);
                String s="Set Wake-up alarm";
                intent.putExtra("msg",s);
                startActivityForResult(intent,1);
                Toast.makeText(AboutActivity.this, "You are in About Activity!",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.contact:
                Intent init=new Intent(this,ContactActivity.class);
                String st="Set location alarm";
                init.putExtra("msg",st);
                startActivityForResult(init,1);
                Toast.makeText(AboutActivity.this, "You are in contact Activity!",
                        Toast.LENGTH_LONG).show();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

}

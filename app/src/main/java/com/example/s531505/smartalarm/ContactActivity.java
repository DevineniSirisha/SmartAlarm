package com.example.s531505.smartalarm;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
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
                Toast.makeText(ContactActivity.this, "You are in About Activity!",
                        Toast.LENGTH_LONG).show();
                return true;
            case R.id.contact:
                Intent init=new Intent(this,ContactActivity.class);
                String st="Set location alarm";
                init.putExtra("msg",st);
                startActivityForResult(init,1);
                Toast.makeText(ContactActivity.this, "You are in contact Activity!",
                        Toast.LENGTH_LONG).show();
                return true;


        }
        return super.onOptionsItemSelected(item);
    }

    public void send(View V) {

        EditText et1 = findViewById(R.id.name);
        EditText et2 = findViewById(R.id.comment);
        String name = et1.getText().toString();
        String text = et2.getText().toString();
        if (text.length() != 0 && name.length() != 0) {
            Log.i("Send email", "");

            String[] TO = {"s531505@nwmissouri.edu"};
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setData(Uri.parse("mailto:"));
            emailIntent.setType("text/plain");


            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, name);
            emailIntent.putExtra(Intent.EXTRA_TEXT, text);

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                finish();
                Log.i("Finished sending email.", "");
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ContactActivity.this,
                        "There is no email client installed.", Toast.LENGTH_SHORT).show();
            }
        }else{
            et1.setHint("Please enter name");
            et2.setHint("Please enter comment");
        }
    }
}

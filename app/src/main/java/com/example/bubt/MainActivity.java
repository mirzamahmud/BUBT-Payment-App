package com.example.bubt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // page layout add
        setContentView(R.layout.activity_main);

        // hide actionbar
        getSupportActionBar().hide();
        // change status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    // goto one activity to another activity
                    Intent intent = new Intent(MainActivity.this, StartActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        };
        // start thread
        thread.start();

    }
}
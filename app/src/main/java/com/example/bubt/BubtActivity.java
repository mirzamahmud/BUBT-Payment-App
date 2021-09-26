package com.example.bubt;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BubtActivity extends AppCompatActivity
{

    WebView bubt;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bubt);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(BubtActivity.this, R.color.colorPrimary));

        // reference widget
        bubt = (WebView) findViewById(R.id.bubt);

        // enable javascript using the webSetting object
        WebSettings webSettings = bubt.getSettings();
        webSettings.setJavaScriptEnabled(true);

        bubt.setWebViewClient(new Callback());
        bubt.loadUrl("https://www.bubt.edu.bd/");
    }

    private class Callback extends WebViewClient
    {
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event)
        {
            return false;
        }
    }

}
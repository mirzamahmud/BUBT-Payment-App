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

public class AnnexActivity extends AppCompatActivity
{

    WebView bubtAnnex;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annex);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(AnnexActivity.this, R.color.colorPrimary));

        // reference widget
        bubtAnnex = (WebView) findViewById(R.id.bubt_annex);

        // enable javascript using the webSetting object
        WebSettings webSettings = bubtAnnex.getSettings();
        webSettings.setJavaScriptEnabled(true);

        bubtAnnex.setWebViewClient(new Callback());
        bubtAnnex.loadUrl("https://annex.bubt.edu.bd/");
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
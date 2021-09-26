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


public class PaymentActivity extends AppCompatActivity
{

    WebView paymentSystem;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(ContextCompat.getColor(PaymentActivity.this, R.color.colorPrimary));

        // reference widget
        paymentSystem = (WebView) findViewById(R.id.payment_system);

        // enable javascript using the webSetting object
        WebSettings webSettings = paymentSystem.getSettings();
        webSettings.setJavaScriptEnabled(true);

        paymentSystem.setWebViewClient(new Callback());
        paymentSystem.loadUrl("https://payment.bubt.edu.bd/manual.php");

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
package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewsWebviewActivity extends AppCompatActivity {

    private WebView webView;
    private WebViewClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webview);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        client=new WebViewClient();

        webView=findViewById(R.id.newsWebview);
        webView.setWebViewClient(client);

        webView.getSettings().setJavaScriptEnabled(true);

        if(getIntent().getStringExtra("WebUrl")!=null)
        {
            webView.loadUrl(getIntent().getStringExtra("WebUrl"));
        }

    }
}
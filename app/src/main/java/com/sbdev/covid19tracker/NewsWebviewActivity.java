package com.sbdev.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class NewsWebviewActivity extends AppCompatActivity {

    private WebView webView;
    private WebViewClient client;

    private ProgressDialog progressDialog;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_webview);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        progressBar=findViewById(R.id.progressBar);

        /*progressDialog=new ProgressDialog(this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.loading_bg);
        //progressDialog.setCancelable(false);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);*/

        client=new WebViewClient()
        {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                //progressDialog.dismiss();
                progressBar.setVisibility(View.GONE);

            }

        };

        webView=findViewById(R.id.newsWebview);
        webView.setWebViewClient(client);

        webView.getSettings().setJavaScriptEnabled(true);

        if(getIntent().getStringExtra("WebUrl")!=null)
        {
            webView.loadUrl(getIntent().getStringExtra("WebUrl"));
        }

    }
}
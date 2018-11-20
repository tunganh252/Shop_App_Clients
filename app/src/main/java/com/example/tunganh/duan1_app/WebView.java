package com.example.tunganh.duan1_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;

public class WebView extends AppCompatActivity {

    private android.webkit.WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        /////////// Web View ///////////
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://www.thegioididong.com/");

        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    ////// set webview khi click back k trở về app, chỉ lùi 1 trang trên webview
    @Override
    public void onBackPressed() {

        if (webView.canGoBack()){
            webView.goBack();
        }else {

        super.onBackPressed();
        }
    }
}

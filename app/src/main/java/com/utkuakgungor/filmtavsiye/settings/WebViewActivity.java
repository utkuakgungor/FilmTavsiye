package com.utkuakgungor.filmtavsiye.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

import com.utkuakgungor.filmtavsiye.R;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        WebView webView= findViewById(R.id.webview);
        if(Objects.requireNonNull(getIntent().getStringExtra("type")).equals("terms")){
            setTitle(getResources().getString(R.string.text_terms));
            webView.loadUrl("https://filmtavsiye-dbc33.web.app/terms.html");
        }
        else{
            setTitle(getResources().getString(R.string.text_privacy));
            webView.loadUrl("https://filmtavsiye-dbc33.web.app/privacy.html");
        }
    }
}

package com.utkuakgungor.filmtavsiye.privacy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;

import com.utkuakgungor.filmtavsiye.R;

public class PrivacyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("Ayarlar",MODE_PRIVATE);
        if(sharedPreferences.contains("Gece")){
            setTheme(R.style.AppThemeDark);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_privacy);
        WebView webView= findViewById(R.id.webview);
        webView.loadUrl("https://filmtavsiye-dbc33.firebaseapp.com");
    }
}

package com.example.atyourfingertips;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class Airportwebview extends AppCompatActivity {
    private static final int RP_ACCESS_LOCATION = 1001;
    private String mGeolocationOrigin;
    private GeolocationPermissions.Callback mGeolocationCallback;
    public class GeoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // When user clicks a hyperlink, load in the existing WebView
            view.loadUrl(url);
            return true;
        }
    }
    public class GeoWebChromeClient extends WebChromeClient {
        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            final String permission = Manifest.permission.ACCESS_FINE_LOCATION;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                    ContextCompat.checkSelfPermission(Airportwebview.this, permission) == PackageManager.PERMISSION_GRANTED) {
                // that is you already implement, but it works only
                // we're on SDK < 23 OR user has ALREADY granted permission
                callback.invoke(origin, true, false);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(Airportwebview.this, permission)) {
                    // user has denied this permission before and selected [/] DON'T ASK ME AGAIN
                    // TODO Best Practice: show an AlertDialog explaining why the user could allow this permission, then ask again
                } else {
                    mGeolocationOrigin = origin;
                    mGeolocationCallback = callback;
                    // ask the user for permissions
                    ActivityCompat.requestPermissions(Airportwebview.this, new String[] {permission}, RP_ACCESS_LOCATION);
                }
            }
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RP_ACCESS_LOCATION:
                boolean allow = false;
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // user has allowed these permissions
                    allow = true;
                }
                if (mGeolocationCallback != null) {
                    // use stored callback and origin for allowing Geolocation permission for WebView
                    mGeolocationCallback.invoke(mGeolocationOrigin, allow, false);
                }
                break;
        }
    }
    WebView webView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalwebview);
        webView = findViewById(R.id.HospitalwebView);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new GeoWebViewClient());
        // Below required for geolocation
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.setWebChromeClient(new GeoWebChromeClient());
        // Load google.com
        webView.loadUrl("https://www.google.com/maps/search/airport+near+me/");
    }
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return false;
        }
    }
}
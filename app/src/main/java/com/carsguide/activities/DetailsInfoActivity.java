package com.carsguide.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.carsguide.Constants;
import com.carsguide.R;

public class DetailsInfoActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        webView = (WebView) findViewById(R.id.car_details_webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setSaveFormData(true);
        webView.setWebViewClient(mWebViewClient);

        progressBar = ProgressDialog.show(DetailsInfoActivity.this, getString(R.string.car_details), getString(R.string.loading));
        progressBar.setCancelable(true);
        if (getIntent().hasExtra(Constants.EXTRAS_CAR_DETAILS_URL)) {
            webView.loadUrl(getIntent().getExtras().getString(Constants.EXTRAS_CAR_DETAILS_URL));
        }

    }

    WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (progressBar.isShowing()) {
                progressBar.dismiss();
            }
        }
    };
}

package com.example.lulu.venmonotes;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;


public class WebLoginActivity extends ActionBarActivity {
    private WebView webView;
    private static final String TAG = "WEBVIEW";
    private static final String CLIENT_CODE = "2743";
    private static final String CLIENT_SECRET = "D8BBa5HEnQkT8HuAeHzep39r6s2XURK8";
    private static final String LOG_URL = "https://api.venmo.com/v1/oauth/authorize?client_id=" + CLIENT_CODE +
            "&scope=make_payments%20access_payment_history%20access_feed%20access_profile%20access_email%20access_phone" +
            "%20access_balance%20access_friends&response_type=code";
    private static final String TOKEN_URL = "https://api.venmo.com/v1/oauth/access_token";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weblogin);
        webView = (WebView) findViewById(R.id.webView1);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDefaultFontSize(8);

        webView.loadUrl(LOG_URL);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d(TAG, url);
                if (url.contains("code=")) {
                    String code = url.substring(url.indexOf("code=") + 5);

                    StringBuilder buf = new StringBuilder();
                    try {
                        buf.append("client_id=" + URLEncoder.encode(CLIENT_CODE,"UTF-8")+"&");
                        buf.append("client_secret="+ URLEncoder.encode(CLIENT_SECRET , "UTF-8"));
                        buf.append("code="+ URLEncoder.encode(code, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    new FetchToken().execute(buf.toString());
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Toast.makeText(WebLoginActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchToken extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                return new HttpService().getPostUrl(TOKEN_URL, params[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String jason) {
            Log.d(TAG, jason);
            Intent i = new Intent(WebLoginActivity.this, HomePageActivity.class);
            i.putExtra(HomePageActivity.TOKEN_IN_JASON, jason);
            startActivity(i);
        }
    }
}

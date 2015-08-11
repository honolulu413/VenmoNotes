package com.example.lulu.venmonotes;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Button buttonSignUp;
    private Button buttonSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;

        super.onCreate(savedInstanceState);


        String token = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(HomePageActivity.ACCESS_TOKEN, null);

        if (token != null) {
            Intent i = new Intent(this, HomePageActivity.class);
            i.putExtra(HomePageActivity.ACCESS_TOKEN, token);
            startActivity(i);
        }

        setContentView(R.layout.activity_main);
        buttonSignUp = (Button) findViewById(R.id.signUp);
        buttonSignIn = (Button) findViewById(R.id.signIn);

        buttonSignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!haveNetworkConnection()) {
                    Toast.makeText(MainActivity.this, "No network connection!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://venmo.com/signup"));
                startActivity(browserIntent);
            }

        });

        buttonSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (!haveNetworkConnection()) {
                    Toast.makeText(MainActivity.this, "No network connection!", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, WebLoginActivity.class);
                startActivity(intent);
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

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}

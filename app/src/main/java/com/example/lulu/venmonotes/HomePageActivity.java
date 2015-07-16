package com.example.lulu.venmonotes;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity{
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    private static final String TAG = "HOME";
    private ArrayList<Transaction> mTransactions;
    private String mAction;
    private String mDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String token = getIntent().getStringExtra(ACCESS_TOKEN);
        Log.d(TAG, token);

        new FetchTransactions().execute(token);
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

    private class FetchTransactions extends AsyncTask<String,Void,ArrayList<Transaction>>{
        @Override
        protected ArrayList<Transaction> doInBackground(String... params){
            String token = params[0];
            return new TransactionFetcher(token).getTransactions(mAction, mDate);
        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> result) {
            Log.d(HttpService.TAG, "" + result);
        }

    }
}

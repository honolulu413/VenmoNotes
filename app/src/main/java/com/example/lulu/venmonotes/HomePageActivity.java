package com.example.lulu.venmonotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;


import java.util.ArrayList;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity{
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    private static final String TAG = "HOME";
    private ArrayList<Transaction> mTransactions;
    private String mAction = null;
    private String mDate = null;
    private Button buttonFilter;
    public static final CharSequence[] options = {"pay", "charge"};
    public HomePageFragment fragment;
    public String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        token = getIntent().getStringExtra(ACCESS_TOKEN);
        Log.d(TAG, token);

        FragmentManager fm = getSupportFragmentManager();
        fragment = (HomePageFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new HomePageFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

         new FetchTransactions().execute(token);

        buttonFilter = (Button) findViewById(R.id.filterButton);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                filterAction();
            }
        });

    }

    private void filterAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch (which) {
                    case 0:
                          mAction = "pay";
                          fragment.mTransactions.clear();
                         break;
                    case 1:
                        mAction = "charge";
                        break;

                }
                new FetchTransactions().execute(token);
//                fragment.updateUI();

            }
        });
        builder.create().show();
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
//            mTransactions = result;
            fragment.mTransactions = result;
            fragment.updateUI();


        }

    }
}

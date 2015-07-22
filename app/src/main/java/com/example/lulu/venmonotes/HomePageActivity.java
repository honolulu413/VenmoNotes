package com.example.lulu.venmonotes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity{
    enum Category {ALL, INCOME, EXPENSE}
    Category mCategory = Category.ALL;
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    private static final String TAG = "HOME";
    private ArrayList<Transaction> mTransactions;
    private String mAction = null;
    private String mDate = null;
    private Button buttonFilter;
    public static final CharSequence[] options = {"pay", "charge"};
    public HomePageFragment fragment;
    public String token;
    private User currentUser;
    private String balance;

    private SmartImageView mImageView;
    private TextView mUserName;
    private TextView mDisplayName;

    private Fragment mTranFragment;
    private Fragment mStaFragment;
    private RadioGroup mRadioGroup;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        token = getIntent().getStringExtra(ACCESS_TOKEN);
        Log.d(TAG, token);

        fm = getSupportFragmentManager();
        fragment = (HomePageFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new HomePageFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        new ProfileFetcher().execute(token);

        buttonFilter = (Button) findViewById(R.id.filterButton);
        buttonFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
                //filterAction();
            }
        });

        mImageView = (SmartImageView) findViewById(R.id.imageView);
        mUserName = (TextView) findViewById(R.id.user_name);
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.tab);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fm.beginTransaction();
                

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
            return new TransactionFetcher(token, currentUser.getUserName()).getTransactions(mDate);
        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> result) {
            mTransactions = result;
            fragment.updateUI(result);
        }

    }

    private void updateUI() {
        if (mCategory == Category.ALL) {
            fragment.updateUI(mTransactions);
            return;
        }

        ArrayList<Transaction> tmp = new ArrayList<Transaction>();
        boolean getPositive = mCategory == Category.INCOME;
        for (Transaction t: mTransactions) {
            if (t.isPositive() == getPositive) {
                tmp.add(t);
            }
        }

        fragment.updateUI(tmp);
    }

    private class ProfileFetcher extends AsyncTask<String, Void, JSONObject>{
        private final String url = "https://api.venmo.com/v1/me?access_token=";
        @Override
        protected JSONObject doInBackground(String... params) {
            String token = params[0];
            String content = new HttpService().getUrl(url + token);
            //Log.d(HttpService.TAG, content);
            try {
                return new JSONObject(content).getJSONObject("data");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                balance = jsonObject.getString("balance");
                currentUser = new User(jsonObject.getJSONObject("user"));
                mImageView.setImageUrl(currentUser.getProfileUrl());
                Log.d(HttpService.TAG, currentUser.getProfileUrl());

                mDisplayName.setText(currentUser.getDisplayName());
                mUserName.setText("@" + currentUser.getUserName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new FetchTransactions().execute(token);
        }
    }
}



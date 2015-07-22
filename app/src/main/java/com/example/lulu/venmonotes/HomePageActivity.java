package com.example.lulu.venmonotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;


import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity {
    enum Category {ALL, INCOME, EXPENSE}
    Category mCategory = Category.ALL;
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    private static final String TAG = "DATE";
    private ArrayList<Transaction> mTransactions;
    private String mAction = null;
    private String mDate = null;
    private Button buttonFilter;
    private static final CharSequence[] options = {"All", "Income", "Expense", "Date"};
    private final boolean[] selected = new boolean[]{false, false, false, false};
    private static final int DATE_PICKER_ID = 111;
    private int year, month, day;

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

        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);

        fm = getSupportFragmentManager();
        fragment = (HomePageFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new HomePageFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }

        new ProfileFetcher().execute(token);

//        new FetchTransactions().execute(token);

//        buttonFilter = (Button) findViewById(R.id.filterButton);
//        buttonFilter.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                //DO SOMETHING! {RUN SOME FUNCTION ... DO CHECKS... ETC}
//                filterAction();
//
//            }
//        });

        mImageView = (SmartImageView) findViewById(R.id.imageView);
        mUserName = (TextView) findViewById(R.id.user_name);
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.tab);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fm.beginTransaction();
                Log.d(TAG, "lalallalalla IS " + checkedId);

                if (checkedId == 1) {
                    Log.d(TAG, "sdf IS " + checkedId);

                }
//                    filterAction();

//                switch (checkedId) {
//                    case 1:
//                        filterAction();
//                        break;
//
//
//                }
            }
        });
    }

    private void filterAction() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Filter");

        builder.setMultiChoiceItems(options, selected, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // The 'which' argument contains the index position
                // of the selected item
                if (isChecked) {

                    switch (which) {
                        case 0:
                            mCategory = Category.ALL;
                            break;
                        case 1:
                            mCategory = Category.INCOME;
                            break;
                        case 2:
                            mCategory = Category.EXPENSE;
                            break;
                        case 3:
                            showDialog(DATE_PICKER_ID);
                            break;

                    }
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                new FetchTransactions().execute(token);

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

    private class FetchTransactions extends AsyncTask<String, Void, ArrayList<Transaction>> {
        @Override
        protected ArrayList<Transaction> doInBackground(String... params) {
            String token = params[0];
            if (!selected[3])
                mDate = null;
            return new TransactionFetcher(token, currentUser.getUserName()).getTransactions(mDate);
        }

        @Override
        protected void onPostExecute(ArrayList<Transaction> result) {
            mTransactions = result;
//            fragment.mTransactions = result;
            updateUI();


        }

    }

    private void updateUI() {
        Log.d(TAG, "CATEGORY IS " + mCategory);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth + 1;
            day   = selectedDay;
            mDate = "" + year + "-" + month + "-" + day;
            Log.d(TAG, "MONTH IS " + month + "");


        }
    };

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

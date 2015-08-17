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
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.DatePicker;


import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity {
    enum Category {ALL, INCOME, EXPENSE}

    Category mCategory = Category.ALL;
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    public static final String CURRENT_USER = "com.example.lulu.HomePageActivity.currentUser";
    private static final String TAG = "DATE";
    private ArrayList<Transaction> mTransactions;
    private String mAction = null;
    private String mDate = null;
    private Button buttonFilter;
    private static final CharSequence[] options = {"All", "Income", "Expense", "Date"};
    private final boolean[] selected = new boolean[]{false, false, false, false};
    private static final int DATE_PICKER_ID = 111;
    private int year, month, day;

    public HomePageFragment mHomeFragment;
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

    private HashMap<User, ArrayList<Transaction>> mFriendTransactions;
    private ArrayList<User> mFriendsList;

    private ListView mListViewFriend;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        token = getIntent().getStringExtra(ACCESS_TOKEN);
        //Log.d(TAG, token);
        mFriendTransactions = new HashMap<User, ArrayList<Transaction>>();
        // Get current date by calender

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        fm = getSupportFragmentManager();
        mHomeFragment = (HomePageFragment) fm.findFragmentById(R.id.fragmentContainer);
        if (mHomeFragment == null) {
            mHomeFragment = new HomePageFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, mHomeFragment)
                    .commit();
        }

        new ProfileFetcher().execute(token);

//        mListViewFriend = (ListView) findViewById(R.id.friendList);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        mImageView = (SmartImageView) findViewById(R.id.imageView);
        mUserName = (TextView) findViewById(R.id.user_name);
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.tab);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(HomePageActivity.this, FriendSearchActivity.class);
                i.putExtra(HomePageActivity.CURRENT_USER, currentUser.getUserName());
                startActivity(i);
            }
        });


        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                FragmentTransaction transaction = fm.beginTransaction();
                switch (checkedId) {
                    case R.id.filter:
                        transaction.replace(R.id.fragmentContainer, mHomeFragment).commit();
                        filterAction();
                        break;
                    case R.id.transactions:
                        transaction.replace(R.id.fragmentContainer, mHomeFragment).commit();
                        mCategory = Category.ALL;

                        updateUI();
                        break;
                    case R.id.statistics:
                        if (mStaFragment == null)
                            mStaFragment = StatisticsFragment.newInstance(mTransactions, mFriendTransactions);
                        transaction.replace(R.id.fragmentContainer, mStaFragment).commit();
//                        updateUI();
                        break;
                }
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


    private class FetchFriends extends AsyncTask<String, Void, ArrayList<User>> {
        @Override
        protected ArrayList<User> doInBackground(String... params) {
            String token = params[0];
            return new FriendSearchFetcher(token, currentUser.getUserName()).getFriend();
        }

        @Override
        protected void onPostExecute(ArrayList<User> result) {
            mFriendsList = result;
//            mListViewFriend.setAdapter(new FriendSearchAdapter(HomePageActivity.this, -1, mFriendsList));

        }

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
            updateTransaction();
//            mHomeFragment.mTransactions = result;
            updateUI();


        }

    }

    private void updateTransaction() {
        mFriendTransactions = new HashMap<User, ArrayList<Transaction>>();
        for (Transaction tx : mTransactions) {
            User friend;
            if (currentUser.getUserName().equals(tx.getActor().getUserName())) {
                friend = tx.getTargetUser();
            } else {
                friend = tx.getActor();
            }
            if (mFriendTransactions.get(friend) == null) {
                mFriendTransactions.put(friend, new ArrayList<Transaction>());
            }
            mFriendTransactions.get(friend).add(tx);
        }
        Log.d(TAG, "Friends\n" + mFriendTransactions);

    }


    private void updateUI() {
        Log.d(TAG, "CATEGORY IS " + mCategory);
        if (mCategory == Category.ALL) {
            Log.d(TAG, "mTransactions is " + mTransactions);

            mHomeFragment.updateUI(mTransactions);
            return;
        }

        ArrayList<Transaction> tmp = new ArrayList<Transaction>();
        boolean getPositive = mCategory == Category.INCOME;
        for (Transaction t : mTransactions) {
            if (t.isPositive() == getPositive) {
                tmp.add(t);
            }
        }
        mHomeFragment.updateUI(tmp);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year = selectedYear;
            month = selectedMonth + 1;
            day = selectedDay;
            mDate = "" + year + "-" + month + "-" + day;
            Log.d(TAG, "MONTH IS " + month + "");


        }
    };

    private class ProfileFetcher extends AsyncTask<String, Void, JSONObject> {
        private final String url = "https://api.venmo.com/v1/me?access_token=";

        @Override
        protected JSONObject doInBackground(String... params) {
            String token = params[0];
            String content = new HttpService().getUrl(url + token);
            //Log.d(HttpService.TAG, content);
            try {
                return new JSONObject(content).getJSONObject("data");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (jsonObject == null) {
                PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this)
                        .edit()
                        .putString(HomePageActivity.ACCESS_TOKEN, null)
                        .commit();
                startActivity(new Intent(HomePageActivity.this, MainActivity.class));
                finish();
                return;
            }
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
//            new FetchFriends().execute(token);
        }
    }
}

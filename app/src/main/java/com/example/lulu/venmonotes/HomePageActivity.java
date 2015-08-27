package com.example.lulu.venmonotes;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.DatePicker;


import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lulu on 7/9/2015.
 */
public class HomePageActivity extends ActionBarActivity {
    enum Category {ALL, INCOME, EXPENSE}

    Category mCategory = Category.ALL;
    public static final String ACCESS_TOKEN = "com.example.lulu.HomePageActivity.accessToken";
    public static final String CURRENT_USER = "com.example.lulu.HomePageActivity.currentUser";
    public static final String TRAN = "com.example.lulu.HomePageActivity.transactions";
    public static final String FRIENDS = "com.example.lulu.HomePageActivity.friends";

    private static final String TAG = "Date";
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
    private TextView mBalance;
    private View mProgressBarContainer;
    private Fragment mTranFragment;
    private Fragment mStaFragment;
    private RadioGroup mRadioGroup;

    private FragmentManager fm;

    private HashMap<User, ArrayList<Transaction>> mFriendTransactions;
    private static ArrayList<User> mFriendsList;

    private ListView mListViewFriend;
    private Button mSearchButton;
    private Button mCreateNoteButton;
    private ListView mDrawer;

    public  static ArrayList<User> getFriendList() {
        return mFriendsList;
    }

    public static final List<String> drawerItems =  Arrays.asList("Search Friend", "Create Note", "Log Out");

    private String[] mNavigationDrawerItemTitles;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;

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

        mProgressBarContainer = findViewById(R.id.progressContainer);
        fm = getSupportFragmentManager();
        mHomeFragment = (HomePageFragment) fm.findFragmentById(R.id.fragmentContainer);
        if (mHomeFragment == null) {
            mHomeFragment = HomePageFragment.newInstance(new ArrayList<Transaction>());
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, mHomeFragment)
                    .commit();
        }

        new ProfileFetcher().execute(token);

        mImageView = (SmartImageView) findViewById(R.id.imageView);
        mUserName = (TextView) findViewById(R.id.user_name);
        mDisplayName = (TextView) findViewById(R.id.display_name);
        mRadioGroup = (RadioGroup) findViewById(R.id.tab);
//        mCreateNoteButton = (Button) findViewById(R.id.createNoteButton);
        mBalance = (TextView) findViewById(R.id.balance);

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[8];

        for (int i = 0; i < drawerItem.length; i++) {
            if (i == 0)
                drawerItem[i] = new ObjectDrawerItem(R.mipmap.ic_people, mNavigationDrawerItemTitles[0]);
            else if (i == 1)
                drawerItem[i] = new ObjectDrawerItem(R.mipmap.ic_note, mNavigationDrawerItemTitles[1]);
            else if (i == 7)
                drawerItem[i] = new ObjectDrawerItem(R.mipmap.ic_logout, mNavigationDrawerItemTitles[2]);
            else
                drawerItem[i] = new ObjectDrawerItem(R.mipmap.ic_white, "");
        }

        DrawerItemAdapter drawerAdapter = new DrawerItemAdapter(this, R.layout.listview_item_row, drawerItem);
        mDrawerList.setAdapter(drawerAdapter);
        mDrawerList.setClickable(true);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) {
                    Intent i = new Intent(HomePageActivity.this, FriendSearchActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable(HomePageActivity.CURRENT_USER, currentUser);

                    i.putExtras(mBundle);
                    i.putExtra(HomePageActivity.TRAN, mFriendTransactions);
                    i.putExtra(HomePageActivity.FRIENDS, mFriendsList);
                    startActivity(i);
                } else if (position == 1) {
                    Intent i = new Intent(HomePageActivity.this, NotesActivity.class);
                    startActivity(i);
                } else if (position == 7) {
                    PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this)
                            .edit()
                            .putString(HomePageActivity.ACCESS_TOKEN, null)
                            .commit();
                    Intent i = new Intent(HomePageActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                }

                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });





//        mDrawer = (ListView) findViewById(R.id.left_drawer);
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(HomePageActivity.this,
//                        android.R.layout.simple_list_item_1,
//                        drawerItems);
//        mDrawer.setAdapter(adapter);
//        mDrawer.setClickable(true);
//        mDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                if (position == 0) {
//                Intent i = new Intent(HomePageActivity.this, FriendSearchActivity.class);
//                Bundle mBundle = new Bundle();
//                mBundle.putSerializable(HomePageActivity.CURRENT_USER, currentUser);
//
//                i.putExtras(mBundle);
//                i.putExtra(HomePageActivity.TRAN, mFriendTransactions);
//                i.putExtra(HomePageActivity.FRIENDS, mFriendsList);
//                startActivity(i);
//                } else if (position == 1) {
//                    Intent i = new Intent(HomePageActivity.this, NotesActivity.class);
//                    startActivity(i);
//                } else {
//                    PreferenceManager.getDefaultSharedPreferences(HomePageActivity.this)
//                            .edit()
//                            .putString(HomePageActivity.ACCESS_TOKEN, null)
//                            .commit();
//                    Intent i = new Intent(HomePageActivity.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//            }
//        });




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
                        Log.d(TAG, "1TX is " + mTransactions);
//                        updateUI();
                        mDate = null;
                        new FetchTransactions().execute(token);
                        break;
                    case R.id.statistics:
                        if (mStaFragment == null)
                            mStaFragment = StatisticsFragment.newInstance(mTransactions, mFriendTransactions);
                        transaction.replace(R.id.fragmentContainer, mStaFragment).commit();
//                        updateUI();
                        Log.d(TAG, "2TX is " + mTransactions);

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
            Log.d(TAG, "after fetch is " + mTransactions);

            updateTransaction();
//            mHomeFragment.mTransactions = result;
            updateUI();
            mProgressBarContainer.setVisibility(View.INVISIBLE);

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
//        Log.d(TAG, "Friends\n" + mFriendTransactions);

    }


    private void updateUI() {
        Log.d(TAG, "CATEGORY IS " + mCategory);
        Log.d(TAG, "mTransactions is " + mTransactions);

        if (mCategory == Category.ALL) {

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
//            Log.d(TAG, "MONTH IS " + month + "");


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
                mBalance.setText("$" + balance + " in Venmo");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new FetchTransactions().execute(token);
            new FetchFriends().execute(token);
        }
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

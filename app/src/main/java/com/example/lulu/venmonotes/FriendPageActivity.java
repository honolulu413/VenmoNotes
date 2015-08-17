package com.example.lulu.venmonotes;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;

/**
 * Created by lulu on 8/17/2015.
 */
public class FriendPageActivity extends ActionBarActivity {
    public static final String USER = "com.example.lulu.FriendPageActivity.user";
    public static final String TRAN = "com.example.lulu.FriendPageActivity.transactions";
    String TAG = "TRAN";
    User mUser;
    SmartImageView mImageView;
    TextView mUserName;
    TextView mDisplayName;
    ArrayList<Transaction> mTransactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        mUser = (User) getIntent().getSerializableExtra(USER);
        mTransactions = (ArrayList<Transaction>) getIntent().getSerializableExtra(TRAN);
        Log.d(TAG, "" + mTransactions);
    }

}

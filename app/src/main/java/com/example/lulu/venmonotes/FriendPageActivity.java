package com.example.lulu.venmonotes;

import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import android.util.Log;

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

        mImageView = (SmartImageView) findViewById(R.id.imageView);
        mUserName = (TextView) findViewById(R.id.user_name);
        mDisplayName = (TextView) findViewById(R.id.display_name);

        mImageView.setImageUrl(mUser.getProfileUrl());
        mUserName.setText(mUser.getUserName());
        mDisplayName.setText(mUser.getDisplayName());

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.transactions, HomePageFragment.class, new Bundler().putSerializable(HomePageFragment.TRANS, mTransactions).get())
                .add(R.string.statistics, StatisticsFragment.class, new Bundler().putSerializable(FriendStatisticsFragment.ARRAYLIST, mTransactions).get())
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
    }

}

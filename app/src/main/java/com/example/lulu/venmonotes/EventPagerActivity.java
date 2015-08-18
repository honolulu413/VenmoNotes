package com.example.lulu.venmonotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Joseph on 2015/8/18.
 */
public class EventPagerActivity extends FragmentActivity {
    private ViewPager mViewPager;
    private ArrayList<Event> mEvents;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mEvents = EventLab.get(this).getEvents();
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mEvents.size();
            }

            @Override
            public Fragment getItem(int pos) {
                Event event = mEvents.get(pos);
                return EventFragment.newInstance(event.getDate());
            }
        });

        Date eventDate = (Date) getIntent().getSerializableExtra(EventFragment.EXTRA_EVENT_DATE);
        for (int i = 0; i < mEvents.size(); i++) {
            if (mEvents.get(i).getDate().equals(eventDate)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }


}

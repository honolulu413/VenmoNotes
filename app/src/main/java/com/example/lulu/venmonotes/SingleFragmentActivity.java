package com.example.lulu.venmonotes;

/**
 * Created by Joseph on 2015/8/18.
 */

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

/**
 * Created by Joseph on 2015/5/30.
 */
public abstract class SingleFragmentActivity extends ActionBarActivity {
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainerNotes);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.rgb(59, 89, 152)));

        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainerNotes, fragment)
                    .commit();
        }
    }
}


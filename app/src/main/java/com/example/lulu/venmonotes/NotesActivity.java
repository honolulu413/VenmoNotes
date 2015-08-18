package com.example.lulu.venmonotes;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by Joseph on 2015/8/18.
 */
public class NotesActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new NotesFragment();
    }
}

package com.example.lulu.venmonotes;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Joseph on 2015/7/16.
 */
public class HomePageFragment extends ListFragment {
    //    public ArrayList<String> transactions = new ArrayList<String>();
    private static final String TAG = "ACTIVITY";

    private ArrayList<Transaction> mTransactions = new ArrayList<Transaction>();

    private HomePageActivity myActivity;

//    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities.
     */
//    public interface Callbacks {
//        void onCrimeSelected(String tx);
//    }
//
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myActivity = (HomePageActivity) activity;
    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mCallbacks = null;
//    }
    public void updateUI(ArrayList<Transaction> list) {
//        mTransactions.clear();
        mTransactions = list;
//        ( (ArrayAdapter<Transaction>)getListAdapter()).notifyDataSetChanged();
//        ArrayAdapter<Transaction> adapter =
//                new ArrayAdapter<Transaction>(getActivity(),
//                        android.R.layout.simple_list_item_1,
//                        mTransactions);

        Log.d(TAG, "Activity is " + myActivity);
        ArrayAdapter<Transaction> adapter =
                new TransactionAdapter(myActivity,
                        -1,
                        mTransactions);
        setListAdapter(adapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, parent, savedInstanceState);

        ListView listView = (ListView) v.findViewById(android.R.id.list);


        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ArrayAdapter<Transaction> adapter =
                new TransactionAdapter(getActivity(),
                        -1,
                        mTransactions);
        setListAdapter(adapter);
    }


}

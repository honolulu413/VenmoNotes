package com.example.lulu.venmonotes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by lulu on 8/18/2015.
 */
public class EventFragment extends Fragment {
    private static final String TAG = "EVENTFRAGMENT";
    public static final String EXTRA_EVENT = "com.event";
    public static final String FRIENDS = "com.example.lulu.EventFragment.friends";
    private static final int REQUEST_PHOTO = 1;
    private static final String DIALOG_IMAGE = "image";
    private ArrayList<User> mFriends;

    private Event mEvent;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;
    private EditText mTitleField;
    private TextView mDateFiled;
    private User mSelectedFriend;
    private Button mAddPayButton;
    private Button mAddChargeButton;
    private AutoCompleteTextView mAutoText;
    private CheckBox mSolvedCheckBox;
    private TextView mPayTextView;
    private TextView mChargeTextView;
    private EditText mAmountTextView;

    private double mPayAmount;
    private double mChargeAmount;
    private String mAmountString = "";

    public static EventFragment newInstance(Event event, ArrayList<User> friends) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_EVENT, event);
        args.putSerializable(FRIENDS, friends);
        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mEvent = (Event) getArguments().getSerializable(EXTRA_EVENT);
        mFriends = (ArrayList<User>) getArguments().getSerializable(FRIENDS);
        FragmentManager fm = getChildFragmentManager();
        SubEventListFragment fragment = null;

        if (fragment == null) {
            fragment = SubEventListFragment.newInstace(mEvent.getSubEvents());
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_event, parent, false);

        mPayTextView = (TextView) v.findViewById(R.id.pay_amount);
        mChargeTextView = (TextView) v.findViewById(R.id.charge_amount);
        mPayTextView.setTextColor(Color.RED);
        mChargeTextView.setTextColor(Color.GREEN);
        updateUI();

        mDateFiled = (TextView) v.findViewById(R.id.event_date);
        mDateFiled.setText(mEvent.getDateString());

        mPhotoButton = (ImageButton) v.findViewById(R.id.event_imageButton);
        mPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EventCameraActivity.class);
                startActivityForResult(i, REQUEST_PHOTO);
            }
        });

        mPhotoView = (ImageView) v.findViewById(R.id.event_imageView);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Photo p = mEvent.getPhoto();
                if (p == null)
                    return;

                FragmentManager fm = getActivity()
                        .getSupportFragmentManager();
                String path = getActivity()
                        .getFileStreamPath(p.getFilename()).getAbsolutePath();
                ImageFragment.newInstance(path)
                        .show(fm, DIALOG_IMAGE);
            }
        });

        mTitleField = (EditText) v.findViewById(R.id.event_title);
        mTitleField.setText(mEvent.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(
                    CharSequence c, int start, int before, int count) {
                mEvent.setTitle(c.toString());
            }

            public void beforeTextChanged(
                    CharSequence c, int start, int count, int after) {
                // This space intentionally left blank
            }

            public void afterTextChanged(Editable c) {
                // This one too
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.event_solved);
        mSolvedCheckBox.setChecked(mEvent.isSettled());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Set the crime's solved property
                mEvent.setSettled(isChecked);
            }
        });

        mAutoText = (AutoCompleteTextView) v.findViewById(R.id.friend_selector);
        FriendSearchAdapter adapter = new FriendSearchAdapter(getActivity(), -1, mFriends);
        mAutoText.setAdapter(adapter);
        mAutoText.setThreshold(1);
        mAutoText.setDropDownHeight(600);
        mAutoText.setCompletionHint("Search by name");

        mAmountTextView = (EditText) v.findViewById(R.id.amount);

        mAddPayButton = (Button) v.findViewById(R.id.add_pay);
        mAddChargeButton = (Button) v.findViewById(R.id.add_charge);

        mAddPayButton.setEnabled(false);
        mAddChargeButton.setEnabled(false);
        mAutoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAddPayButton.setEnabled(false);
                mAddChargeButton.setEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAutoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedFriend = (User) parent.getAdapter().getItem(position);
                mAutoText.setText(mSelectedFriend.getDisplayName());
                mAddPayButton.setEnabled(true);
                mAddChargeButton.setEnabled(true);
            }
        });



        mAddPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, mAmountTextView.getText().toString());
                    double amount = Double.parseDouble(mAmountTextView.getText().toString());
                    SubEvent s = new SubEvent(mSelectedFriend);
                    s.setAction("pay");
                    s.setIsPositive(false);
                    s.setAmount(amount);
                    mEvent.addSubEvent(s);
                    updateUI();
                } catch (Exception e) {
                    Log.d(TAG, "wrong number");
                    return;
                }
            }
        });

        mAddChargeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    double amount = Double.parseDouble(mAmountTextView.getText().toString());
                    SubEvent s = new SubEvent(mSelectedFriend);
                    s.setAction("charge");
                    s.setIsPositive(true);
                    s.setAmount(amount);
                    mEvent.addSubEvent(s);
                    updateUI();
                } catch (Exception e) {
                    return;
                }
            }
        });
        return v;
    }

    public void updateUI() {
        FragmentManager fm = getChildFragmentManager();
        SubEventListFragment fragment = (SubEventListFragment)fm.findFragmentById(R.id.fragmentContainer);
        if (fragment != null) fragment.updateUI();
        mPayAmount = 0;
        mChargeAmount = 0;
        for (SubEvent e: mEvent.getSubEvents()) {
            if (e.isPositive()) {
                mChargeAmount += e.getAmount();
            } else {
                mPayAmount += e.getAmount();
            }
        }
        mPayTextView.setText("" + mPayAmount);
        mChargeTextView.setText("" + mChargeAmount);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_PHOTO) {
            // Create a new Photo object and attach it to the crime
            String filename = data
                    .getStringExtra(EventCameraFragment.EXTRA_PHOTO_FILENAME);
            if (filename != null) {
                Photo p = new Photo(filename);
                mEvent.setPhoto(p);
                showPhoto();
            }
        }
    }

    private void showPhoto() {
        Photo p = mEvent.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventLab.get(getActivity()).saveEvents();
    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }

    @Override
    public void onStart() {
        super.onStart();
        showPhoto();
    }
}
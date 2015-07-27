package com.example.lulu.venmonotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

import java.util.ArrayList;

/**
 * Created by Joseph on 2015/7/26.
 */
public class TransactionAdapter extends ArrayAdapter<Transaction> {
    public TransactionAdapter(Context context, int resource, ArrayList<Transaction> items) {
        super(context, resource, items);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_transaction, null);
        }

        Transaction transaction = getItem(position);

        TextView textViewDetail = (TextView) convertView.findViewById(R.id.tx_detail);
        textViewDetail.setText(transaction.getDetail());

        TextView textViewDate = (TextView) convertView.findViewById(R.id.tx_date);
        textViewDate.setText(transaction.getShortDate());

        TextView textViewNote = (TextView) convertView.findViewById(R.id.tx_note);
        textViewNote.setText(transaction.getNote());

        SmartImageView mImageView = (SmartImageView) convertView.findViewById(R.id.profileImg);
        mImageView.setImageUrl(transaction.getActor().getProfileUrl());


        return convertView;
    }


}

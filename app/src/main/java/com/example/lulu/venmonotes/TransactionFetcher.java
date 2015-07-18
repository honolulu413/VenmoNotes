package com.example.lulu.venmonotes;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lulu on 7/11/2015.
 */
public class TransactionFetcher {
    private final String ENDPOINT = "https://api.venmo.com/v1/payments";
    private String token;
    private String status = "settled";
    private String currentUser;
    public TransactionFetcher(String token, String currentUser) {
        this.token = token;
        this.currentUser = currentUser;
    }

    public ArrayList<Transaction> getTransactions(String dateAfter) {
        Uri.Builder builder = Uri.parse(ENDPOINT).buildUpon();
        builder.appendQueryParameter("access_token", token).appendQueryParameter("status", status);
        if (dateAfter != null) builder.appendQueryParameter("after", dateAfter);

        String urlSpec = builder.build().toString();
        HttpService httpService = new HttpService();

        String jsonString = httpService.getUrl(urlSpec);
        Log.d(HttpService.TAG, jsonString);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject tmpObject = jsonArray.getJSONObject(i);
                String date = tmpObject.getString("date_completed");
                JSONObject jUser = tmpObject.getJSONObject("target").getJSONObject("user");
                User target = new User(jUser.getString("username"), jUser.getString("display_name"));

                JSONObject jActor = tmpObject.getJSONObject("actor");
                User actor = new User(jActor.getString("username"), jActor.getString("display_name"));
                String note = tmpObject.getString("note");
                double amount = tmpObject.getDouble("amount");
                String tAction = tmpObject.getString("action");

                Transaction t = new Transaction(date, target, actor, note, amount, tAction, currentUser);
                transactions.add(t);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(HttpService.TAG, "" + e);
        }
        Log.d(HttpService.TAG, "" + transactions);
        return transactions;
    }

}

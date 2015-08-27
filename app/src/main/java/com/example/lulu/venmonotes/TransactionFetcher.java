package com.example.lulu.venmonotes;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

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
        HashMap<String, User> users = new HashMap<String, User>();
        Uri.Builder builder = Uri.parse(ENDPOINT).buildUpon();
        builder.appendQueryParameter("access_token", token).appendQueryParameter("status", status);
        if (dateAfter != null) builder.appendQueryParameter("after", dateAfter);

        String urlSpec = builder.build().toString();
        HttpService httpService = new HttpService();

        String jsonString = httpService.getUrl(urlSpec);
        Log.d(HttpService.TAG, "" + jsonString);
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            addTransaction(transactions, jsonObject, users);

            String next = jsonObject.getJSONObject("pagination").getString("next");
            while (!(next == null || next.equals("") || next.equals("null"))) {
                Thread.sleep(200);
                jsonString = httpService.getUrl(next + "&access_token=" + token);
                jsonObject = new JSONObject(jsonString);
                next = jsonObject.getJSONObject("pagination").getString("next");
                addTransaction(transactions, jsonObject, users);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(HttpService.TAG, "" + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(HttpService.TAG, "" + transactions);
        return transactions;
    }

    public void addTransaction(ArrayList<Transaction> transactions, JSONObject jsonObject, HashMap<String, User> users) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tmpObject = jsonArray.getJSONObject(i);
            String date = tmpObject.getString("date_completed");
//                User target = new User(tmpObject.getJSONObject("target").getJSONObject("user"));
//                User actor = new User(tmpObject.getJSONObject("actor"));

            String targetName = tmpObject.getJSONObject("target").getJSONObject("user").getString("username");
            if (!users.containsKey(targetName)) {
                users.put(targetName, new User(tmpObject.getJSONObject("target").getJSONObject("user")));
            }
            User target = users.get(targetName);

            String actorName = tmpObject.getJSONObject("actor").getString("username");
            if (!users.containsKey(actorName)) {
                users.put(actorName, new User(tmpObject.getJSONObject("actor")));
            }
            User actor = users.get(actorName);
            String note = tmpObject.getString("note");
            double amount = tmpObject.getDouble("amount");
            String tAction = tmpObject.getString("action");

            Transaction t = new Transaction(date, target, actor, note, amount, tAction, currentUser);
            transactions.add(t);

        }
    }
}

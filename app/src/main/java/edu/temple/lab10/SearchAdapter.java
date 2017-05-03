package edu.temple.lab10;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.AccessControlContext;

/**
 * Created by casey on 5/3/17.
 *
 * absolutely nothing about this part works yet
 */

public class SearchAdapter extends ArrayAdapter<JSONObject> {

    Context context;
    JSONArray companies;
    String key;

    public SearchAdapter(Context c, int resource, JSONArray array) {
        super(c, resource);
        companies = array;
    }

    public SearchAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
    }

    public SearchAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public void setJSONArray(JSONArray array) {
        companies = array;
    }

    public int getCount() {
        return companies.length();
    }

    public JSONObject getJSONObject(int i) throws JSONException {
        return companies.getJSONObject(i);
    }
    public String getString(int i) throws JSONException{
        return companies.getJSONObject(i).getString(key);
    }

    public void remove(int i) {
        companies.remove(i);
    }

    public boolean contains(String value, int i) throws JSONException {
        return companies.getJSONObject(i).getString(key).contains(value);
    }

    public void contract() throws JSONException {
    /*  String tmp = companies.toString();
        companies = new JSONArray(tmp); */
        companies = new JSONArray(companies.toString());
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        TextView tv = new TextView(context);
        try {
            tv.setText(companies.getJSONObject(position).getString(key).toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return tv;
    }
}

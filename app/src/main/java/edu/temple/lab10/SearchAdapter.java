package edu.temple.lab10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.security.AccessControlContext;

/**
 * Created by casey on 5/3/17.
 *
 * absolutely nothing about this part works yet
 */

public class SearchAdapter extends BaseAdapter {

    Context context;
    JSONArray companies;

    public SearchAdapter(Context context, JSONArray companies) {
        this.companies = companies;
    }

    @Override
    public int getCount() {
        return companies.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return companies.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView name = new TextView(context);
        name.setTextSize(21);
        return name;
    }
}

package edu.temple.lab10;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by casey on 4/30/17.
 */

public class StockListAdapter extends BaseAdapter {

    Context context;
    Portfolio portfolio;

    public StockListAdapter(Context context, Portfolio portfolio) {
        this.context = context;
        this.portfolio = portfolio;
    }

    @Override
    public int getCount() {
        return portfolio.size();
    }

    @Override
    public Object getItem(int i) {
        return portfolio.getStockAtIndex(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView tv = new TextView(context);
        tv.setTextSize(21);
        // obtain ticker symbols of all Stocks in Portfolio
        tv.setText((portfolio.getStockAtIndex(i).getSymbol()));
        return tv;
    }
}

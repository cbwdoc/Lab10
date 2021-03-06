package edu.temple.lab10;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.logging.Logger;


public class StockDetailsFragment extends Fragment {

    public static  String NAME_BUNDLE_KEY = "Name";
    public static  String SHARE_BUNDLE_KEY = "SharePrice";
    public static  String SYMBOL_BUNDLE_KEY = "Symbol";

    Logger log = Logger.getAnonymousLogger();
    View v;
    ImageView dayChart;
    TextView companyName, sharePrice;
    String symbol;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(NAME_BUNDLE_KEY, companyName.getText().toString());
        outState.putString(SHARE_BUNDLE_KEY, sharePrice.getText().toString());
        outState.putString(SYMBOL_BUNDLE_KEY, symbol);
    }

    public StockDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stock_details, container, false);

        dayChart = (ImageView) layout.findViewById(R.id.day_chart);
        companyName = (TextView) layout.findViewById(R.id.company_name);
        sharePrice = (TextView) layout.findViewById(R.id.share_price);
        // all values are held in savedInstanceState.mMap
        if (savedInstanceState != null) {
            // not working in Android Studio 2.3.1 for Windows 10
            log.info(savedInstanceState.getString(NAME_BUNDLE_KEY) + '\n'
                    + savedInstanceState.getString(SHARE_BUNDLE_KEY) + '\n'
                    + savedInstanceState.getString(SYMBOL_BUNDLE_KEY));
            companyName.setText(savedInstanceState.getString(NAME_BUNDLE_KEY));
            sharePrice.setText(savedInstanceState.getString(SHARE_BUNDLE_KEY));
            displayChart(savedInstanceState.getString(SYMBOL_BUNDLE_KEY));
            // execute pending transactions?
        }

        return layout;
    }

    public void getChartAndSymbol(Stock stock) {
        symbol = stock.getSymbol();
        Picasso.with(dayChart.getContext()).load("https://chart.yahoo.com/z?t=1d&s=" + stock.getSymbol()).into(dayChart);
    }

    public void displayCompany(Stock stock) {
        companyName.setText(String.valueOf(stock.getName()));
    }

    public void displayPrice(Stock stock) {
        sharePrice.setText("$" + String.valueOf(stock.getPrice()));
    }

    public void displayChart(String symbol) {
        Picasso.with(dayChart.getContext()).load("https://chart.yahoo.com/z?t=1d&s=" + symbol).into(dayChart);
    }

    public void displayAll(Stock stock) {
        displayCompany(stock);
        displayPrice(stock);
        getChartAndSymbol(stock);
    }


}

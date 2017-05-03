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

    Logger log = Logger.getAnonymousLogger();
    View v;
    ImageView dayChart;
    TextView companyName, sharePrice;


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

        return layout;
    }

    public void displayChart(Stock stock) {
        Picasso.with(dayChart.getContext()).load("https://chart.yahoo.com/z?t=1d&s="+stock.getSymbol()).into(dayChart);
    }

    public void displayCompany(Stock stock) {
        companyName.setText(String.valueOf(stock.getName()));
    }

    public void displayPrice(Stock stock) {
        sharePrice.setText("$" + String.valueOf(stock.getPrice()));
    }

    public void displayAll(Stock stock) {
        displayCompany(stock);
        displayPrice(stock);
        displayChart(stock);
    }


}

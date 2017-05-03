package edu.temple.lab10;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.logging.Logger;

public class StockListFragment extends Fragment {

    public static  String BUNDLE_KEY = "portfolio";

    stockSelectedInterface parent;

    TextView promptText1, promptText2;
    ImageView promptImage;
    ListView stockList;
    Portfolio portfolio;
    boolean noStocks = false;

    public StockListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof stockSelectedInterface) {
            parent = (stockSelectedInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement stockSelectedInterface");
        }
*/    }

    @Override
    public void onDetach() {
        super.onDetach();
        parent = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_stock_list, container, false);

        promptText1 = (TextView) layout.findViewById(R.id.textView);
        promptImage = (ImageView) layout.findViewById(R.id.imageView);
        promptText2 = (TextView) layout.findViewById(R.id.textView2);
        stockList = (ListView) layout.findViewById(R.id.stocks);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            portfolio = (Portfolio) bundle.getSerializable(BUNDLE_KEY);
        }

        if (portfolio == null || portfolio.size() == 0) {
            noStocks = true;
            promptText1.setVisibility(View.VISIBLE);
            promptImage.setVisibility(View.VISIBLE);
            promptText2.setVisibility(View.VISIBLE);
        }

        System.out.println(portfolio.size());

        StockListAdapter adapter = new StockListAdapter (getContext(), portfolio);
        stockList.setAdapter(adapter);



        stockList.setOnItemClickListener((new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((stockSelectedInterface) getActivity())
                        .stockSelected(portfolio.getStockAtIndex(i));
            }
        }));

        return layout;
    }

/*
    public void  setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
*/

    public void addStock(Stock stock) {
        // portfolio.add(stock);    // would like to know why this adds an extra symbol
        ((StockListAdapter) stockList.getAdapter()).notifyDataSetChanged();
        if (noStocks) {
            promptText1.setVisibility(View.GONE);
            promptImage.setVisibility(View.GONE);
            promptText2.setVisibility(View.GONE);
        }
    }

    public interface  stockSelectedInterface {
        public void stockSelected(Stock stock);
    }
}

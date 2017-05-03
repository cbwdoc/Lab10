package edu.temple.lab10;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import android.os.Message;
/**
 * Created by casey on 4/30/17.
 */

public class Portfolio implements Serializable {
    private ArrayList<Stock> stocks;

    public Portfolio(ArrayList<Stock> stocks) {
        this.stocks = stocks;
    }

    public Portfolio() {
        stocks = new ArrayList<Stock>();
    }

    public void add(Stock stock) {
        stocks.add(stock);
    }

    public ArrayList<Stock> getStocks() {
        return stocks;
    }

    public Stock getStockAtIndex(int index) {
        return stocks.get(index);
    }

    public int size() {
        return stocks.size();
    }
}

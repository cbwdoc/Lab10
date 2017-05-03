package edu.temple.lab10;

import android.media.Image;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.logging.Handler;

/**
 * Created by casey on 4/30/17.
 */

public class Stock implements Serializable{

    private String companyName, tickerSymbol;
    private double sharePrice;

    public Stock(String companyName, String tickerSymbol) {
        this.companyName = companyName;
        this.tickerSymbol = tickerSymbol;
    }

    public Stock(String companyName, String tickerSymbol, double sharePrice) {
        this.companyName = companyName;
        this.tickerSymbol = tickerSymbol;
        this.sharePrice = sharePrice;
    }


    public Stock (JSONObject stockObject) throws JSONException {
        companyName = stockObject.getString("Name");
        tickerSymbol= stockObject.getString("Symbol");
        sharePrice = stockObject.getDouble("LastPrice");
    }

    // getters/setters for all data fields

    public String getName() {
        return companyName;
    }

    public void setName(String companyName) {
        this.companyName = companyName;
    }

    public String getSymbol() {
        return tickerSymbol;
    }

    public void setSymbol(String tickerSymbol) {
        this.tickerSymbol = tickerSymbol;
    }

    public double getPrice() {
        return sharePrice;
    }

    public void setPrice(double sharePrice) {
        this.sharePrice = sharePrice;
    }


    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Stock) &&
                this.tickerSymbol.equalsIgnoreCase(((Stock) obj).tickerSymbol);
    }

    public JSONObject getStockAsJSON() {
        JSONObject stockObj = new JSONObject();
        try {
            stockObj.put("Name", companyName);
            stockObj.put("Symbol", tickerSymbol);
            stockObj.put("LastPrice", sharePrice);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return stockObj;
    }

    // assumes ticker symbols and company names don't change, only stock prices
    public void update(Handler handler) {
        // use the handler to update the dayChart and sharePrice using the name
    }

}

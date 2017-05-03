package edu.temple.lab10;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class StockPriceService extends Service {

    // binds the service
    public class StockBinder extends Binder {
        StockPriceService getService (){
            return StockPriceService.this;
        }
    }

    // IBinder is an interface which Binder implements to receive interactions from clients
    private final IBinder stockBinder = new StockBinder();

    // right now just a getter for stockBinder
    @Override
    public IBinder onBind(Intent intent) {
        return stockBinder;
    }

    // uses Thread and Handler to retrieve JSONObject and parse info given a ticker symbol
    public void getQuote(final String symbol, final int index, final Handler handler) {

        // thread for web service & Markit API with custom methods
        Thread t = new Thread() {

            @Override
            public void run() {

                URL stockUrl;

                try {
                    stockUrl = new URL("http://dev.markitondemand.com/MODApis/Api/v2/Quote/jsonp?symbol=" + symbol + "&callback=myFunction");

                    // buffers an InputStreamReader for URL source
                    // the JSON objects from this API are only one line apiece
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    stockUrl.openStream()));

                    String response, tmpResponse;

                    tmpResponse = reader.readLine();
                    // extract the JSON object from reader
                    response = tmpResponse.substring(tmpResponse.indexOf('{'), tmpResponse.lastIndexOf('}') + 1);

                    // convert String to JSON object
                    Stock stockObject = new Stock(new JSONObject(response));

                    //
                    Message msg = Message.obtain();

                    // put JSON object in message
                    msg.obj = stockObject.getPrice();
                    msg.arg1 = index;

                    // return message to handler
                    handler.sendMessage(msg);

                } catch (Exception e) {

                    //
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }
}
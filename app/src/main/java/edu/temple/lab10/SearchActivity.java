package edu.temple.lab10;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static java.security.AccessController.getContext;

public class SearchActivity extends AppCompatActivity {

    AutoCompleteTextView tickerQuery;
    ImageButton searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tickerQuery = (AutoCompleteTextView) findViewById(R.id.search_bar);
        searchButton = (ImageButton) findViewById(R.id.search_button);

        // listens for the Dialog Button
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!tickerQuery.getText().toString().isEmpty()) {
                    String response = tickerQuery.getText().toString();
                    // takes user Input understood to be a ticker symbol
                    Intent passIntent = new Intent();

                    // assigns a Bundle with this info to passIntent.mExtras
                    // MUST MATCH ARGUMENT OF data.getStringExtra(String)
                    passIntent.putExtra("Symbol", response);
                    //
                    setResult(RESULT_OK, passIntent);
                    // returns to MainActivity
                    finish();
                }
            }
        });
    }


    // uses Thread and Handler to retrieve JSONObject and parse info given a ticker symbol
    public static void getNewQuote(final String symbol, final Handler handler) {

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
                    JSONObject stockObject = new JSONObject(response);

                    //
                    Message msg = Message.obtain();

                    // put JSON object in message
                    msg.obj = stockObject;

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

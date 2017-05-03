package edu.temple.lab10;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private final int POPUP_ACTIVITY = 1;

    public Portfolio portfolio;

    Logger log = Logger.getAnonymousLogger();
    StockListFragment listFrag;
    StockDetailsFragment detailsFrag;
    boolean connected, bothFrags, handled;
    File file;
    String fileName = "Portfolio", newStock;
    Writer fileWriter;
    StockPriceService stockService;
    int delay = 60000; // in milliseconds

    ServiceConnection appConnection = new ServiceConnection() {

        // required methods for ServiceConnection
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            stockService = ((StockPriceService.StockBinder) service).getService();
            connected = true;
        }

        // required methods for ServiceConnection
        // sets connection to false
        @Override
        public void onServiceDisconnected(ComponentName name) {
            stockService = null;
            connected = false;
        }
    };

    Handler updateHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            double newPrice = (double) msg.obj;

            // write to file : msg.arg1 is the index given at the updatePortfolio function
            portfolio.getStockAtIndex(msg.arg1).setPrice(newPrice);

            Context c = MainActivity.this;
            Toast update = Toast.makeText(c, "Updated "
                            + portfolio.getStockAtIndex(msg.arg1).getSymbol()
                            + " to $" + String.valueOf(newPrice),
                    Toast.LENGTH_SHORT);
            update.show();

            return false;
        }
    });

    Handler searchHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            // get quote with ticker symbol from MarkitOnDemand
            JSONObject responseObject = (JSONObject) msg.obj;

            Stock currentStock = null;
            try {
                // adds properties of Stock from JSON object
                currentStock = new Stock(responseObject);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Context c = MainActivity.this;
            Toast searchResults;

            if (currentStock != null) {
                if (listFrag.portfolio.getStocks()
                        .contains(currentStock.getSymbol())) {
                    searchResults = Toast.makeText(c,
                            currentStock.getSymbol() + " "
                                + c.getResources().getString(R.string.already_have_stock),
                            Toast.LENGTH_SHORT);
                } else {
                    searchResults = Toast.makeText(c,
                            c.getResources().getString(R.string.stock_added)
                                    + " " + currentStock.getSymbol(),
                            Toast.LENGTH_SHORT);

                    // needed to show ticker symbol immediately after search
                    listFrag.addStock(currentStock);

                    // write to file
                    portfolio.add(currentStock);
                    addNewStockToFile(currentStock);
                }
            } else {
                searchResults = Toast.makeText(c,
                        c.getResources().getString(R.string.no_search_results),
                        Toast.LENGTH_SHORT);
            }

            searchResults.show();

            return false;
        }
    });

    public void onStart() {
        super.onStart();

        Intent serviceIntent = new Intent(this, StockPriceService.class);
        bindService(serviceIntent, appConnection, Context.BIND_AUTO_CREATE);
        updateHandler.postDelayed(new Runnable(){
            public void run(){
                updatePortfolio();
                updateHandler.postDelayed(this, delay);
            }
        }, delay);
    }

    public void onStop() {
        super.onStop();
        unbindService(appConnection);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creates new Portfolio object for session
        portfolio = new Portfolio();
        // determines whether to show the details fragment
        bothFrags = (findViewById(R.id.deets_fragment) != null);

        listFrag = new StockListFragment();
        detailsFrag = new StockDetailsFragment();

        // make a new File object for session
        File dir = this.getFilesDir();
        // retrieve previous session data
        file = new File(dir, fileName);

        // renews portfolio from file
        if (file.exists()) {
            try {
                // reads ASCII characters saved to file until '\n' or '\0'
                BufferedReader reader = new BufferedReader(new FileReader(file));

                // each line is a ticker symbol
                // added to the Porfolio as a Stock
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        portfolio.add(new Stock(new JSONObject(line.toString())));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bundle bundle = new Bundle();
        bundle.putSerializable(StockListFragment.BUNDLE_KEY, portfolio);
        listFrag.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, listFrag)
                .commit();

        detailsFrag = new StockDetailsFragment();
        if(bothFrags) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.list_fragment, listFrag)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // adds menu items
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_stock_button :
                searchDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void swapFragment() {
        Logger log = Logger.getAnonymousLogger();
        log.info("Fragments swapped");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.list_fragment, detailsFrag)
                .addToBackStack(null)
                .commit();
        getFragmentManager().executePendingTransactions();
    }

    public void stockSelected(Stock stock) {
        if (bothFrags) {
            detailsFrag.displayAll(stock);
            getFragmentManager().executePendingTransactions();
        } else
            swapFragment();

    }

    private void searchDialog() {
        Intent intent = new Intent(this, SearchActivity.class);
        // launches SearchActivity
        startActivityForResult(intent, POPUP_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // confirms this Intent is received from searchDialog()
        if (requestCode == POPUP_ACTIVITY)
            // received some user input
            if (resultCode == RESULT_OK) {

                // this is the user's input
                // MUST MATCH FIRST ARGUMENT OF passIntent.putExtra(String, String)
                newStock = data.getStringExtra("Symbol");

                if (connected)
                    SearchActivity.getNewQuote(newStock, searchHandler);
                handled = true;

                System.out.println("symbol");
            } else if (resultCode == RESULT_CANCELED)
                return;
    }

    // FILE OPERATIONS
    public void addNewStockToFile(Stock stock) {
        try {
            Writer writer;
            writer = new BufferedWriter(new FileWriter(file, true));

            writer.append(stock.getStockAsJSON().toString());
            writer.append('\n');
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePortfolio() {
        Context c = MainActivity.this;
        Toast updates = null;

        if (connected) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;
                int i = 0;
                while ((line = reader.readLine()) != null) {
                    stockService.getQuote(portfolio.getStockAtIndex(i).getSymbol(),
                            i, updateHandler);
                    i++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
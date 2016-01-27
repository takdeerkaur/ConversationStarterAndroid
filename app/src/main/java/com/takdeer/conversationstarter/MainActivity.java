package com.takdeer.conversationstarter;

import android.content.Context;
import android.content.Entity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button topicsButton;
    Button postCategoryButton;
    EditText categoryNameInput;
    ListView mainListView;
    JSONAdapter mJSONAdapter;
    String categoryName;
    Spinner spinner;
    ArrayList<String> categoryArray = new ArrayList<String>();
    ArrayAdapter adapter;

    private static final String QUERY_URL = "http://conversationstarter.elasticbeanstalk.com/topics/?format=json";
    private static final String CATEGORY_POST_URL = "http://conversationstarter.elasticbeanstalk.com/categories/";
    private static final String CATEGORIES_URL = "http://conversationstarter.elasticbeanstalk.com/categories/?format=json";
    private static final String QUERY_BY_CATEGORY_URL = "http://conversationstarter.elasticbeanstalk.com/topics/category/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Access the All Topics Button defined in layout XML
        // and listen for it here
        topicsButton = (Button) findViewById(R.id.topics_button);
        topicsButton.setOnClickListener(this);

//        postCategoryButton = (Button) findViewById(R.id.post_category_button);
//        postCategoryButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                categoryNameInput = (EditText) findViewById(R.id.category_name_input);
//                categoryName = categoryNameInput.getText().toString();
//                postCategory(categoryName);
//            }
//        });

        // Create a JSONAdapter for the ListView
        mJSONAdapter = new JSONAdapter(this, getLayoutInflater());

        // Access the ListView
        mainListView = (ListView) findViewById(R.id.main_listview);

        // Set the ListView to use the ArrayAdapter
        mainListView.setAdapter(mJSONAdapter);

        populateCategories();
        spinner = (Spinner) findViewById(R.id.categories_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                queryTopics(category);
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("hello", "hello");
            }
        });
        // Apply the adapter to the spinner
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categoryArray);
        spinner.setAdapter(adapter);
    }

    private void postCategory(String categoryName) {
        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();
        Context context = this.getApplicationContext();

        // Have the client get a JSONArray of data
        // and define how to respond
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("category_name", categoryName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(context, CATEGORY_POST_URL, entity, "application/json",
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.e("yooo", CATEGORY_POST_URL);
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("Uhoh, it not work", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void queryTopics() {
        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(QUERY_URL,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();
//                        Log.d("yaaay, worked", jsonObject.toString());
//
//                        Iterator x = jsonObject.keys();
//                        JSONArray jsonArray = new JSONArray();
//
//                        while (x.hasNext()){
//                            String key = (String) x.next();
//                            jsonArray.put(jsonObject);
//                        }

                        // update the data in your custom method.
                        mJSONAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("Uhoh, it not work", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    private void queryTopics(String category) {
        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(QUERY_BY_CATEGORY_URL + category + "/?format=json" ,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        // update the data in your custom method.
                        mJSONAdapter.updateData(jsonArray);
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("Uhoh, it not work", statusCode + " " + throwable.getMessage());
                    }
                });
    }

    private void populateCategories() {
        // Create a client to perform networking
        AsyncHttpClient client = new AsyncHttpClient();

        // Have the client get a JSONArray of data
        // and define how to respond
        client.get(CATEGORIES_URL,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        // Display a "Toast" message
                        // to announce your success
                        Toast.makeText(getApplicationContext(), "Success!", Toast.LENGTH_LONG).show();

                        // update the data in your custom method.
//                        categoryAdapter.updateData(jsonArray);
                        // add categories to an array list for Spinner
                        if (jsonArray != null) {
                            for (int i=0;i<jsonArray.length();i++){
                                try {
                                    categoryArray.add(jsonArray.getJSONObject(i).getString("category_name"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        // Display a "Toast" message
                        // to announce the failure
                        Toast.makeText(getApplicationContext(), "Error: " + statusCode + " " + throwable.getMessage(), Toast.LENGTH_LONG).show();

                        // Log error message
                        // to help solve any problems
                        Log.e("Uhoh, it not work", statusCode + " " + throwable.getMessage());
                    }
                });

    }

    @Override
    public void onClick(View v) {
        // On Get Topics button click, show results
        queryTopics();
    }
}

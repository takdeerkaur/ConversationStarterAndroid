package com.takdeer.conversationstarter;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by takdeerkaur on 2016-02-02.
 */
public class Topic {
    JSONArray topicJSONArray = new JSONArray();

    private static final String TOPIC_URL_JSON = "http://conversationstarter.elasticbeanstalk.com/topics/?format=json";
    private static final String TOPIC_URL = "http://conversationstarter.elasticbeanstalk.com/topics/";
    private static final String TOPIC_BY_CATEGORY_URL = "http://conversationstarter.elasticbeanstalk.com/topics/category/";

    public JSONArray getTopics() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TOPIC_URL_JSON,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        topicJSONArray =  jsonArray;
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Log.e("query topics not work", statusCode + " " + throwable.getMessage());
                    }
                });
        return topicJSONArray;
    }

    public JSONArray getTopics(String category) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(TOPIC_BY_CATEGORY_URL + category + "/?format=json" ,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        topicJSONArray =  jsonArray;
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Log.e("topicbycategorynotwork", statusCode + " " + throwable.getMessage());
                    }
                });
        return topicJSONArray;
    }

    public void postTopic(Context context, String topicName, int categoryID) {
        AsyncHttpClient client = new AsyncHttpClient();
//        Context context = this.getApplicationContext();

        // Have the client get a JSONArray of data
        // and define how to respond
        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put("topic_name", topicName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jsonParams.put("category_id", categoryID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        client.post(context, TOPIC_URL, entity, "application/json",
                new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONArray jsonArray) {
                        Log.e("yooo", TOPIC_URL);
                    }
                    @Override
                    public void onFailure(int statusCode, Throwable throwable, JSONObject error) {
                        Log.e("post topic not work", statusCode + " " + throwable.getMessage());
                    }
                });
    }
}

package com.takdeer.conversationstarter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by takdeerkaur on 2016-01-16.
 */
public class JSONAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        // your particular dataset uses String IDs
        // but you have to put something in this method
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.row_topic, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.topicTextView = (TextView) convertView.findViewById(R.id.topic_name);
            holder.categoryTextView = (TextView) convertView.findViewById(R.id.category_name);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        JSONObject jsonObject = (JSONObject) getItem(position);

        // Grab the topic name + category from the JSON
        String topicName = "";
        String categoryName = "placeholder";
        if (jsonObject.has("topic_name")) {
            topicName = jsonObject.optString("topic_name");
        }

        if (jsonObject.has("category")) {
            categoryName = jsonObject.optJSONObject("category").optString("category_name");
        }

        // Send these Strings to the TextViews for display
        holder.topicTextView.setText(topicName);
        holder.categoryTextView.setText(categoryName);

        return convertView;
    }

    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public TextView topicTextView;
        public TextView categoryTextView;
    }

    public void updateData(JSONArray jsonArray) {
        // update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }
}

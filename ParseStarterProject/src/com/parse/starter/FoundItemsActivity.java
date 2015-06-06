package com.parse.starter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FoundItemsActivity extends ListActivity {

    String gameId;
    Date currentTime;
    Date endTime;
    Date startTime;
    String timeRemaining;
    String username;
    String foundItemId;
    FoundItemsAdapter foundItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            gameId = (String) b.get("info6");
            username = (String) b.get("info7");
            System.out.println(gameId);
            System.out.println(username);

        }


        ParseQuery<ParseUser> query = ParseQuery.getQuery("User");
        query.getInBackground(username, new GetCallback<ParseUser>() {
            public void done(ParseUser object, com.parse.ParseException e) {
                if (e == null) {
                    String name = object.getString("username");
                    System.out.println(name);
                    getActionBar().setTitle(name + "'s Found Items");
                }
            }
        });


        foundItemsAdapter = new FoundItemsAdapter(this);
        foundItemsAdapter.loadObjects();
        setListAdapter(foundItemsAdapter);





    }

    protected void onListItemClick (ListView l, View v, int position, long id) {
        ParseObject itemSelection = foundItemsAdapter.getItem(position);
        String item = itemSelection.getObjectId();

        Intent intent = new Intent(FoundItemsActivity.this, ItemPictureActivity.class);
        intent.putExtra("info8", item);
        startActivity(intent);
    }

    private class FoundItemsAdapter extends ParseQueryAdapter<ParseObject> {

        public FoundItemsAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                public ParseQuery<ParseObject> create() {
                    // Here we can configure a ParseQuery to display
                    // only top-rated meals.
                    ParseQuery query = new ParseQuery("ItemsFound");
                    query.whereEqualTo("gameId", gameId);
                    query.whereEqualTo("playerName", username);
                    return query;
                }
            });
        }


        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.activity_found_items, null);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic for
            // populating the main TextView/ImageView.
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            // if it will be populating a TextView or ImageView for you.
            super.getItemView(object, v, parent);

            //String found = Integer.toString(object.getInt("itemsFound"));
            //System.out.println(found);
            //String total = Integer.toString(object.getInt("itemCount"));
            //System.out.println(total);
            //username = object.getString("username");

            Date date = object.getCreatedAt();

            DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
            String timeFound = sdf.format(date);

            // Do additional configuration before returning the View.
            TextView itemView = (TextView) v.findViewById(R.id.text1);
            itemView.setText(object.getString("item"));
            TextView dateView = (TextView) v.findViewById(R.id.text2);
            dateView.setText(timeFound);

            /*
            foundItemId = object.getObjectId();

            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(FoundItemsActivity.this, ItemPictureActivity.class);
                    intent.putExtra("info8", foundItemId);
                    startActivity(intent);
                }
            });

            dateView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(FoundItemsActivity.this, ItemPictureActivity.class);
                    intent.putExtra("info8", foundItemId);
                    startActivity(intent);
                }
            });
            */

            return v;

        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_found_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            startActivity(new Intent(FoundItemsActivity.this, LeaderboardActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

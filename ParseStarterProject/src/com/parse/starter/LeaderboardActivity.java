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
import android.widget.AdapterView;
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

import java.util.Date;
import java.util.List;


public class LeaderboardActivity extends ListActivity {

    String gameId;
    TextView leaderboardTimeRemainingTextView;
    Date currentTime;
    Date endTime;
    Date startTime;
    String timeRemaining;
    String username;
    LeaderboardAdapter leaderboardAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            gameId = (String) b.get("info5");
            //System.out.println(gameId);
        }


        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
        query.getInBackground(gameId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameObject, com.parse.ParseException e) {
                if (e == null) {
                    String gameName = gameObject.getString("gameName");
                    System.out.println(gameName);
                    getActionBar().setTitle(gameName + " Leaderboard");
                }
            }
        });


        leaderboardAdapter = new LeaderboardAdapter(this);
        leaderboardAdapter.loadObjects();
        setListAdapter(leaderboardAdapter);






        leaderboardAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {

            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<ParseObject> parseObjects, Exception e) {


            }

        });


        /*
        leaderboardTimeRemainingTextView = (TextView) this.findViewById(R.id.leaderboard_timeRemaining_textview);

        ParseQuery<ParseObject> gameEndQuery = ParseQuery.getQuery("GameObject");
        gameEndQuery.whereEqualTo("objectId", gameId);
        gameEndQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {


                    currentTime = new Date();
                    System.out.println(currentTime);
                    endTime = object.getDate("gameEndTime");
                    System.out.println(endTime);
                    startTime = object.getDate("gameStartTime");

                    long diff = endTime.getTime() - currentTime.getTime();

                    //long diffSeconds = diff / 1000 % 60;
                    long diffMinutes = diff / (60 * 1000) % 60;
                    long diffHours = diff / (60 * 60 * 1000) % 24;
                    long diffDays = diff / (24 * 60 * 60 * 1000);

                    timeRemaining = "Time Remaining: " + diffDays + " days, " + diffHours + " hours, " + diffMinutes + " minutes.";
                    leaderboardTimeRemainingTextView.setText(timeRemaining);

                }
            }
        });
        */

    }


    protected void onListItemClick (ListView l, View v, int position, long id) {
        ParseObject playerSelection = leaderboardAdapter.getItem(position);
        String game = playerSelection.getString("gamesInvited");
        String player = playerSelection.getString("username");

        Intent intent = new Intent(LeaderboardActivity.this, FoundItemsActivity.class);
        intent.putExtra("info6", game);
        intent.putExtra("info7", player);
        startActivity(intent);
    }

    private class LeaderboardAdapter extends ParseQueryAdapter<ParseObject> {

        public LeaderboardAdapter(Context context) {
            super(context, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                public ParseQuery<ParseObject> create() {
                    // Here we can configure a ParseQuery to display
                    // only top-rated meals.
                    ParseQuery query = new ParseQuery("GamesInvitedObject");
                    query.whereEqualTo("gamesInvited", gameId);
                    query.whereEqualTo("response", "accepted");
                    query.orderByDescending("itemsFound");
                    return query;
                }
            });
        }


        @Override
        public View getItemView(ParseObject object, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.activity_leaderboard, null);
            }

            // Take advantage of ParseQueryAdapter's getItemView logic for
            // populating the main TextView/ImageView.
            // The IDs in your custom layout must match what ParseQueryAdapter expects
            // if it will be populating a TextView or ImageView for you.
            super.getItemView(object, v, parent);

            String found = Integer.toString(object.getInt("itemsFound"));
            //System.out.println(found);
            String total = Integer.toString(object.getInt("itemCount"));
            //System.out.println(total);
            username = object.getString("username");

            // Do additional configuration before returning the View.
            TextView usernameView = (TextView) v.findViewById(R.id.text1);
            usernameView.setText(username);
            TextView itemsFoundView = (TextView) v.findViewById(R.id.text2);
            itemsFoundView.setText(found + " of " + total);

            /*
            usernameView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LeaderboardActivity.this, FoundItemsActivity.class);
                    intent.putExtra("info6", gameId);
                    intent.putExtra("info7", username);
                    startActivity(intent);
                }
            });

            itemsFoundView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(LeaderboardActivity.this, FoundItemsActivity.class);
                    intent.putExtra("info6", gameId);
                    intent.putExtra("info7", username);
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
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            startActivity(new Intent(LeaderboardActivity.this, GamesActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}

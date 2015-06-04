package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;


public class LeaderboardActivity extends Activity {

    String gameId;
    TextView leaderboardTimeRemainingTextView;
    Date currentTime;
    Date endTime;
    Date startTime;
    String timeRemaining;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);
        leaderboardTimeRemainingTextView = (TextView) this.findViewById(R.id.leaderboard_timeRemaining_textview);



        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            gameId = (String) b.get("info5");
            //System.out.println(gameId);
        }

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

        final ArrayAdapter<String> playerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView playerListView = (ListView) this.findViewById(R.id.player_listview);
        playerListView.setAdapter(playerListAdapter);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = (ParseUser)objects.get(i);
                        String name = u.getString("username").toString();
                        playerListAdapter.add(name);
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
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
            startActivity(new Intent(LeaderboardActivity.this, GameDetailActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

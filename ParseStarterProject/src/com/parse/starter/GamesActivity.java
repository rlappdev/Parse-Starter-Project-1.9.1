package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.List;


public class GamesActivity extends Activity {

    ListView gamesListView;
    TextView gamesTextView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ParseUser user = ParseUser.getCurrentUser();
        username = user.getUsername();



        /*
        final ArrayAdapter<String> gamesListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView gamesListView = (ListView) this.findViewById(R.id.games_listview);
        gamesListView.setAdapter(gamesListAdapter);
        ParseQuery query = new ParseQuery("GamesInvitedObject");
        query.whereEqualTo("username", username);
        query.whereEqualTo("response", "accepted");
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    if ((objects == null) || (objects.size() == 0)) {

                        gamesTextView.setText("Sorry, no games to display.");
                    } else {
                        // The query was successful.
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject g = (ParseObject) objects.get(i);
                            String gameName = g.getString("gamesInvitedName").toString();
                            gamesListAdapter.add(gameName);
                        }
                    }

                } else {
                    // Something went wrong.
                }
            }
        });
        */


        final ParseQueryAdapter<ParseObject> adapter =
                new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("GamesInvitedObject");
                        query.whereEqualTo("username", username);
                        query.whereEqualTo("response", "accepted");
                        query.whereNotEqualTo("gameStatus", "over");
                        query.orderByDescending("createdAt");
                        return query;




                    }
                });
        adapter.setTextKey("gamesInvitedName");
        gamesListView = (ListView) this.findViewById(R.id.games_listview);
        gamesTextView = (TextView) this.findViewById(R.id.games_textview);
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {

            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<ParseObject> parseObjects, Exception e) {
                if ((parseObjects == null) || (parseObjects.size() == 0)) {

                    gamesTextView.setText("Sorry, no games to display.");
                }
            }

        });

        gamesListView.setAdapter(adapter);


        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object o = gamesListView.getItemAtPosition(position);
                ParseObject gameSelection = adapter.getItem(position);
                String game = gameSelection.getString("gamesInvited");
                //System.out.println(game);


                Intent intent = new Intent(GamesActivity.this, GameDetailActivity.class);
                intent.putExtra("info", game);
                startActivity(intent);
            }
        });

        /*
        final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, "GameObject");
        adapter.setTextKey("gameName");
        gamesListView = (ListView) this.findViewById(R.id.games_listview);
        gamesListView.setAdapter(adapter);
        gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Object o = gamesListView.getItemAtPosition(position);
                    ParseObject gameSelection = adapter.getItem(position);
                    String game = gameSelection.getObjectId();
                    System.out.println(game);


                    Intent intent = new Intent(GamesActivity.this, GameDetailActivity.class);
                    intent.putExtra("info", game);
                    startActivity(intent);
                }
        });
        */

        /*
        ParseQueryAdapter.QueryFactory<ParseObject> factory =
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        ParseQuery query = new ParseQuery("GameObject");
                        query.include("gameName");
                        query.include("gameCreator");
                        query.include("gameStartTime");
                        query.include("gameEndTime");
                        query.orderByDescending("createdAt");
                        return query;
                    }
                };

        gamesQueryAdapter = new ParseQueryAdapter<ParseObject>(this, factory);
        */

    }







    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            startActivity(new Intent(GamesActivity.this, MainMenuActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

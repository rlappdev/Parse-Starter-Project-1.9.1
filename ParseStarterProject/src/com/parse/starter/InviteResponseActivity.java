package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;


public class InviteResponseActivity extends Activity {

    String gameId;
    private Button acceptButton;
    private Button declineButton;
    String username;
    String creator;
    String gName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_response);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            gameId = (String) b.get("info4");
            System.out.println(gameId);

        }
        ParseUser user = ParseUser.getCurrentUser();
        username = user.getUsername();

        ParseQuery<ParseObject> gameQuery = ParseQuery.getQuery("GameObject");
        gameQuery.whereEqualTo("objectId", gameId);
        gameQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    gName = object.getString("gameName");
                    creator = object.getString("gameCreator");

                    System.out.println(gName);
                    System.out.println(creator);

                } else {
                }
            }
        });

        setupButtonCallbacks();

    }

    private void setupButtonCallbacks() {
        acceptButton = (Button) findViewById(R.id.inviteResponseButton_accept);
        acceptButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // XXX open NewGameActivity
                // Intent i = new Intent(mThisActivity, NewGameActivity.class);
                // mThisActivity.startActivity(i);

                //startActivity(new Intent(MainMenuActivity.this, CreateGameActivity.class));
                //finish();

                /*
                ParseObject gameObject = new ParseObject("GameObject");
                Date date = new Date();
                gameObject.put("gameCreator", "Robert");
                gameObject.put("gameName", "TestGame1");
                gameObject.put("gameStartTime", date);
                gameObject.put("gameEndTime", date);
                gameObject.put("itemsToBeFound", Arrays.asList("laptop", "tablet"));
                gameObject.put("players", Arrays.asList("Robert", "Joe"));
                gameObject.saveInBackground();
                */

            }
        });

        declineButton = (Button) findViewById(R.id.inviteResponseButton_decline);
        declineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // XXX open NewGameActivity
                // Intent i = new Intent(mThisActivity, NewGameActivity.class);
                // mThisActivity.startActivity(i);

                //startActivity(new Intent(MainMenuActivity.this, CreateGameActivity.class));
                //finish();

                /*
                ParseObject gameObject = new ParseObject("GameObject");
                Date date = new Date();
                gameObject.put("gameCreator", "Robert");
                gameObject.put("gameName", "TestGame1");
                gameObject.put("gameStartTime", date);
                gameObject.put("gameEndTime", date);
                gameObject.put("itemsToBeFound", Arrays.asList("laptop", "tablet"));
                gameObject.put("players", Arrays.asList("Robert", "Joe"));
                gameObject.saveInBackground();
                */

                ParseQuery<ParseObject> queryId = ParseQuery.getQuery("GamesInvitedObject");
                queryId.whereEqualTo("gamesInvited", gameId);
                //queryId.whereEqualTo("username", username);
                queryId.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            //object.deleteInBackground();
                            object.put("Declined", "yes");
                            //object.remove("gamesInvited");
                            //object.saveInBackground();
                        } else {
                        }
                        notifyDecline();
                    }
                });
            }
        });
    }

    public void notifyDecline() {
        //push notification to users with the correct game invites
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereEqualTo("objectId", creator);
        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(username + " declined your invite to the following game: " + gName);
        push.sendInBackground();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_response, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_done) {
            startActivity(new Intent(InviteResponseActivity.this, InvitesActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

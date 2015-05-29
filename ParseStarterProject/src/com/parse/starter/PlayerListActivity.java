package com.parse.starter;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.Players;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PlayerListActivity extends Activity {

    private LayoutInflater inflater;
    String gameId;
    String gName;
    String userSelection;
    //String userObjectId;
    String[] names;
    String creator;
    //String[] toppings = new String[1];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_list);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            gameId = (String) b.get("info3");
            //System.out.println(gameId);
        }

        ParseQuery<ParseObject> gameNameQuery = ParseQuery.getQuery("GameObject");
        gameNameQuery.whereEqualTo("objectId", gameId);
        gameNameQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {

                    gName = object.getString("gameName");
                    //System.out.println("gName");
                } else {
                }
            }
        });

        //toppings[0] = "test";

        ParseObject gamePlayers = new ParseObject("GamesJoinedObject");
        ParseUser user = ParseUser.getCurrentUser();
        creator = user.getUsername();
        gamePlayers.put("username", creator);
        //String id = user.getObjectId();
        //gamePlayers.put("userObjectId", id);
        gamePlayers.put("gamesJoined", gameId);
        gamePlayers.saveInBackground();
        //System.out.println(creator);

        //ParseUser user = ParseUser.getCurrentUser();
        //user.addUnique("gameInvites", Arrays.asList(gameId));
        //user.saveInBackground();

        //Display list of users
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

        playerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                userSelection = playerListAdapter.getItem(position);

                //check that userSelection = username of user clicked
                //System.out.println(userSelection);

/*
                //get the user's objectId using the username
                ParseQuery<ParseUser> userInviteQuery = ParseUser.getQuery();
                userInviteQuery.whereEqualTo("username", userSelection);
                userInviteQuery.findInBackground(new FindCallback<ParseUser>() {
                    public void done(List<ParseUser> objects, ParseException e) {
                        if (e == null) {
                            //check size of query; should be 1
                            System.out.println(objects.size());
                            for (int i = 0; i < objects.size(); i++) {
                                ParseUser selectedUser = (ParseUser)objects.get(i);
                                userObjectId = selectedUser.getObjectId();
                                //check that the user's objectId was retrieved
                                System.out.println(userObjectId);
                            }

                        } else {
                        }
                    }
                });
*/
                //Add an invite for the user
                ParseObject playerInvite = new ParseObject("GamesInvitedObject");
                playerInvite.put("username", userSelection);
                //playerInvite.put("userObjectId", userObjectId);
                playerInvite.put("gamesInvited", gameId);
                playerInvite.put("gamesInvitedName", gName);
                playerInvite.saveInBackground();


                final ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
                //query.whereEqualTo("gameId", gameId);
                query.getInBackground(gameId, new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {

                            object.addUnique("players", Arrays.asList(userSelection));
                            object.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(com.parse.ParseException e) {
                                    if (e == null) {
                                        showToast(PlayerListActivity.this, (userSelection + " added to game"));

                                    } else {
                                        showToast(PlayerListActivity.this, getString(R.string.label_addPlayerErrorMessage) + " "
                                                + getString(R.string.label_loginPleaseTryAgainMessage));
                                    }
                                }
                            });


                        } else {
                            // something went wrong
                        }
                    }
                });




            }
        });



    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player_list, menu);
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

            /*
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereEqualTo("channels", "");
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery);
            push.setMessage("You have been added to the following game: " + gameId);
            push.sendInBackground();
            */





            /*
            // Find users invited to the game
            ParseQuery userQuery = ParseUser.getQuery();
            userQuery.whereEqualTo("gameInvites", gameId);
            userQuery.findInBackground(new FindCallback<ParseUser>() {
                public void done(List<ParseUser> objects, ParseException e) {
                    if (e == null) {
                        // The query was successful.
                        for (int i = 0; i < objects.size(); i++) {
                            ParseUser selectedUser = (ParseUser)objects.get(i);
                            System.out.println(selectedUser);

                        }
                    } else {
                        // Something went wrong.
                    }
                }
            });


       //get the objectId of the user so that they can be notified
                ParseQuery<ParseObject> queryId = ParseQuery.getQuery("PlayerObject");
                queryId.whereEqualTo("username", userSelection);
                queryId.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> objects, ParseException e) {
                        if (e == null) {
                            for (int i = 0; i < objects.size(); i++) {
                                ParseUser selectedUser = (ParseUser) objects.get(i);
                                userObjectId = selectedUser.getObjectId();
                                //check that the user's objectId was retrieved
                                System.out.println(userObjectId);
                            }
                        } else {
                        }
                    }
                });


// Find devices associated with these users
            ParseQuery pushQuery = ParseInstallation.getQuery();
            pushQuery.whereMatchesQuery("user", userQuery);

// Send push notification to query
            ParsePush push = new ParsePush();
            push.setQuery(pushQuery); // Set our Installation query
            push.setMessage("You have been added to the following game: " + gameId);
            push.sendInBackground();
            */




            //query GamesInvitedObject for all users invited to the gameId
            ParseQuery<ParseObject> queryId = ParseQuery.getQuery("GamesInvitedObject");
            queryId.whereEqualTo("gamesInvited", gameId);
            queryId.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        int n = objects.size();
                        names = new String[n];
                        for (int i = 0; i < objects.size(); i++) {
                            ParseObject selected = (ParseObject) objects.get(i);
                            String name = selected.getString("username");
                            names[i] = name;
                            //System.out.println(name);

                        }
                    } else {
                    }
                    notifyInvitees();
                }
            });



            startActivity(new Intent(PlayerListActivity.this, GamesActivity.class));
            finish();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    public void notifyInvitees() {
        //push notification to users with the correct game invites
        ParseQuery userQuery = ParseUser.getQuery();
        userQuery.whereContainedIn("username", Arrays.asList(names));
        // Find devices associated with these users
        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereMatchesQuery("user", userQuery);
        // Send push notification to query
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery); // Set our Installation query
        push.setMessage(creator + " has invited you to the following game: " + gName);
        push.sendInBackground();
    }
}

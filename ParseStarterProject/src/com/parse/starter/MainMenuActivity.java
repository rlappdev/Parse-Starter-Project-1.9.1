package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import java.util.Date;
import java.util.Arrays;


import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.ParseObject;

public class MainMenuActivity extends Activity {
    private Button newGameButton;
    //private Button joinGameButton;
    private Button myGamesButton;
    private Button myInvitesButton;

    @SuppressWarnings("unused")
    // This member will be used for actual game play, which is why it's
    // but since no game play code exists yet, it's unused in this activity
    private ParseUser currentUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setupButtonCallbacks();
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("user",ParseUser.getCurrentUser());
        installation.saveInBackground();
    }

    public void onResume() {
        super.onResume();
        currentUser = ParseUser.getCurrentUser();
    }

    /**
     * Setup the Screen callbacks
     */
    private void setupButtonCallbacks() {
        newGameButton = (Button) findViewById(R.id.mainMenuButton_newGame);
        newGameButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // XXX open NewGameActivity
                // Intent i = new Intent(mThisActivity, NewGameActivity.class);
                // mThisActivity.startActivity(i);

                startActivity(new Intent(MainMenuActivity.this, CreateGameActivity.class));
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

        /*
        joinGameButton = (Button) findViewById(R.id.mainMenuButton_joinGame);
        joinGameButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // XXX open JoinGameActivity
                // Intent i = new Intent(mThisActivity, JoinGameActivity.class);
                // mThisActivity.startActivity(i);
            }
        });
        */

        myGamesButton = (Button) findViewById(R.id.mainMenuButton_myGames);
        myGamesButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // XXX open MyGamesActivity
                // Intent i = new Intent(mThisActivity, MyGamesActivity.class);
                // mThisActivity.startActivity(i);
                startActivity(new Intent(MainMenuActivity.this, GamesActivity.class));

            }
        });

        myInvitesButton = (Button) findViewById(R.id.mainMenuButton_myInvites);
        myInvitesButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // XXX open MyGamesActivity
                // Intent i = new Intent(mThisActivity, MyGamesActivity.class);
                // mThisActivity.startActivity(i);
                startActivity(new Intent(MainMenuActivity.this, InvitesActivity.class));

            }
        });

    }

    // /////////////////////////////////////////////////////
    // Menu Handler
    // /////////////////////////////////////////////////////

    /**
     * The create options menu event listener. Invoked at the time to create the
     * menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    /**
     * The options item selected event listener. Invoked when a menu item has
     * been selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
/*
            case R.id.menuitem_prefs:
                // Intent i = new Intent(mThisActivity, PrefsActivity.class);
                // mThisActivity.startActivity(i);
                return true;
*/
            case R.id.menuitem_logout:
                ParseUser.logOut();
                startActivity(new Intent(MainMenuActivity.this, LoginActivity.class));
                finish();
                return true;
            default:
                break;
        }

        return false;
    }

}

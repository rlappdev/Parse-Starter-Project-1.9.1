package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;


public class GameDetailActivity extends Activity {

    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            gameId = (String) b.get("info");
            //System.out.println(gameId);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
        query.getInBackground(gameId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameObject, com.parse.ParseException e) {
                if (e == null) {
                    String gameName = gameObject.getString("gameName");
                    System.out.println(gameName);

                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_detail, menu);
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
}

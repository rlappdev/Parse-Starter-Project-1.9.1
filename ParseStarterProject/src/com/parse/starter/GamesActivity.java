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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;


public class GamesActivity extends Activity {

    ListView gamesListView;
    //private ParseQueryAdapter<ParseObject> gamesQueryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);



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


        setupButtonCallbacks();
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




    private void setupButtonCallbacks() {


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

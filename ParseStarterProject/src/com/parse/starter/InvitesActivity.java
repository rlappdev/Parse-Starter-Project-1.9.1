package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.List;

import static com.parse.ParseQueryAdapter.OnQueryLoadListener;


public class InvitesActivity extends Activity {

    ListView invitesListView;
    TextView invitesTextView;
    String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invites);

        ParseUser user = ParseUser.getCurrentUser();
        username = user.getUsername();

        //final ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, "GamesInvited");
        //adapter.setTextKey("gameName");




        final ParseQueryAdapter<ParseObject> adapter =
                new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("GamesInvitedObject");
                        query.whereEqualTo("username", username);
                        String[] responses = {"declined", "accepted"};
                        query.whereNotContainedIn("response", Arrays.asList(responses));
                        query.orderByDescending("createdAt");
                        return query;




                    }
                });
        adapter.setTextKey("gamesInvitedName");
        invitesListView = (ListView) this.findViewById(R.id.invites_listview);
        invitesTextView = (TextView) this.findViewById(R.id.invites_textview);


        adapter.addOnQueryLoadListener(new OnQueryLoadListener<ParseObject>() {

           @Override
           public void onLoading() {

           }

           @Override
           public void onLoaded(List<ParseObject> parseObjects, Exception e) {
               if ((parseObjects == null) || (parseObjects.size() == 0)) {

                   invitesTextView.setText("Sorry, no invites to display.");
               }
           }

       });

        invitesListView.setAdapter(adapter);

        invitesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Object o = gamesListView.getItemAtPosition(position);
                ParseObject gameSelection = adapter.getItem(position);
                String game = gameSelection.getString("gamesInvited");
                //System.out.println(game);


                Intent intent = new Intent(InvitesActivity.this, InviteResponseActivity.class);
                intent.putExtra("info4", game);
                startActivity(intent);
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
        getMenuInflater().inflate(R.menu.menu_invites, menu);
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
            startActivity(new Intent(InvitesActivity.this, MainMenuActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;


public class TestActivity extends Activity {

    ListView lv;
    Player[] modelItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        lv = (ListView) findViewById(R.id.player_listview);


        modelItems = new Player[10];


        modelItems[0] = new Player("pizza");
        modelItems[1] = new Player("burger");
        modelItems[2] = new Player("olives");
        modelItems[3] = new Player("orange");
        modelItems[4] = new Player("tomato");



        //final ArrayAdapter<String> playerListAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        //ListView playerListView = (ListView) this.findViewById(R.id.player_listview);
        //playerListView.setAdapter(playerListAdapter);
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending("username");

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {

                if (e == null) {
                    // The query was successful.
                    //int arraySize = objects.size();
                    //System.out.println(arraySize);
                    //modelItems = new Player[arraySize];
                    for (int i = 0; i < objects.size(); i++) {
                        ParseUser u = (ParseUser)objects.get(i);
                        String name = u.getString("username").toString();
                        //modelItems[i] = new Player(name);
                        //playerListAdapter.add(name);
                    }

                } else {
                    // Something went wrong.
                }
            }


        });

        CustomAdapter adapter = new CustomAdapter(this, modelItems);
        lv.setAdapter(adapter);

    }



    public class CustomAdapter extends ArrayAdapter<Player> {
        Player[] modelItems = null;
        Context context;
        public CustomAdapter(Context context, Player[] resource) {
            super(context,R.layout.simple_list_item_1,resource);
            // TODO Auto-generated constructor stub
            this.context = context;
            this.modelItems = resource;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.simple_list_item_1, parent, false);
            TextView name = (TextView) convertView.findViewById(R.id.text1);
            CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
            name.setText(modelItems[position].getName());

            /*
            if(modelItems[position].getValue() == 1)
                cb.setChecked(true);
            else
            */
                cb.setChecked(false);
            return convertView;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test, menu);
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

package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.GetCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.util.Arrays;


public class AddItemsActivity extends Activity {

    private EditText itemNameEditText;
    private ImageButton addItemButton;
    private String itemName;
    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_items);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            gameId = (String) b.get("info");
            //System.out.println(gameName);
        }
        setupButtonCallbacks();
    }

    private void setupButtonCallbacks() {
        addItemButton = (ImageButton) findViewById(R.id.button_addItem);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                itemNameEditText = (EditText) findViewById(R.id.textbox_itemName);
                itemName = itemNameEditText.getText().toString();

                if (itemName == null || itemName.length() == 0) {
                    showToast(AddItemsActivity.this, getString(R.string.hint_itemName));
                } else {

                /*
                //Add items to GameObject gameId as an array (doesn't work -- array is null)
                ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
                query.getInBackground(gameId, new GetCallback<ParseObject>() {
                    public void done(ParseObject gameObject, com.parse.ParseException e) {
                        if (e == null) {
                            gameObject.put("items", Arrays.asList(itemName));
                            gameObject.saveInBackground();
                        }
                    }
                });
                */


                    ParseObject itemObject = new ParseObject("ItemObject");
                    itemObject.put("item", itemName);
                    itemObject.put("gameId", gameId);
                    itemObject.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            if (e == null) {
                                loadItemsList();
                            } else {
                                showToast(AddItemsActivity.this, getString(R.string.label_addItemErrorMessage) + " "
                                        + getString(R.string.label_loginPleaseTryAgainMessage));
                            }
                        }
                    });

                }

            }

        });

    }

    public void loadItemsList() {
            ParseQueryAdapter<ParseObject> adapter =
                new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("ItemObject");
                        query.whereEqualTo("gameId", gameId);
                        return query;
                    }
                });


            adapter.setTextKey("item");
            ListView itemsListView = (ListView) this.findViewById(R.id.items_listview);
            itemsListView.setAdapter(adapter);
            itemNameEditText.setText("");
            itemNameEditText.requestFocus();
            itemName = null;



    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_next:
                //startActivity(new Intent(AddItemsActivity.this, AddPlayersActivity.class));
                ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemObject");
                query.whereEqualTo("gameId", gameId);
                query.countInBackground(new CountCallback() {
                    public void done(final int count, com.parse.ParseException e) {
                        if (e == null) {
                            //final int itemCount = count;
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
                            query.getInBackground(gameId, new GetCallback<ParseObject>() {
                                public void done(ParseObject gameObject, com.parse.ParseException e) {
                                    if (e == null) {
                                        gameObject.put("itemCount", count);
                                        gameObject.saveInBackground();
                                    }
                                }
                            });
                        } else {
                            // The request failed
                        }
                    }
                });
                Intent intent = new Intent(AddItemsActivity.this, PlayerListActivity.class);
                intent.putExtra("info3", gameId);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return false;
    }

}

package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ItemPictureActivity extends Activity {

    String itemId;
    ParseImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_picture);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            itemId = (String) b.get("info8");
            System.out.println(itemId);
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ItemsFound");
        query.getInBackground(itemId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    String item = object.getString("item");
                    System.out.println(item);
                    getActionBar().setTitle(item + " Picture");
                    ParseFile itemPicture = (ParseFile)object.get("image");
                    imageView = (ParseImageView) findViewById(R.id.imageView1);
                    imageView.setParseFile(itemPicture);
                    imageView.loadInBackground(new GetDataCallback() {
                        public void done(byte[] data, ParseException e) {
                            if (e == null) {
                                // data has the bytes for the resume


                            } else {
                                // something went wrong
                            }
                        }
                    });
                }
            }
        });






    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_item_picture, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_done) {
            startActivity(new Intent(ItemPictureActivity.this, FoundItemsActivity.class));
            finish();
            return true;

        }
        */

        return super.onOptionsItemSelected(item);
    }
}

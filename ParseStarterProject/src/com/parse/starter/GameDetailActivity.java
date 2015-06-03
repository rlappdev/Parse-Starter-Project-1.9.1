package com.parse.starter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class GameDetailActivity extends Activity {

    String item;
    String gameId;
    String gName;
    ListView gameDetailsItemsListView;
    TextView gameDetailsTextView;
    int iCount;
    String playerName;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ParseFile parseImageFile;
    int foundCount;
    String[] names;
    String winnerName;

    public GameDetailActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_detail);
        ParseUser user = ParseUser.getCurrentUser();
        playerName = user.getUsername();
        gameDetailsTextView = (TextView) this.findViewById(R.id.gameDetails_textview);


        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if (b != null) {
            gameId = (String) b.get("info");
            //System.out.println(gameId);
        }


        ParseQuery<ParseObject> gameWonQuery = ParseQuery.getQuery("GamesInvitedObject");
        gameWonQuery.whereEqualTo("gamesInvited", gameId);
        gameWonQuery.whereEqualTo("winner", "winner");
        gameWonQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    loadGameDetails();
                } else {
                    winnerName = object.getString("username");
                    updateGameStatus();
                }
            }
        });

    }

    private void updateGameStatus() {
        ParseQuery<ParseObject> statusQuery = ParseQuery.getQuery("GamesInvitedObject");
        statusQuery.whereEqualTo("gamesInvited", gameId);
        statusQuery.whereEqualTo("username", playerName);
        statusQuery.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    object.put("gameStatus", "over");
                    object.saveInBackground();
                    gameDetailsTextView.setText("Game over. " + winnerName + " has won!");

                }

            }
        });
    }

    private void loadGameDetails() {

        ParseQuery<ParseObject> gameNameQuery = ParseQuery.getQuery("GameObject");
        gameNameQuery.whereEqualTo("objectId", gameId);
        gameNameQuery.getFirstInBackground(new GetCallback<ParseObject>() {
           public void done(ParseObject object, ParseException e) {
               if (e == null) {
                   gName = object.getString("gameName");
                   iCount = object.getInt("itemCount");
               } else {
               }
           }
        });

        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
        query.getInBackground(gameId, new GetCallback<ParseObject>() {
            public void done(ParseObject gameObject, com.parse.ParseException e) {
                if (e == null) {
                    String gameName = gameObject.getString("gameName");
                    System.out.println(gameName);
                    getActionBar().setTitle(gameName + " Details");
                }
            }
        });

        final ParseQueryAdapter<ParseObject> adapter =
                new ParseQueryAdapter<ParseObject>(this, new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("ItemObject");
                        query.whereEqualTo("gameId", gameId);
                        query.orderByDescending("createdAt");
                        return query;




                    }
                });
        adapter.setTextKey("item");
        gameDetailsItemsListView = (ListView) this.findViewById(R.id.gameDetailItems_listview);
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {

            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(List<ParseObject> parseObjects, Exception e) {
                if ((parseObjects == null) || (parseObjects.size() == 0)) {

                    gameDetailsTextView.setText("Sorry, no items were added to the game.");
                }
            }

        });

        gameDetailsItemsListView.setAdapter(adapter);
        gameDetailsItemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ParseObject itemSelection = adapter.getItem(position);
                item = itemSelection.getString("item");
                System.out.println(item);

                ParseQuery<ParseObject> itemQuery = ParseQuery.getQuery("ItemsFound");
                itemQuery.whereEqualTo("gameId", gameId);
                itemQuery.whereEqualTo("playerName", playerName);
                itemQuery.whereEqualTo("item", item);
                itemQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            dispatchTakePictureIntent();
                        }
                        else {
                            showToast(GameDetailActivity.this, "You have already submitted this item as found.");
                        }


                    }
                });






                //ParseQuery<ParseObject> foundCountQuery = ParseQuery.getQuery("ItemsFound");
                //foundCountQuery.whereEqualTo()








            }
        });




    }





    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //byte[] test = timestampItAndSave(imageBitmap);
            //parseImageFile = new ParseFile("item_pic.png", test);
            ByteArrayOutputStream blob = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
            byte[] imgArray = blob.toByteArray();
            parseImageFile = new ParseFile("item_pic.png", imgArray);



            ParseObject addFoundItem = new ParseObject("ItemsFound");
            addFoundItem.put("item", item);
            addFoundItem.put("itemCount", iCount);
            addFoundItem.put("gameId", gameId);
            addFoundItem.put("gameName", gName);
            addFoundItem.put("playerName", playerName);
            addFoundItem.put("image", parseImageFile);
            addFoundItem.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        updateFoundItems();
                    }
                    //checkForWin();
                }
            });





        }
    }

    private void updateFoundItems(){
        ParseQuery<ParseObject> itemFoundCountQuery = ParseQuery.getQuery("ItemsFound");
        itemFoundCountQuery.whereEqualTo("gameId", gameId);
        itemFoundCountQuery.whereEqualTo("playerName", playerName);
        itemFoundCountQuery.countInBackground(new CountCallback() {
            public void done(final int count, com.parse.ParseException e) {
                if (e == null) {
                    foundCount = count;
                    //System.out.println(foundCount);
                    ParseQuery<ParseObject> itemFoundQuery = ParseQuery.getQuery("GamesInvitedObject");
                    itemFoundQuery.whereEqualTo("gamesInvited", gameId);
                    itemFoundQuery.whereEqualTo("username", playerName);
                    itemFoundQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.put("itemsFound", foundCount);
                                if (iCount == foundCount){
                                    object.put("winner", "winner");
                                    winAnnouncement();
                                }
                                object.saveInBackground();
                            }

                        }
                    });
                }
            }

        });
    }


    private void winAnnouncement(){

        ParseQuery<ParseObject> queryId = ParseQuery.getQuery("GamesInvitedObject");
        queryId.whereEqualTo("gamesInvited", gameId);
        queryId.whereEqualTo("response", "accepted");
        queryId.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    int n = objects.size();
                    names = new String[n];
                    for (int i = 0; i < objects.size(); i++) {
                        final ParseObject selected = (ParseObject) objects.get(i);
                        String name = selected.getString("username");
                        names[i] = name;
                        //System.out.println(name);
                        ParseQuery userQuery = ParseUser.getQuery();
                        userQuery.whereEqualTo("username", name);
                        userQuery.getFirstInBackground(new GetCallback<ParseUser>() {
                            public void done(ParseUser object, ParseException e) {
                                if (e == null) {
                                    String user = object.getObjectId();
                                    ParseACL inviteACL = new ParseACL();
                                    inviteACL.setPublicReadAccess(true);
                                    inviteACL.setWriteAccess(user, true);
                                    selected.setACL(inviteACL);
                                    selected.saveInBackground();

                                }
                            }
                        });




                    }
                } else {
                }
                //push notification to users with the correct game invites
                ParseQuery userQuery = ParseUser.getQuery();
                userQuery.whereContainedIn("username", Arrays.asList(names));
                // Find devices associated with these users
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereMatchesQuery("user", userQuery);
                // Send push notification to query
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage(playerName + " has won game: " + gName);
                push.sendInBackground();
            }
        });


    }

    /*
    private void checkForWin(){
        if (iCount == foundCount) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("GameObject");
            query.getInBackground(gameId, new GetCallback<ParseObject>() {
                public void done(ParseObject gameObject, com.parse.ParseException e) {
                    if (e == null) {
                        gameObject.put("winner", playerName);
                        gameObject.saveInBackground();
                        System.out.println("win?");

                    }
                }
            });
        }
    }
    */

    /*
    private byte[] timestampItAndSave(Bitmap toEdit){
        Bitmap dest = Bitmap.createBitmap(toEdit.getWidth(), toEdit.getHeight(), Bitmap.Config.ARGB_8888);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(Calendar.getInstance().getTime()); // reading local time in the system

        Canvas cs = new Canvas(dest);
        Paint tPaint = new Paint();
        tPaint.setTextSize(10);
        tPaint.setColor(Color.BLUE);
        tPaint.setStyle(Paint.Style.FILL);
        float height = tPaint.measureText("yY");
        cs.drawText(dateTime, 20f, height+15f, tPaint);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        dest.compress(Bitmap.CompressFormat.PNG, 0, blob);
        //imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, blob);
        byte[] imgArray = blob.toByteArray();


        return imgArray;
    }
*/
    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
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
        if (id == R.id.action_done) {
            startActivity(new Intent(GameDetailActivity.this, GamesActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.parse.starter;

    import android.app.Activity;
    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.view.Gravity;
    import android.view.Menu;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageButton;
    import android.widget.ListView;
    import android.widget.Toast;

    import com.google.android.gms.games.Player;
    import com.parse.ParseObject;
    import com.parse.ParseQueryAdapter;
    import com.parse.SaveCallback;

    import java.text.ParseException;


public class AddPlayersActivity extends Activity {

    private Button addPlayerButton;
    String gameId;
    //String game = getIntent().getStringExtra("data");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        if(b!=null) {
            gameId = (String) b.get("info2");
            System.out.println(gameId);
        }
        loadPlayerList();
        setupButtonCallbacks();
    }

    private void setupButtonCallbacks() {
        addPlayerButton = (Button) findViewById(R.id.button_addPlayer);
        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //can be temporarily changed to testactivity (should be playerlistactivity)
                Intent intent = new Intent(AddPlayersActivity.this, PlayerListActivity.class);
                intent.putExtra("info3", gameId);
                startActivity(intent);
            }

        });

    }

    public void loadPlayerList() {
        ParseQueryAdapter<ParseObject> adapter = new ParseQueryAdapter<ParseObject>(this, "PlayerObject");
        adapter.setTextKey("player");
        ListView playerListView = (ListView) this.findViewById(R.id.player_listview);
        playerListView.setAdapter(adapter);

    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_players, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                startActivity(new Intent(AddPlayersActivity.this, GamesActivity.class));
                return true;
            default:
                break;
        }
        return false;
    }


}

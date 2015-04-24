package com.parse.starter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;


public class CreateGameActivity extends Activity {

    private Button pickStartDateButton;
    private Button pickEndDateButton;
    private EditText gamenameEditText;
    private String gamename;
    private String gameStartTime;
    private String gameEndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);
        setupButtonCallbacks();
    }

    public class StartDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date;

            date = day + "/" + (month + 1) + "/" + year;
            pickStartDateButton.setText(date);

        }
    }

    public class EndDatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            String date;

            date = day + "/" + (month + 1) + "/" + year;
            pickEndDateButton.setText(date);

        }
    }



    private void setupButtonCallbacks() {
        pickStartDateButton = (Button) findViewById(R.id.button_pick_startDate);
        pickStartDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              DialogFragment newFragment = new StartDatePickerFragment();
              newFragment.show(getFragmentManager(), "datePicker");



            }
        });

        pickEndDateButton = (Button) findViewById(R.id.button_pick_endDate);
        pickEndDateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DialogFragment newFragment = new EndDatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");



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
        getMenuInflater().inflate(R.menu.menu_create_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_cancel:
                startActivity(new Intent(CreateGameActivity.this, MainMenuActivity.class));
                finish();
                return true;
            case R.id.action_next:
                test();
                return true;
            default:
                break;
        }
        return false;
    }


    /*
    private void getGameInfo() {
        gamenameEditText = (EditText) findViewById(R.id.textbox_gameName);
        gamename = gamenameEditText.getText().toString();
        // game name validation -- need to make sure this is unique
        if (gamename == null || gamename.length() == 0) {
            showToast(CreateGameActivity.this, getString(R.string.hint_username));
            return;
        }

        ParseUser currentUser = ParseUser.getCurrentUser();

        gameStartTime = pickStartDateButton.getText().toString();
        gameEndTime = pickEndDateButton.getText().toString();

        ParseObject gameObject = new ParseObject("GameObject");
        Date date = new Date();
        gameObject.put("gameCreator", currentUser);
        gameObject.put("gameName", gamename);
        gameObject.put("gameStartTime", gameStartTime);
        gameObject.put("gameEndTime", gameEndTime);
        gameObject.saveInBackground();

    }
    */

    private void test() {

        gamenameEditText = (EditText) findViewById(R.id.textbox_gameName);
        gamename = gamenameEditText.getText().toString();
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject gameObject = new ParseObject("GameObject");
        gameObject.put("gameCreator", currentUser);
        gameObject.put("gameName", gamename);
        gameObject.saveInBackground();
    }

}

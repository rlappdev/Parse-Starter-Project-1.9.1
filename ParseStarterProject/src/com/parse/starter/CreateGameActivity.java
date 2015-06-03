package com.parse.starter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;


import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class CreateGameActivity extends Activity {

    private Button pickStartDateButton;
    private Button pickEndDateButton;
    private EditText gamenameEditText;
    private String gamename;
    private String username;
    private String gameStartTime;
    private String gameEndTime;
    ParseObject gameObject;
    String data;

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
            gameStartTime = day + "/" + (month + 1) + "/" + year;
            DialogFragment newFragment = new StartTimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");

            //pickStartDateButton.setText(gameStartTime);

        }
    }

    public class StartTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String sHour = "00";
            if(hourOfDay < 10){
                sHour = "0"+hourOfDay;
            } else {
                sHour = String.valueOf(hourOfDay);
            }

            String sMinute = "00";
            if(minute < 10){
                sMinute = "0"+minute;
            } else {
                sMinute = String.valueOf(minute);
            }

            gameStartTime = gameStartTime + " at " + sHour + ":" + sMinute;
            pickStartDateButton.setText(gameStartTime);
        }
    }

    public class EndTimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            String sHour = "00";
            if(hourOfDay < 10){
                sHour = "0"+hourOfDay;
            } else {
                sHour = String.valueOf(hourOfDay);
            }

            String sMinute = "00";
            if(minute < 10){
                sMinute = "0"+minute;
            } else {
                sMinute = String.valueOf(minute);
            }

            gameEndTime = gameEndTime + " at " + sHour + ":" + sMinute;
            pickEndDateButton.setText(gameEndTime);

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
            gameEndTime = day + "/" + (month + 1) + "/" + year;

            DialogFragment newFragment = new EndTimePickerFragment();
            newFragment.show(getFragmentManager(), "timePicker");

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
            case R.id.action_next:
                gamenameEditText = (EditText) findViewById(R.id.textbox_gameName);
                gamename = gamenameEditText.getText().toString();
                //pickStartDateButton = (Button) findViewById(R.id.button_pick_startDate);
                //gameStartTime = pickStartDateButton.getText().toString();
                //pickEndDateButton = (Button) findViewById(R.id.button_pick_endDate);
                //gameEndTime = pickEndDateButton.getText().toString();
                //DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                //Date date = new Date();


                //System.out.println(startDate);
                //gameStartTime = formatter.parse(date);
                //DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);;



                if (gamename == null || gamename.length() == 0) {
                    showToast(CreateGameActivity.this, getString(R.string.hint_gameName));
                    break;
                } else if (gameStartTime == null || gameStartTime.length() == 0) {
                    showToast(CreateGameActivity.this, getString(R.string.hint_startTime));
                    break;
                } else if (gameEndTime == null || gameEndTime.length() == 0) {
                    showToast(CreateGameActivity.this, getString(R.string.hint_endTime));
                    break;
                }

                Date startDate = new Date();
                Date endDate = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm");
                //formatter = new SimpleDateFormat("dd-mm-yyyy HHmm", Locale.US);
                //formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                try {
                    startDate = formatter.parse(gameStartTime);
                    endDate = formatter.parse(gameEndTime);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ParseUser currentUser = ParseUser.getCurrentUser();
                username = currentUser.getUsername();
                String currentUserObjectId = currentUser.getObjectId();
                String currentUserName = currentUser.getUsername();
                gameObject = new ParseObject("GameObject");
                gameObject.put("gameCreator", currentUserObjectId);
                gameObject.put("gameCreatorName", currentUserName);
                gameObject.put("gameName", gamename);
                //gameObject.put("gameStartTime", date);
                gameObject.put("gameStartTime", startDate);
                gameObject.put("gameEndTime", endDate);
                gameObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(com.parse.ParseException e) {
                        if (e == null) {
                            data = gameObject.getObjectId();
                            //data = gameObject.getString("gameName");
                            //System.out.println(data);
                            Intent intent = new Intent(CreateGameActivity.this, AddItemsActivity.class);
                            intent.putExtra("info", data);
                            //intent.putExtra("info2", "test2");
                            startActivity(intent);
                        } else {
                            showToast(CreateGameActivity.this, getString(R.string.label_createGameErrorMessage) + " "
                                    + getString(R.string.label_loginPleaseTryAgainMessage));
                        }


                    }
                });
                return true;

            default:
                break;
        }
        return false;
    }



}

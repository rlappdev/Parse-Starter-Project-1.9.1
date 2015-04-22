package com.parse.starter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.List;


public class RegistrationActivity extends Activity {

    private static final String TAG = "Registration";

    private Button continueButton;
    private Button cancelButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText emailEditText;

    private String username;
    private String password;
    private String email;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        setupButtonCallbacks();
    }

    public void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get currentUser from ParseUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null && currentUser.getObjectId() != null) {
            username = currentUser.getUsername();
            usernameEditText.setText(username);
            email = currentUser.getEmail();
            emailEditText.setText(email);
            // If active currentUser, no need to login, go to main menu
            startActivity(new Intent(this, MainMenuActivity.class));
            finish();
        }

    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    private void setupButtonCallbacks() {
        usernameEditText = (EditText) findViewById(R.id.textbox_loginUsername);
        passwordEditText = (EditText) findViewById(R.id.textbox_loginPassword);
        emailEditText = (EditText) findViewById(R.id.textbox_loginEmail);

        continueButton = (Button) findViewById(R.id.loginbutton_continue);
        continueButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();
                email = emailEditText.getText().toString();
                // username validation
                if (username == null || username.length() == 0) {
                    showToast(RegistrationActivity.this, getString(R.string.hint_username));
                    return;
                }
                // password validation
                if (password == null || password.length() == 0) {
                    showToast(RegistrationActivity.this, getString(R.string.hint_password));
                    return;
                }

                progressDialog = ProgressDialog.show(RegistrationActivity.this,
                        getString(R.string.label_login_please_wait),
                        getString(R.string.label_query_in_progress) + " '"
                                + username + "'");

                //Ensure username and/or email are not already in use
                List<ParseQuery<ParseUser>> parseUserQueryList = new ArrayList<ParseQuery<ParseUser>>();

                ParseQuery<ParseUser> parseUsernameQuery = ParseUser.getQuery();

                parseUsernameQuery.whereEqualTo("username", username);
                parseUserQueryList.add(parseUsernameQuery);

                ParseQuery parseEmailQuery = ParseUser.getQuery();
                parseEmailQuery.whereEqualTo("email", email);
                parseUserQueryList.add(parseEmailQuery);
                // use a compound query to check username and email
                ParseQuery parseUserQuery = ParseQuery.or(parseUserQueryList);

                parseUserQuery.findInBackground(new FindCallback<ParseObject>() {

                    public void done(List<ParseObject> arg0, ParseException arg1) {

                        dismissProgressDialog();

                        if (arg1 == null) {
                            // If the arg0 list has more than one entry
                            if (arg0 != null && arg0.size() > 0) {
                                // Get the first one (should be only one)
                                ParseUser user = (ParseUser) arg0.get(0);

                                if (username != null) {
                                    String existingUsername = user.getUsername();
                                    if (username.equals(existingUsername)) {
                                        // clear the username field
                                        usernameEditText.setText("");
                                        usernameEditText.requestFocus();
                                        // set the username to null
                                        username = null;
                                        // Tell user to choose new username
                                        showToast(RegistrationActivity.this, getString(R.string.label_loginUsernameAlreadyExists));
                                        return;
                                    }
                                }
                                if (email != null) {
                                    String existingEmail = user.getEmail();
                                    if (email.equals(existingEmail)) {
                                        // clear the email field
                                        emailEditText.setText("");
                                        emailEditText.requestFocus();
                                        // set the email to null
                                        email = null;
                                        // Tell user to choose new email
                                        showToast(RegistrationActivity.this, getString(R.string.label_loginEmailAlreadyExists));
                                        return;
                                    }
                                }


                            } else
                                doSignUp();

                        } else
                            showToast(RegistrationActivity.this, getString(R.string.label_signupErrorMessage) + " "
                                    + getString(R.string.label_loginPleaseTryAgainMessage));
                    }


                });
            }
        });

        cancelButton = (Button) findViewById(R.id.loginbutton_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Go back to LoginActivity
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    private final SignUpCallback signinCallback = new SignUpCallback() {
        @Override
        public void done(ParseException arg0) {

            dismissProgressDialog();

            if (arg0 == null) {
                Log.d(TAG + ".doSignUp",
                        "Success!  User account created for username="
                                + RegistrationActivity.this.username);
                startActivity(new Intent(RegistrationActivity.this,
                        MainMenuActivity.class));
                finish();
            } else {
                // Show an error message
                showToast(RegistrationActivity.this, getString(R.string.label_signupErrorMessage) + " "
                        + getString(R.string.label_loginPleaseTryAgainMessage));
            }
        }
    };

    private void doSignUp() {
        progressDialog = ProgressDialog.show(RegistrationActivity.this,
                getString(R.string.label_login_please_wait),
                getString(R.string.label_signup_in_progress));

        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        user.signUpInBackground(signinCallback);
    }




}

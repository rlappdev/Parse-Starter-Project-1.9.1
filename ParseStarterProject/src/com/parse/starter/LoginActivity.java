package com.parse.starter;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;

import com.parse.ParseUser;



public class LoginActivity extends Activity {

    private static final String TAG = "Login";

    private Button loginButton;
    private Button registerButton;
    private EditText usernameEditText;
    private EditText passwordEditText;


    private String username;
    private String password;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeParse();
        setupButtonCallbacks();
    }

    public void initializeParse() {
        try {
            synchronized (this) {
                Log.i(TAG, "Parse.initialize");
                Parse.initialize(this, getString(R.string.parse_app_id),
                        getString(R.string.parse_client_id));
                ParseUser.enableAutomaticUser();
                ParseACL defaultACL = new ParseACL();
                defaultACL.setPublicReadAccess(true);
                ParseACL.setDefaultACL(defaultACL, true);
                Log.i(TAG, "Parse.initialize - done");
            }
        } catch (Exception ex) {
            Log.e(TAG + "." + "initializeParse", ex.getMessage());
        }
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
        // Login button handler
        loginButton = (Button) findViewById(R.id.loginbutton_login);
        loginButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                username = usernameEditText.getText().toString();
                password = passwordEditText.getText().toString();

                // email validation
                if (username == null || username.length() == 0) {
                    showToast(LoginActivity.this, getString(R.string.hint_username));
                    return;
                }
                // password validation
                if (password == null || password.length() == 0) {
                    showToast(LoginActivity.this, getString(R.string.hint_password));
                    return;
                }

                progressDialog = ProgressDialog.show(LoginActivity.this,
                        getString(R.string.label_login_please_wait),
                        getString(R.string.label_query_in_progress) + " '"
                                + username + "'");


                doLogin();



            }
        });

        // Register button handler
        registerButton = (Button) findViewById(R.id.loginbutton_register);
        registerButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Go to RegistrationActivity
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
                finish();
            }
        });
    }

    private final LogInCallback loginCallback = new LogInCallback() {
        @Override
        public void done(ParseUser arg0, ParseException arg1) {

            dismissProgressDialog();

            if (arg0 != null) {
                Log.d(TAG + ".doParseLogin",
                        "Success!  Current User ObjectId: "
                                + arg0.getObjectId());
                startActivity(new Intent(LoginActivity.this,
                        MainMenuActivity.class));
                finish();
            } else {
                // Notify user that login failed and ask to try again
                Log.d(TAG + ".doParseLogin", "Failed", arg1);
                showToast(LoginActivity.this, getString(R.string.label_loginErrorMessage) + " "
                        + arg1.getMessage() + ".  "
                        + getString(R.string.label_loginPleaseTryAgainMessage));
                dismissProgressDialog();
            }
        }
    };

    private void doLogin() {

        dismissProgressDialog();

        progressDialog = ProgressDialog.show(LoginActivity.this,
                getString(R.string.label_login_please_wait),
                getString(R.string.label_login_in_progress) + " '" + username
                        + "'");

        // Login with username and password given loginCallback
        ParseUser.logInInBackground(username, password, loginCallback);
    }




}

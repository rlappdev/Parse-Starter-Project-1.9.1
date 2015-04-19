package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.Date;

import android.util.Log;


public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Initialize Crash Reporting.
    ParseCrashReporting.enable(this);

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    // Add your initialization code here
    Parse.initialize(this, "h7oOnFdKOMhFsQUU4RJpOG9c8R1xNsPbCJYQmFcN", "6ihlAKU2WhYEWonFzDcnPbRYAK8HqCSy4lzEwXg9");


    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);

    ParseObject testObject = new ParseObject("TestObject");
    testObject.put("foo", "bar");
    testObject.saveInBackground();

      ParseObject newObject = new ParseObject("NewObject");
      Date date = new Date();
      //long currentTime = System.currentTimeMillis();
      //timeObject.put("currentTime", currentTime);
      newObject.put("currentTime", date);
      newObject.saveInBackground();

      ParseQuery<ParseObject> query = ParseQuery.getQuery("NewObject");
      query.getInBackground("8zogptAsvv", new GetCallback<ParseObject>() {
          public void done(ParseObject object, ParseException e) {
              String TAG = "Task1";
              Date date1 = object.getDate("currentTime");
              if (e == null) {
                  // object will be your game score
                  Log.i(TAG, "index=" + date1);
              } else {
                  // something went wrong
                  Log.e(TAG, "index=" + "Unknown error");
              }
          }
      });

  }




}

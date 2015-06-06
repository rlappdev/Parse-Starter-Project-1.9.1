package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by robertleung on 2015-06-04.
 */
@ParseClassName("GamesInvitedObject")
public class Leaderboard extends ParseObject {

    public Leaderboard(){
        // A default constructor is required.
    }

    public String getUsername() {
        return getString("username");
    }

    public int getItemsFound() {
        return getInt("itemsFound");
    }

}

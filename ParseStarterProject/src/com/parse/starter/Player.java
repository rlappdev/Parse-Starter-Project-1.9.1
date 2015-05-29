package com.parse.starter;

/**
 * Created by robertleung on 2015-05-24.
 */
public class Player {

    String name;
    int value; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */

    Player(String name){
        this.name = name;
        this.value = 0;
    }
    public String getName(){
        return this.name;
    }
    public int getValue(){
        return this.value;
    }

    /*
    String code = null;
    String name = null;
    boolean selected = false;

    public Player(String code, String name, boolean selected) {
        super();
        this.code = code;
        this.name = name;
        this.selected = selected;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    */
}

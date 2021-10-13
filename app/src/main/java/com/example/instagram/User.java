package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    public static final String KEY_RANDOM = "random";

    public String getRandom(){
        return getString(KEY_RANDOM);
    }
    public void setRandom(String random){
        put(KEY_RANDOM, random);
    }
}

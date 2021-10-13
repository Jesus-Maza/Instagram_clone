package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.parceler.Parcel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@ParseClassName("Post")

public class Post extends ParseObject {
    public static final String KEY_DESCRIPTION = "Description";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_USER = "User";
    public static final String KEY_DATE = "createdAt";
    public static final String KEY_PROFILE_PICTURE = "Profile_picture";

    public Post(){

    }

    public  ParseFile getProfile_picture(){
        return getParseUser(KEY_USER).getParseFile(KEY_PROFILE_PICTURE);
    }

    public void setProfile_picture(ParseFile parseFile){
        getParseUser(KEY_USER).put(KEY_PROFILE_PICTURE, parseFile);
    }


    public String getDate(){
        Date date = getCreatedAt();
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String reportDate = df.format(date);;
        return reportDate;
    }

    public void setDate(String date){
        put(KEY_DATE,date);
    }

    public String getDescription(){
        return getString(KEY_DESCRIPTION);
    }


    public void setDescription(String description){
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage(){
        return getParseFile(KEY_IMAGE);
    }

    public void setImage(ParseFile parseFile){
        put(KEY_IMAGE, parseFile);
    }


    public ParseUser getUser(){
        return getParseUser(KEY_USER);
    }

    public void setUser (ParseUser user){
        put(KEY_USER, user);
    }
}

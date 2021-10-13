package com.example.instagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Comments")
public class Comments extends ParseObject {
    public static final String KEY_COMMENT = "Comment";
    public static final String KEY_POST = "Belong_To";

    public String getComment(){
        return getString(KEY_POST);
    }
    public void setString(String comment){
        put(KEY_COMMENT, comment);
    }
}

package com.example.instagram;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comments.class);
        ParseObject.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("2o4XgA4g4OUe3RW31dKDmpUkm03fPeWc2piQrwFD")
                .clientKey("8KGUxA6AeYOXtwkVtYIdcsvXwOWrTj4TqVefRmS7")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}

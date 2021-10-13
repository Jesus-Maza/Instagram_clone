package com.example.instagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private GridView gridView;
    protected List<Post> allPosts;
    protected GridAdapter adapter;
    public static final String TAG = "DetailActivity";
    public String ObjectID;
    public ParseUser user;
    public Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = (ParseUser) extras.get("User");
        }
        gridView = findViewById(R.id.post_grid_for_otherUser);
        allPosts = new ArrayList<>();
        adapter = new GridAdapter(DetailActivity.this, allPosts);
        gridView.setAdapter(adapter);

        querryPosts();
    }

    private void querryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        query.whereEqualTo(Post.KEY_USER, user);
        query.addDescendingOrder(Post.KEY_DATE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "issue with getting post", e);
                    return;
                }
                for(Post post : posts){
                    Log.i(TAG, "Post " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });

    }
}
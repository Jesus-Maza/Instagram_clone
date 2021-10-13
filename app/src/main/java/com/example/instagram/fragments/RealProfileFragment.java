package com.example.instagram.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.instagram.Comments;
import com.example.instagram.GridAdapter;
import com.example.instagram.Post;
import com.example.instagram.PostAdapter;
import com.example.instagram.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RealProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RealProfileFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvPosts_for_profile;
    protected PostAdapter Rvadapter;
    protected List<Comments> allComments;

    public static final int PICK_IMAGE = 1;
    private File photoFile;
    public String photoFileName = "photo.jpg";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final String KEY_PROFILE_PICTURE = "Profile_picture";
    private GridView gridView;
    protected List<Post> allPosts;
    protected GridAdapter adapter2;
    public static final String TAG = "Profile_Fragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RealProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RealProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RealProfileFragment newInstance(String param1, String param2) {
        RealProfileFragment fragment = new RealProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_real_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        swipeRefreshLayout = view.findViewById(R.id.SwipeContainer);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG,"Tring to refresh");
                querryPosts();
            }
        });

        Button profile_pic;

        profile_pic = view.findViewById(R.id.setProfile_Photo);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
            }
        });

        profile_pic.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getContext(), "Opening camera", Toast.LENGTH_SHORT).show();
                launchCamera();
                return false;
            }
        });

        allComments = new ArrayList<>();
        rvPosts_for_profile = view.findViewById(R.id.rvPost_for_Profile);
        allPosts = new ArrayList<>();
        Rvadapter = new PostAdapter(getContext(), allPosts, allComments );

        rvPosts_for_profile.setAdapter(Rvadapter);
        rvPosts_for_profile.setLayoutManager(new LinearLayoutManager(getContext()));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvPosts_for_profile.addItemDecoration(itemDecoration);
        querryPosts();

    }

    private void launchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "code " + requestCode);
        if(requestCode == PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri targetUri = data.getData();
                photoFile = new File(targetUri.getPath());
                Log.i(TAG, "File trying to work.");
                ParseUser user = ParseUser.getCurrentUser();
                user.put("Profile_picture", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i(TAG, "Success");
                        } else {
                            Log.e(TAG, "error", e);
                        }
                    }
                });
            }
        }
        else if ( requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.i(TAG, "Resultcode " + resultCode);
            if (resultCode == Activity.RESULT_OK) {
                // by this point we have the camera photo on disk
                Log.i(TAG, "trying to save.");
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview

                ParseUser user = ParseUser.getCurrentUser();
                user.put("Profile_picture", new ParseFile(photoFile));
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i(TAG, "Success");
                        } else {
                            Log.e(TAG, "error", e);
                        }
                    }
                });
            }

        }
        //savePost(photoFile);
    }

    

    protected void queryComments(){
        ParseQuery<Comments> query2 = ParseQuery.getQuery(Comments.class);
        query2.include(Comments.KEY_POST);
        query2.findInBackground(new FindCallback<Comments>() {
            @Override
            public void done(List<Comments> comments, ParseException e) {
                allComments.addAll(comments);
                Rvadapter.notifyDataSetChanged();
            }
        });
    }

    private void querryPosts() {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
            query.include(Post.KEY_USER);
            query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                    Rvadapter.clear();
                    Rvadapter.addAll(posts);
                    //allPosts.addAll(posts);
                    swipeRefreshLayout.setRefreshing(false);
                }
            });

    }
}